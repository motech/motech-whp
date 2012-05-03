package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.*;

@Data
public class PatientRequest {

    private String case_id;
    private DateTime treatmentStartDate;
    private String phi;
    private String first_name;
    private String last_name;
    private Gender gender;
    private PatientType patient_type;
    private String mobile_number;
    private DiseaseClass disease_class;
    private Address address = new Address();
    private SmearTestResults smearTestResults = new SmearTestResults();
    private WeightStatistics weightStatistics = new WeightStatistics();
    private String tb_id;
    private String provider_id;
    private TreatmentCategory treatment_category;
    private String tb_registration_number;
    private int age;
    private DateTime date_modified;

    public PatientRequest() {
    }

    public PatientRequest setPatientInfo(String caseId, String firstName, String lastName, Gender gender, PatientType patientType, String patientMobileNumber, String phi) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.mobile_number = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public PatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address = new Address(houseNumber, landmark, block, village, district, state);
        return this;
    }

    public PatientRequest setTreatmentData(TreatmentCategory category, String tbId, String providerId, DiseaseClass diseaseClass, int patientAge, String registrationNumber, DateTime treatmentStartDate) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        this.tb_registration_number = registrationNumber;
        this.treatmentStartDate = treatmentStartDate;
        return this;
    }

    public PatientRequest setSmearTestResults(SmearTestSampleInstance smearSampleInstance, LocalDate smearTestDate1, SmearTestResult smear_result_1, LocalDate smearTestDate2, SmearTestResult smearResult2) {
        this.smearTestResults = new SmearTestResults(smearSampleInstance, smearTestDate1, smear_result_1, smearTestDate2, smearResult2);
        return this;
    }

    public PatientRequest setWeightStatistics(WeightInstance weightInstance, Double weight, LocalDate measuringDate) {
        weightStatistics = new WeightStatistics(weightInstance, weight, measuringDate);
        return this;
    }

}
