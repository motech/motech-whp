package org.motechproject.whp.patient.exception;

import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.http.HttpStatus;

public class WHPCaseException extends CaseException {

    public WHPCaseException(String message, HttpStatus status) {
        super(message, status);
    }

}
