package org.motechproject.whp.webservice;

import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.mapper.ProviderMapper;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.motechproject.whp.request.ProviderRequest;
import org.motechproject.whp.util.MultipleFieldErrorsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provider/**")
public class ProviderWebService extends ProviderRegistrationService<ProviderRequest> {

    private AllProviders allProviders;
    private Validator providerValidator;

    @Autowired
    public ProviderWebService(AllProviders allProviders, Validator validator) {
        super(ProviderRequest.class);
        this.allProviders = allProviders;
        providerValidator = validator;
    }

    @Override
    public void createOrUpdate(ProviderRequest providerRequest) throws OpenRosaRegistrationValidationException {
        validateProvider(providerRequest);
        Provider provider = new ProviderMapper().map(providerRequest);
        allProviders.addOrReplace(provider);
    }

    private void validateProvider(ProviderRequest providerRequest) throws OpenRosaRegistrationValidationException {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(providerRequest, "provider");
        providerValidator.validate(providerRequest, errors);
        if (errors.hasErrors()) {
            throw new OpenRosaRegistrationValidationException(MultipleFieldErrorsMessage.getMessage(errors), HttpStatus.BAD_REQUEST);
        }
    }

}
