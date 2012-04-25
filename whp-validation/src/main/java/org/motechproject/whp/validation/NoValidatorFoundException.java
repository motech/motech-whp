package org.motechproject.whp.validation;


public class NoValidatorFoundException extends RuntimeException {

    public NoValidatorFoundException(String constraintName) {
        super(String.format("%s %s", "No validator found for the constraint with name", constraintName));
    }
}
