package org.motechproject.whp.request;

import lombok.Data;

@Data
public class PatientRequest {

    private String case_id;
    private String case_type;
    private String first_name;
    private String last_name;
    private String gender;
    private String patient_type;
    private String patient_mobile_num;
    private String disease_class;

    private String patient_address_house_num;
    private String patient_address_landmark;
    private String patient_address_village;
    private String patient_address_block;
    private String patient_address_district;
    private String patient_address_state;

    private String smear_sample_instance_1;
    private String smear_test_date_1;
    private String smear_result_1;
    private String smear_sample_instance_2;
    private String smear_test_date_2;
    private String smear_result_2;

    private String patient_weight_instance;
    private String patient_weight;

    private String tb_id;
    private String provider_id;
    private String treatment_category;
    private String registration_number;
    private String registration_date;


    public PatientRequest() {
    }

    public PatientRequest setPatientInfo(String caseId, String firstName, String lastName, String gender, String patientType, String patientMobileNumber) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.patient_mobile_num = patientMobileNumber;
        return this;
    }

    public PatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.patient_address_house_num = houseNumber;
        this.patient_address_landmark = landmark;
        this.patient_address_block = block;
        this.patient_address_village = village;
        this.patient_address_district = district;
        this.patient_address_state = state;
        return this;
    }

    public PatientRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
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

    public PatientRequest setWeightStatistics(String weightInstance, String weight) {
        this.patient_weight_instance = weightInstance;
        this.patient_weight = weight;
        return this;
    }
}
