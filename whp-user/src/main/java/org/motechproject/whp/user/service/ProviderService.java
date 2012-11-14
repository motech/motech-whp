package org.motechproject.whp.user.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.repository.AllDistricts;
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
    private AllDistricts allDistricts;

    @Autowired
    public ProviderService(MotechAuthenticationService motechAuthenticationService, AllProviders allProviders, @Qualifier("eventContext") EventContext eventContext, AllDistricts allDistricts) {
        this.motechAuthenticationService = motechAuthenticationService;
        this.allProviders = allProviders;
        this.eventContext = eventContext;
        this.allDistricts = allDistricts;
    }

    public void registerProvider(ProviderRequest providerRequest) {
        District district = allDistricts.findByName(providerRequest.getDistrict());
        if(district == null)
            throw new WHPRuntimeException(WHPErrorCode.INVALID_DISTRICT);

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

        String docId = addOrReplace(provider);

        if (existingProvider != null) {
            if (existingProvider.hasDifferentDistrict(providerRequest.getDistrict())) {
                eventContext.send(PROVIDER_DISTRICT_CHANGE, provider.getProviderId());
            }
        }

        return docId;
    }

    private String addOrReplace(Provider provider) {
        allProviders.addOrReplace(provider);
        return provider.getId();
    }

    public List<Provider> fetchBy(String district, String providerId) {
        if(StringUtils.isEmpty(providerId) && StringUtils.isEmpty(district)){
            return allProviders.getAll();
        }

        if (StringUtils.isEmpty(providerId)) {
            return fetchBy(district);
        }
        return allProviders.findByDistrictAndProviderId(district, providerId);
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
