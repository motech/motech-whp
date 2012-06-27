package org.motechproject.whp.validation;

import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new WHPRuntimeException(result);
        }
    }
}
