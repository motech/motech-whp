package org.motechproject.whp.request;

import lombok.Data;

@Data
public class PatientRequest {

    private String case_id;
    private String case_type;
    private String first_name;
    private String last_name;
    private String gender;

    private String smear_sample_instance_1;
    private String smear_test_date_1;
    private String smear_result_1;
    private String smear_sample_instance_2;
    private String smear_test_date_2;
    private String smear_result_2;

    private String tb_id;
    private String provider_id;
    private String treatment_category;
    private String registration_number;
    private String registration_date;


    public PatientRequest() {
    }

    public PatientRequest setPatientInfo(String caseId, String firstName, String lastName, String gender) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        return this;
    }

    public PatientRequest setTreatmentData(String category, String tbId, String providerId) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        return this;
    }

    public PatientRequest setSmearTestResults(String smear_sample_instance_1, String smear_test_date_1, String smear_result_1, String smear_sample_instance_2, String smear_test_date_2, String smear_result_2) {
        this.smear_sample_instance_1 = smear_sample_instance_1;
        this.smear_test_date_1 = smear_test_date_1;
        this.smear_result_1 = smear_result_1;
        this.smear_sample_instance_2 = smear_sample_instance_2;
        this.smear_test_date_2 = smear_test_date_2;
        this.smear_result_2 = smear_result_2;
        return this;
    }

    public PatientRequest setRegistrationDetails(String registrationNumber, String registrationDate) {
        this.registration_number = registrationNumber;
        this.registration_date = registrationDate;
        return this;
    }

}
