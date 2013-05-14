package org.motechproject.whp.user.service;

import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.mapper.ProviderReportingService;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.motechproject.whp.common.event.EventKeys.PROVIDER_DISTRICT_CHANGE;

@Service
public class ProviderService {

    MotechAuthenticationService motechAuthenticationService;

    private AllProviders allProviders;
    private EventContext eventContext;
    private ProviderReportingService providerReportingService;

    @Autowired
    public ProviderService(MotechAuthenticationService motechAuthenticationService, AllProviders allProviders, @Qualifier("eventContext") EventContext eventContext, ProviderReportingService providerReportingService) {
        this.motechAuthenticationService = motechAuthenticationService;
        this.allProviders = allProviders;
        this.eventContext = eventContext;
        this.providerReportingService = providerReportingService;
    }

    public void registerProvider(ProviderRequest providerRequest) {
        String providerDocId = createOrUpdateProvider(providerRequest);
        try {
            motechAuthenticationService.register(providerRequest.getProviderId(), "password", providerDocId, asList(WHPRole.PROVIDER.name()), false);
        } catch (Exception e) {
            throw new WHPRuntimeException(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR, e.getMessage());
        }
    }

    public List<Provider> fetchByFilterParams(Integer startIndex, Integer rowsPerPage, String district, String providerId) {
        if (isEmpty(providerId) && isEmpty(district)) {
            return new ArrayList<>();
        }

        if (isEmpty(providerId)) {
            return allProviders.paginateByDistrict(startIndex, rowsPerPage, district);
        }

        return filterByProviderId(providerId);
    }

    private List<Provider> filterByProviderId(String providerId) {
        Provider provider = allProviders.findByProviderId(providerId);
        if(provider != null) return asList(provider);
        else return new ArrayList<>();
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

    public List<Provider> findByProviderIds(ProviderIds providerIds) {
        return allProviders.findByProviderIds(providerIds);
    }

    public ProviderIds findByDistrict(String district) {
        List<Provider> providers = allProviders.findByDistrict(district);
        return ProviderIds.ofProviders(providers);
    }

    public Provider findByMobileNumber(String mobileNumber) {
        return allProviders.findByMobileNumber(mobileNumber);
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
        providerReportingService.reportProvider(provider);
        return docId;
    }

    private String addOrReplace(Provider provider) {
        allProviders.addOrReplace(provider);
        return provider.getId();
    }

    public List<Provider> getAll() {
        return allProviders.getAll();
    }

    public Integer count(FilterParams searchCriteria) {
        if(searchCriteria.isEmpty()){
            return 0;
        }
        String selectedProvider = searchCriteria.get("providerId").toString();
        if(isNotEmpty(selectedProvider)){
            return (allProviders.findByProviderId(selectedProvider) != null ? 1 : 0);
        }

        String selectedDistrict = searchCriteria.get("district").toString();
        if(isNotEmpty(selectedDistrict)) {
            return Integer.parseInt(allProviders.findCountByDistrict(selectedDistrict));
        }
        return 0;
    }
}
