package org.motechproject.whp.mapping;

public class StringToEnumeration extends WHPCustomMapper {

    @Override
    public Object convert(Object src, Class<?> destClass) {
        Object[] enumValues = destClass.getEnumConstants();
        if (src != null)
            for (Object enumValue : enumValues) {
                if (((Enum) enumValue).name().compareToIgnoreCase((src.toString().trim()))==0) {
                    return enumValue;
                }
            }
        return null;
    }
}
