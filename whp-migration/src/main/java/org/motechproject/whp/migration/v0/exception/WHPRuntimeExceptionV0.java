package org.motechproject.whp.migration.v0.exception;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WHPRuntimeExceptionV0 extends RuntimeException {

    private final List<WHPErrorV0> errors = new ArrayList<WHPErrorV0>();

    public WHPRuntimeExceptionV0(WHPErrorCodeV0 errorCode) {
        this.errors.add(new WHPErrorV0(errorCode));
    }

    public WHPRuntimeExceptionV0(WHPErrorCodeV0 errorCode, String message) {
        this.errors.add(new WHPErrorV0(errorCode, message));
    }

    public WHPRuntimeExceptionV0(List<WHPErrorCodeV0> errors) {
        for (WHPErrorCodeV0 errorCode : errors) {
            this.errors.add(new WHPErrorV0(errorCode));
        }
    }

    public WHPRuntimeExceptionV0(Errors errors) {
        for (FieldError fieldError : errors.getFieldErrors()) {
            String message = String.format("field:%s:%s", fieldError.getField(), fieldError.getDefaultMessage());
            this.errors.add(new WHPErrorV0(WHPErrorCodeV0.FIELD_VALIDATION_FAILED, message));
        }
    }

    public List<WHPErrorV0> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public WHPErrorV0 error(WHPErrorCodeV0 errorCode) {
        for (WHPErrorV0 error : errors) {
            if (error.getErrorCode().equals(errorCode)) {
                return error;
            }
        }
        return null;
    }

    @Override
    public String getMessage() {
        List<String> allMessages = new ArrayList<String>();
        for (WHPErrorV0 error : errors) {
            allMessages.add(error.getMessage());
        }
        return StringUtils.join(allMessages.toArray(new String[]{}), ":");
    }
}