package org.motechproject.whp.webservice.exception;

import org.motechproject.casexml.service.exception.CaseError;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class WHPCaseException extends CaseException {

    public WHPCaseException(WHPRuntimeException exception) {
        super(exception.getMessage(), HttpStatus.BAD_REQUEST, buildErrorMessages(exception));
    }

    private static List<CaseError> buildErrorMessages(WHPRuntimeException exception) {
        List<CaseError> errors = new ArrayList<CaseError>();
        for (WHPError whpError : exception.getErrors()) {
            errors.add(new CaseError(whpError.getErrorCode().name(), whpError.getMessage()));
        }
        return errors;
    }

}
