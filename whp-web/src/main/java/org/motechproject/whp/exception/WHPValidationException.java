package org.motechproject.whp.exception;

import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class WHPValidationException extends CaseException {
    public WHPValidationException(String message,HttpStatus status) {
        super(message,status);
    }
    public WHPValidationException(String message,HttpStatus status,Map<String,String> errorMessages) {
        super(message,status,errorMessages);
    }
}
