package org.motechproject.whp.adherenceapi.validation;

import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceIVRRequestValidator {

    public static final String GLOBAL_SCOPE = "";

    private RequestValidator requestValidator;

    @Autowired
    public AdherenceIVRRequestValidator(RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    public RequestValidation isValid(Object request) {
        try {
            requestValidator.validate(request, GLOBAL_SCOPE);
            return new RequestValidation();
        } catch (WHPRuntimeException e) {
            return new RequestValidation().withErrors(e.getErrors());
        }
    }
}
