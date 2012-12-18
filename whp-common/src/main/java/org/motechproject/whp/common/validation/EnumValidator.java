package org.motechproject.whp.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumValidator implements ConstraintValidator<Enumeration, String> {

    private Class<? extends java.lang.Enum> value;

    @Override
    public void initialize(Enumeration enumeration) {
        value = enumeration.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return allEnumValues().contains(value);
    }

    private List<String> allEnumValues() {
        List<String> allEnumerations = new ArrayList<String>();
        for (Enum enumValues : value.getEnumConstants()) {
            allEnumerations.add(enumValues.name());
        }
        return allEnumerations;
    }
}
