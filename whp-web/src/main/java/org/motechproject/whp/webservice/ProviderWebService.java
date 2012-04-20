package org.motechproject.whp.webservice;

import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.motechproject.whp.util.MultipleFieldErrorsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provider/**")
public class ProviderWebService extends ProviderRegistrationService<Provider> {

    private AllProviders allProviders;
    private Validator providerValidator;

    @Autowired
    public ProviderWebService(AllProviders allProviders, Validator validator) {
        super(Provider.class);
        this.allProviders = allProviders;
        providerValidator = validator;
    }

    @Override
    public void createOrUpdate(Provider provider) throws OpenRosaRegistrationValidationException {
        BeanPropertyBindingResult validationErrors = new BeanPropertyBindingResult(provider, "provider");
        providerValidator.validate(provider, validationErrors);
        if (validationErrors.hasErrors()) {
            throw new OpenRosaRegistrationValidationException(MultipleFieldErrorsMessage.getMessage(validationErrors), HttpStatus.BAD_REQUEST);
        }
        allProviders.addOrReplace(provider);
    }
}
