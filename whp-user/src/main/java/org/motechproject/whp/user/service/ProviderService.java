package org.motechproject.whp.user.service;

import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProviderService {

    MotechAuthenticationService motechAuthenticationService;

    AllProviders allProviders;

    @Autowired
    public ProviderService(MotechAuthenticationService motechAuthenticationService, AllProviders allProviders) {
        this.motechAuthenticationService = motechAuthenticationService;
        this.allProviders = allProviders;
    }

    public void registerProvider(ProviderRequest providerRequest) {
        String providerDocId = createProvider(providerRequest);
        try{
            // TODO : make this idempotent
            motechAuthenticationService.register(providerRequest.getProviderId(), "password", providerDocId, Arrays.asList(WHPRole.PROVIDER.name()), false);
        }
        catch (Exception e) {
            throw new WHPRuntimeException(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR,e.getMessage());
        }
    }

    private String createProvider(ProviderRequest providerRequest) {
        Provider provider = new Provider(providerRequest.getProviderId(), providerRequest.getPrimaryMobile(), providerRequest.getDistrict(), providerRequest.getLastModifiedDate());
        provider.setSecondaryMobile(providerRequest.getSecondaryMobile());
        provider.setTertiaryMobile(providerRequest.getTertiaryMobile());
        allProviders.addOrReplace(provider);
        return provider.getId();
    }

    public List<Provider> fetchBy(String district, String providerId) {
        if(providerId.isEmpty()) {
            return fetchBy(district);
        } else {
            return allProviders.findByDistrictAndProviderId(district, providerId);
        }
    }

    public List<Provider> fetchBy(String district){
        return allProviders.findByDistrict(district);
    }

    public Map<String, MotechUser> fetchAllWebUsers() {
        Map<String, MotechUser> allWebUsers = new HashMap();
        for (MotechUser motechUser : motechAuthenticationService.findByRole(WHPRole.PROVIDER.name())) {
            allWebUsers.put(motechUser.getUserName(), motechUser);
        }
        return allWebUsers;
    }

    public Provider fetchByProviderId(String providerId){
        return allProviders.findByProviderId(providerId);
    }

    public boolean isRegisteredMobileNumber(String mobileNumber) {
        return allProviders.findByMobileNumber(mobileNumber) != null;
    }
}
