package org.motechproject.whp.common.exception;


public enum WHPErrorCode {

    TB_ID_DOES_NOT_MATCH("No such tb id for current treatment"),
    TREATMENT_DETAILS_DO_NOT_MATCH("Transfer In is not supported with change in treatment category"),
    TREATMENT_ALREADY_IN_PROGRESS("Current treatment is already in progress"),
    TREATMENT_ALREADY_PAUSED("Current treatment is already paused"),
    TREATMENT_ALREADY_CLOSED("Current treatment is already closed"),
    TREATMENT_NOT_CLOSED("Current treatment is not closed"),

    DUPLICATE_CASE_ID("Patient with the same case-id is already registered"),
    DUPLICATE_PROVIDER_ID("Provider with the same provider-id is already registered"),
    INVALID_PATIENT_CASE_ID("Invalid case-id. No such patient"),
    NO_EXISTING_TREATMENT_FOR_CASE("Case does not have any current treatment"),
    NULL_VALUE_IN_WEIGHT_STATISTICS("Invalid weight statistics : null value"),
    NULL_VALUE_IN_ADDRESS("Invalid address : null value"),

    FIELD_VALIDATION_FAILED("Field Validation failed"),
    WEB_ACCOUNT_REGISTRATION_ERROR("Error occured while tying to register user"),

    INVALID_CONTAINER_ID("No such container id in motech"),
    SPUTUM_LAB_RESULT_IS_INCOMPLETE("Lab results are incomplete"),
    PATIENT_NOT_FOUND("No such patient is registered in WHP"),
    NO_LAB_RESULTS_IN_CONTAINER("The container does not contain lab results"),
    INVALID_SPUTUM_TEST_INSTANCE("The sputum test instance is invalid"),
    NO_SUCH_TREATMENT_EXISTS("No such treatment exists for patient"),
    INVALID_PHONE_NUMBER("No provider found for the given phone number"),
    CONTAINER_ALREADY_REGISTERED("The container Id is already registered"),
    INVALID_PHASE("Phase should be one of [PreTreatment, InTreatment]"),
    NULL_VALUE_IN_TB_REGISTRATION_DATE("TB Registration date is expected for PreTreatment phase"),
    INVALID_TB_REGISTRATION_DATE("TB Registration date should be of format dd/MM/yyyy");

    private String message;

    WHPErrorCode(String message) {
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
