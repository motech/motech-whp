package org.motechproject.whp.webservice;

import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.exception.WHPProviderException;
import org.motechproject.whp.mapper.ProviderRequestMapper;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public void createOrUpdate(ProviderWebRequest providerWebRequest) {
        try {
            providerValidator.validate(providerWebRequest, UpdateScope.createScope);
            ProviderRequest providerRequest = new ProviderRequestMapper().map(providerWebRequest);
            registrationService.registerProvider(providerRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPProviderException(e);
        }
    }
}
