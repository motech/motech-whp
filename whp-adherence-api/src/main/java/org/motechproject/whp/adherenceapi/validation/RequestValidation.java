package org.motechproject.whp.adherenceapi.validation;

import org.motechproject.whp.common.exception.WHPError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestValidation implements Iterable<String> {

    protected List<String> errors = new ArrayList<>();

    public RequestValidation withErrors(List<WHPError> errors) {
        for (WHPError error : errors) {
            this.errors.add(error.getErrorCode().name() + ":" + error.getMessage());
        }
        return this;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public Iterator<String> iterator() {
        return errors.iterator();
    }
}
