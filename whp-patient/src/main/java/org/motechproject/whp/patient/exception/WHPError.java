package org.motechproject.whp.patient.exception;

import org.apache.commons.lang.StringUtils;

public class WHPError {

    private WHPErrorCode errorCode;
    private String message;

    public WHPError(WHPErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public WHPError(WHPErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public WHPErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        if (StringUtils.isBlank(message))
            return errorCode.getMessage();
        return message;
    }

}
