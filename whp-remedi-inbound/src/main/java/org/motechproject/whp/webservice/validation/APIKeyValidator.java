package org.motechproject.whp.webservice.validation;

import org.motechproject.validation.validator.root.NamedConstraintValidator;
import org.motechproject.whp.common.service.RemediProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

@Component
public class APIKeyValidator extends NamedConstraintValidator {

    public static final String API_KEY_VALIDATION = "api_key_validation";

    private RemediProperties remediProperties;

    @Autowired
    public APIKeyValidator(RemediProperties remediProperties) {
        this.remediProperties = remediProperties;
    }

    @Override
    public String getConstraintName() {
        return API_KEY_VALIDATION;
    }

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        field.setAccessible(true);
        try {
            String  api_key = (String) field.get(target);
            if (!remediProperties.getApiKey().equals(api_key)) {
                String message = String.format("%s:%s", "api_key", "is invalid.");
                errors.rejectValue(field.getName(), "403 Forbidden", message);
            }
        } catch (IllegalAccessException ignored) {
        }
    }
}
