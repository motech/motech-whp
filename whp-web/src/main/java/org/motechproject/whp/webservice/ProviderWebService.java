package org.motechproject.whp.webservice;

import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.mapper.ProviderMapper;
import org.motechproject.whp.request.ProviderRequest;
import org.motechproject.whp.util.MultipleFieldErrorsMessage;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provider/**")
public class ProviderWebService extends ProviderRegistrationService<ProviderRequest> {

    private RegistrationService registrationService;
    private BeanValidator providerValidator;

    @Autowired
    public ProviderWebService(BeanValidator validator, RegistrationService registrationService) {
        super(ProviderRequest.class);
        providerValidator = validator;
        this.registrationService = registrationService;
    }

    @Override
    public void createOrUpdate(ProviderRequest providerRequest) throws OpenRosaRegistrationValidationException {
        validateProvider(providerRequest);
        Provider provider = new ProviderMapper().map(providerRequest);
        registrationService.registerProvider(provider);
    }

    private void validateProvider(ProviderRequest providerRequest) throws OpenRosaRegistrationValidationException {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(providerRequest, "provider");
        providerValidator.validate(providerRequest, ValidationScope.create, errors);
        if (errors.hasErrors()) {
            throw new OpenRosaRegistrationValidationException(MultipleFieldErrorsMessage.getMessage(errors), HttpStatus.BAD_REQUEST);
        }
    }

}
