package org.motechproject.whp.mapping;

public class StringToEnumeration extends WHPCustomMapper {

    @Override
    public Object convert(Object src, Class<?> destClass) {
        Object[] enumValues = destClass.getEnumConstants();
        if (src != null) {
            String value = src.toString().trim();
            for (Object enumValue : enumValues) {
                if (((Enum) enumValue).name().compareToIgnoreCase(value) == 0) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
