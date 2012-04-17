package org.motechproject.whp.provider.service;

import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.util.StringUtil;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/16/12
 * Time: 4:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/whp/provider/**")
public class WHPProviderService extends ProviderRegistrationService<Provider> {
    private AllProviders allProviders;

    @Autowired
    public WHPProviderService(AllProviders allProviders) {
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
