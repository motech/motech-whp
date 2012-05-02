package org.motechproject.whp.webservice;

import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.validation.validator.BeanValidator;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.mapper.ProviderRequestMapper;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.util.MultipleFieldErrorsMessage;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provider/**")
public class ProviderWebService extends ProviderRegistrationService<ProviderWebRequest> {

    RegistrationService registrationService;
    RequestValidator providerValidator;

    @Autowired
    public ProviderWebService(RequestValidator validator, RegistrationService registrationService) {
        super(ProviderWebRequest.class);
        providerValidator = validator;
        this.registrationService = registrationService;
    }

    @Override
    public void createOrUpdate(ProviderWebRequest providerWebRequest){
        providerValidator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
        ProviderRequest providerRequest = new ProviderRequestMapper().map(providerWebRequest);
        registrationService.registerProvider(providerRequest);
    }
}
