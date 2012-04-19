package org.motechproject.whp.request;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.whp.request.validator.DateFieldValidator;
import org.motechproject.whp.exception.WHPValidationException;

@Data
public class PatientRequest {

    private String case_id;
    private String date_modified;
    private String case_type;
    private String first_name;
    private String last_name;
    private String gender;
    private String patient_type;
    private String mobile_number;
    private String disease_class;

    private String address_house_number;
    private String address_landmark;
    private String address_village;
    private String address_block;
    private String address_district;
    private String address_state;
    private String address_postal_code;

    private String smear_sample_instance_1;
    private String smear_test_date_1;
    private String smear_test_result_1;
    private String smear_sample_instance_2;
    private String smear_test_date_2;
    private String smear_test_result_2;

    private String weight_instance;
    private String weight;

    private String tb_id;
    private String provider_id;
    private String treatment_category;
    private String tb_registration_number;
    private String registration_date;
    private String age;

    @JsonIgnore
    private final DateFieldValidator dateFieldValidator = new DateFieldValidator();

    public PatientRequest() {
    }

    public PatientRequest setPatientInfo(String caseId, String firstName, String lastName, String gender, String patientType, String patientMobileNumber) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.mobile_number = patientMobileNumber;
        return this;
    }

    public PatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state, String postalCode) {
        this.address_house_number = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
        this.address_postal_code = postalCode;
        return this;
    }

    public PatientRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass, String patientAge) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        return this;
    }

    public PatientRequest setSmearTestResults(String smear_sample_instance_1, String smear_test_date_1, String smear_result_1, String smear_sample_instance_2, String smear_test_date_2, String smear_result_2) {
        this.smear_sample_instance_1 = smear_sample_instance_1;
        this.smear_test_date_1 = smear_test_date_1;
        this.smear_test_result_1 = smear_result_1;
        this.smear_sample_instance_2 = smear_sample_instance_2;
        this.smear_test_date_2 = smear_test_date_2;
        this.smear_test_result_2 = smear_result_2;
        return this;
    }

    public PatientRequest setRegistrationDetails(String registrationNumber, String registrationDate) {
        this.tb_registration_number = registrationNumber;
        this.registration_date = registrationDate;
        return this;
    }

    public PatientRequest setWeightStatistics(String weightInstance, String weight) {
        this.weight_instance = weightInstance;
        this.weight = weight;
        return this;
    }

    public void validate() throws WHPValidationException {
        validateDateFields();
    }

    private void validateDateFields() {
        dateFieldValidator.validateDateTime("date_modified", date_modified);
        dateFieldValidator.validateLocalDate("registration_date", registration_date);
        dateFieldValidator.validateLocalDate("smear_test_date_1", smear_test_date_1);
        dateFieldValidator.validateLocalDate("smear_test_date_2", smear_test_date_2);
    }
}
