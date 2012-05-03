package org.motechproject.whp.application.mapping;

public class StringToEnumeration extends WHPCustomMapper {

    @Override
    public Object convert(Object src, Class<?> destClass) {
        Object[] enumValues = destClass.getEnumConstants();
        for (Object enumValue : enumValues) {
            if (((Enum) enumValue).name().equals(src)) {
                return enumValue;
            }
        }
        return null;
    }
}
