package org.motechproject.whp.common.exception;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WHPRuntimeException extends RuntimeException {

    private List<WHPError> errors = new ArrayList<WHPError>();

    public WHPRuntimeException(WHPErrorCode errorCode) {
        this.errors.add(new WHPError(errorCode));
    }

    public WHPRuntimeException(WHPErrorCode errorCode, String message) {
        this.errors.add(new WHPError(errorCode, message));
    }

    public WHPRuntimeException(List<WHPErrorCode> errorCodes) {
        for (WHPErrorCode errorCode : errorCodes) {
            this.errors.add(new WHPError(errorCode));
        }
    }

    public WHPRuntimeException(List<WHPError> errors, String message) {
        super(message);
        this.errors = errors;
    }

    public WHPRuntimeException(Errors errors) {
        for (FieldError fieldError : errors.getFieldErrors()) {
            String message = String.format("field:%s:%s", fieldError.getField(), fieldError.getDefaultMessage());
            this.errors.add(new WHPError(WHPErrorCode.FIELD_VALIDATION_FAILED, message));
        }
    }

    public List<WHPError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public WHPError error(WHPErrorCode errorCode) {
        for (WHPError error : errors) {
            if (error.getErrorCode().equals(errorCode)) {
                return error;
            }
        }
        return null;
    }

    @Override
    public String getMessage() {
        List<String> allMessages = new ArrayList<String>();
        for (WHPError error : errors) {
            allMessages.add(error.getMessage());
        }
        return StringUtils.join(allMessages.toArray(new String[]{}), ":");
    }
}
