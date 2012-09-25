package org.motechproject.whp.webservice.validation;

import org.motechproject.validation.validator.root.NamedConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.Properties;

@Component
public class APIKeyValidator extends NamedConstraintValidator {

    public static final String API_KEY_VALIDATION = "api_key_validation";

    private Properties remediProperties;

    @Autowired
    public APIKeyValidator(@Qualifier("remediProperty") Properties remediProperty) {
        this.remediProperties = remediProperty;
    }

    @Override
    public String getConstraintName() {
        return API_KEY_VALIDATION;
    }

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        field.setAccessible(true);
        try {
            String  api_key = field.get(target).toString();
            if (!remediProperties.getProperty("remedi.api.key", "").equals(api_key)) {
                String message = String.format("%s:%s", "api_key", "is invalid.");
                errors.rejectValue(field.getName(), "403 Forbidden", message);
            }
        } catch (IllegalAccessException ignored) {
        }
    }
}