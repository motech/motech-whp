package org.motechproject.whp.v0.exception;

import org.apache.commons.lang.StringUtils;

public class WHPErrorV0 {

    private WHPErrorCodeV0 errorCode;
    private String message;

    public WHPErrorV0(WHPErrorCodeV0 errorCode) {
        this.errorCode = errorCode;
    }

    public WHPErrorV0(WHPErrorCodeV0 errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public WHPErrorCodeV0 getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        if (StringUtils.isBlank(message))
            return errorCode.getMessage();
        return message;
    }

}