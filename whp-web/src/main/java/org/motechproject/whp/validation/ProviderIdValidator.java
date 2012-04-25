package org.motechproject.whp.validation;


import org.motechproject.whp.repository.AllProviders;
import org.motechproject.whp.validation.validator.root.NamedConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

@Component
public class ProviderIdValidator extends NamedConstraintValidator {

    public static final String PROVIDER_ID_CONSTRAINT = "provider.id";

    AllProviders allProviders;

    @Autowired
    public ProviderIdValidator(AllProviders allProviders) {
        this.allProviders = allProviders;
    }

    @Override
    public String getConstraintName() {
        return PROVIDER_ID_CONSTRAINT;
    }

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        field.setAccessible(true);
        try {
            String providerId = field.get(target).toString();
            if (providerWithIdExists(providerId)) {
                String message = String.format("%s:%s", "No provider is found with id", providerId);
                errors.rejectValue(field.getName(), "provider-not-found", message);
            }
        } catch (IllegalAccessException ignored) {
        } catch (NullPointerException e) {
            errors.rejectValue(field.getName(), "provider-id-invalid", "Provider Id cannot be null");
        }
    }

    private boolean providerWithIdExists(String providerId) {
        return null == allProviders.findByProviderId(providerId);
    }
}
