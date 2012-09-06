package org.motechproject.whp.user.service;

import org.motechproject.scheduler.context.EventContext;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.whp.common.event.EventKeys.PROVIDER_DISTRICT_CHANGE;

@Service
public class ProviderService {

    MotechAuthenticationService motechAuthenticationService;

    private AllProviders allProviders;
    private EventContext eventContext;

    @Autowired
    public ProviderService(MotechAuthenticationService motechAuthenticationService, AllProviders allProviders, @Qualifier("eventContext") EventContext eventContext) {
        this.motechAuthenticationService = motechAuthenticationService;
        this.allProviders = allProviders;
        this.eventContext = eventContext;
    }

    public void registerProvider(ProviderRequest providerRequest) {
        String providerDocId = createOrUpdateProvider(providerRequest);
        try {
            // TODO : make this idempotent
            motechAuthenticationService.register(providerRequest.getProviderId(), "password", providerDocId, Arrays.asList(WHPRole.PROVIDER.name()), false);
        } catch (Exception e) {
            throw new WHPRuntimeException(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR, e.getMessage());
        }
    }

    String createOrUpdateProvider(ProviderRequest providerRequest) {
        Provider provider = providerRequest.makeProvider();
        Provider existingProvider = allProviders.findByProviderId(providerRequest.getProviderId());

        if (existingProvider == null) {
            return addProvider(provider);
        }

        provider.setId(existingProvider.getId()); //set existing DocId to new provider

        if (existingProvider.hasDifferentDistrict(providerRequest.getDistrict())) {
            eventContext.send(PROVIDER_DISTRICT_CHANGE, provider.getProviderId());
        }

        return updateProvider(provider);
    }

    private String updateProvider(Provider provider) {
        allProviders.update(provider);
        return provider.getId();
    }

    private String addProvider(Provider provider) {
        allProviders.add(provider);
        return provider.getId();
    }

    public List<Provider> fetchBy(String district, String providerId) {
        if (providerId.isEmpty()) {
            return fetchBy(district);
        } else {
            return allProviders.findByDistrictAndProviderId(district, providerId);
        }
    }

    public List<Provider> fetchBy(String district) {
        return allProviders.findByDistrict(district);
    }

    public Map<String, MotechUser> fetchAllWebUsers() {
        Map<String, MotechUser> allWebUsers = new HashMap();
        for (MotechUser motechUser : motechAuthenticationService.findByRole(WHPRole.PROVIDER.name())) {
            allWebUsers.put(motechUser.getUserName(), motechUser);
        }
        return allWebUsers;
    }

    public Provider findByProviderId(String providerId) {
        return allProviders.findByProviderId(providerId);
    }

    public Provider findByMobileNumber(String mobileNumber) {
        return allProviders.findByMobileNumber(mobileNumber);
    }
}
