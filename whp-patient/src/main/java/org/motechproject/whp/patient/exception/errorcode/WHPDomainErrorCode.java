package org.motechproject.whp.patient.exception.errorcode;


public enum WHPDomainErrorCode {

    // Treatment Update
    TB_ID_DOES_NOT_MATCH("No such tb id for current treatment"),
    TREATMENT_ALREADY_IN_PROGRESS("Current treatment is already in progress"),
    TREATMENT_ALREADY_PAUSED("Current treatment is already paused"),
    TREATMENT_ALREADY_CLOSED("Current treatment is closed. Cannot pause a closed treatment"),
    TREATMENT_NOT_CLOSED("Current treatment is not closed"),
    NULL_VALUE_IN_SMEAR_TEST_RESULTS("Invalid smear test results : null value"),


    DUPLICATE_CASE_ID("patient with the same case-id is already registered."),
    CASE_ID_DOES_NOT_EXIST("Invalid case-id. No such patient"),
    NO_EXISTING_TREATMENT_FOR_CASE("Case does not have any current treatment"),
    NULL_VALUE_IN_WEIGHT_STATISTICS("Invalid weight statistics : null value"),
    NULL_VALUE_IN_ADDRESS("Invalid address : null value");


    private String message;

    WHPDomainErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return name() + " | " + getMessage();
    }
}
