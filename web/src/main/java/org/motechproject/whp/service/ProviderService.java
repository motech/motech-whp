package org.motechproject.whp.service;

import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.util.StringUtil;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/whp/provider/**")
public class ProviderService extends ProviderRegistrationService<Provider> {
    private AllProviders allProviders;

    @Autowired
    public ProviderService(AllProviders allProviders) {
        super(Provider.class);
        this.allProviders = allProviders;
    }

    @Override
    public void createOrUpdate(Provider provider) {
        validateMandatoryFields(provider);
        allProviders.addOrReplace(provider);
    }

    void validateMandatoryFields(Provider provider) {
        if(StringUtil.isNullOrEmpty(provider.getProviderId()) ||
                StringUtil.isNullOrEmpty(provider.getPrimaryMobile()) ||
                StringUtil.isNullOrEmpty(provider.getDistrict()))
            throw new WHPValidationException("Mandatory fields providerId, primaryMobile and district cannot be null or empty");
    }
}
