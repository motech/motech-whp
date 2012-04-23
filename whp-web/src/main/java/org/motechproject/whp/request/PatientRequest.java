package org.motechproject.whp.request;

import lombok.Data;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.util.MultipleFieldErrorsMessage;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.validation.constraints.Scope;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.validation.constraints.Size;

@Data
public class PatientRequest {

    @Size(min = 10, max = 11)
    private String case_id;

    @Scope(scope = {ValidationScope.create})
    @DateTimeFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    private String date_modified;

    private String case_type;
    private String first_name;
    private String last_name;
    private String gender;
    private String patient_type;
    private String mobile_number;
    private String disease_class;

    private String address_location;
    private String address_landmark;
    private String address_village;
    private String address_block;
    private String address_district;
    private String address_state;

    private String smear_sample_instance_1;

    @Scope(scope = {ValidationScope.create})
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private String smear_test_date_1;

    private String smear_test_result_1;
    private String smear_sample_instance_2;

    @Scope(scope = {ValidationScope.create})
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private String smear_test_date_2;

    private String smear_test_result_2;

    private String weight_instance;
    private String weight;

    private String tb_id;

    @Size(min = 5, max = 6)
    private String provider_id;

    private String treatment_category;
    private String tb_registration_number;
    private String age;

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

    public PatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address_location = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
        return this;
    }

    public PatientRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass, String patientAge, String registrationNumber) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        this.tb_registration_number = registrationNumber;
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

    public PatientRequest setWeightStatistics(String weightInstance, String weight) {
        this.weight_instance = weightInstance;
        this.weight = weight;
        return this;
    }

    public void validate(BeanValidator validator) throws WHPValidationException {
        BeanPropertyBindingResult requestValidationResult = new BeanPropertyBindingResult(this, "patient");
        validator.validate(requestValidationResult.getTarget(), ValidationScope.create, requestValidationResult);
        if (requestValidationResult.hasErrors()) {
            throw new WHPValidationException(MultipleFieldErrorsMessage.getMessage(requestValidationResult));
        }
    }
}
