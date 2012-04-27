package org.motechproject.whp.webservice;

import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.mapper.ProviderRequestMapper;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.request.ProviderWebRequest;
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
public class ProviderWebService extends ProviderRegistrationService<ProviderWebRequest> {

    private RegistrationService registrationService;
    private BeanValidator providerValidator;

    @Autowired
    public ProviderWebService(BeanValidator validator, RegistrationService registrationService) {
        super(ProviderWebRequest.class);
        providerValidator = validator;
        this.registrationService = registrationService;
    }

    @Override
    public void createOrUpdate(ProviderWebRequest providerWebRequest) throws OpenRosaRegistrationValidationException {
        validateProvider(providerWebRequest);
        ProviderRequest providerRequest = new ProviderRequestMapper().map(providerWebRequest);
        registrationService.registerProvider(providerRequest);
    }

    private void validateProvider(ProviderWebRequest providerWebRequest) throws OpenRosaRegistrationValidationException {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(providerWebRequest, "provider");
        providerValidator.validate(providerWebRequest, ValidationScope.create, errors);
        if (errors.hasErrors()) {
            throw new OpenRosaRegistrationValidationException(MultipleFieldErrorsMessage.getMessage(errors), HttpStatus.BAD_REQUEST);
        }
    }

}
