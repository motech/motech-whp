package org.motechproject.whp.mapping;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

public abstract class WHPCustomMapper implements CustomConverter {

    @Override
    public Object convert(Object dest, Object src, Class<?> destClass, Class<?> srcClass) {
        if (src == null) {
            return null;
        }
        if (canConvert(src, srcClass)) {
            return convert(src, destClass);
        }
        throw new MappingException("Incompatible type conversion");
    }

    private boolean canConvert(Object src, Class<?> srcClass) {
        return src.getClass().equals(srcClass);
    }

    protected abstract Object convert(Object src, Class<?> destClass);
}
