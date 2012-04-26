package org.motechproject.whp.patient.exception;

import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class WHPException extends CaseException {

    public WHPException(String message,HttpStatus status) {
        super(message,status);
    }

    public WHPException(String message,HttpStatus status,Map<String,String> errorMessages) {
        super(message,status,errorMessages);
    }

}
