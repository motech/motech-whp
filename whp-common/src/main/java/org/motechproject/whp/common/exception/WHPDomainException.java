package org.motechproject.whp.common.exception;

public class WHPDomainException extends RuntimeException {

    private String exceptionMessage;

    public WHPDomainException(String exceptionMessage){
        this.exceptionMessage = exceptionMessage;
    }

}
