package org.motechproject.whp.user.validation;

import org.motechproject.validation.validator.root.NamedConstraintValidator;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

@Component
public class DistrictValidator extends NamedConstraintValidator{

    public static final String DISTRICT_CONSTRAINT = "district";
    private AllDistricts allDistricts;

    @Autowired
    public DistrictValidator(AllDistricts allDistricts) {
        this.allDistricts = allDistricts;
    }

    @Override
    public String getConstraintName() {
        return DISTRICT_CONSTRAINT;
    }

    @Override
    public void validateField(Object target, Field field, Errors errors) {
        field.setAccessible(true);
        try {
            String district = field.get(target).toString();
            if (!isValid(district)) {
                String message = String.format("%s:%s", "Not a valid district", district);
                errors.rejectValue(field.getName(), "district-not-valid", message);
            }
        } catch (IllegalAccessException ignored) {
        } catch (NullPointerException e) {
            errors.rejectValue(field.getName(), "district-invalid", "District is invalid");
        }
    }

    boolean isValid(String district) {
        return allDistricts.getAll().contains(new District(district));
    }
}
