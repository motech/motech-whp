package org.motechproject.whp.common.exception;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

@EqualsAndHashCode
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
