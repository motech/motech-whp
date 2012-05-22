package org.motechproject.whp.patient.exception;

import org.dozer.MappingException;
import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class WHPCaseException extends CaseException {

    public WHPCaseException(WHPRuntimeException exception) {
        super(exception.getMessage(), HttpStatus.BAD_REQUEST, buildErrorMessageMap(exception));
    }

    public WHPCaseException(final MappingException exception) {
        super(exception.getMessage(), HttpStatus.BAD_REQUEST, new HashMap<String, String>() {{
            put(WHPErrorCode.FIELD_VALIDATION_FAILED.name(), exception.getMessage());
        }});
    }

    private static Map<String, String> buildErrorMessageMap(WHPRuntimeException exception) {
        HashMap<String, String> errors = new HashMap<String, String>();
        for (WHPError whpError : exception.getErrors()) {
            errors.put(whpError.getErrorCode().name(), whpError.getMessage());
        }
        return errors;
    }

}
