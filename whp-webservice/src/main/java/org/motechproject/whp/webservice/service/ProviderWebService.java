package org.motechproject.whp.webservice.service;

import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.webservice.exception.WHPProviderException;
import org.motechproject.whp.webservice.mapper.ProviderRequestMapper;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provider/**")
public class ProviderWebService extends ProviderRegistrationService<ProviderWebRequest> {

    ProviderService providerService;
    RequestValidator providerValidator;

    @Autowired
    public ProviderWebService(RequestValidator validator, ProviderService providerService) {
        super(ProviderWebRequest.class);
        providerValidator = validator;
        this.providerService = providerService;
    }

    @Override
    public void createOrUpdate(ProviderWebRequest providerWebRequest) {
        try {
            providerValidator.validate(providerWebRequest, UpdateScope.createScope);
            ProviderRequest providerRequest = new ProviderRequestMapper().map(providerWebRequest);
            providerService.registerProvider(providerRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPProviderException(e);
        }
    }
}
