package org.motechproject.whp.patient.exception;

import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WHPDomainException extends RuntimeException {

    private final List<WHPDomainErrorCode> errorCodes = new ArrayList<WHPDomainErrorCode>();

    public WHPDomainException(WHPDomainErrorCode errorCodes) {
        this.errorCodes.add(errorCodes);
    }

    public WHPDomainException(List<WHPDomainErrorCode> errorCodes) {
        this.errorCodes.addAll(errorCodes);
    }

    public List<WHPDomainErrorCode> getErrorCodes() {
        return Collections.unmodifiableList(errorCodes);
    }

    @Override
    public String toString() {
        return errorCodes.toString();
    }
}
