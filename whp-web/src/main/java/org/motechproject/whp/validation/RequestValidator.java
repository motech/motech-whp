package org.motechproject.whp.validation;

import org.motechproject.validation.validator.BeanValidator;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.util.MultipleFieldErrorsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

@Component
public class RequestValidator {

    BeanValidator validator;

    @Autowired
    public RequestValidator(BeanValidator validator) {
        this.validator = validator;
    }

    public void validate(Object target, String scope) {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
        validator.validate(target, scope, result);
        if (result.hasErrors()) {
            throw new WHPException(MultipleFieldErrorsMessage.getMessage(result), HttpStatus.BAD_REQUEST);
        }
    }
}
