package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.*;

@Data
public class CreatePatientRequest {

    private String caseId;
    private DateTime treatmentStartDate;
    private String phi;
    private String firstName;
    private String lastName;
    private Gender gender;
    private PatientType patientType;
    private String mobileNumber;
    private DiseaseClass diseaseClass;
    private Address address;
    private SmearTestResults smearTestResults;
    private WeightStatistics weightStatistics;
    private String tbId;
    private String providerId;
    private TreatmentCategory treatmentCategory;
    private String tbRegistrationNumber;
    private int age;
    private DateTime lastModifiedDate;

    public CreatePatientRequest() {
    }

    public CreatePatientRequest setPatientInfo(String caseId, String firstName, String lastName, Gender gender, PatientType patientType, String patientMobileNumber, String phi) {
        this.caseId = caseId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.patientType = patientType;
        this.mobileNumber = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public CreatePatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address = new Address(houseNumber, landmark, block, village, district, state);
        return this;
    }

    public CreatePatientRequest setTreatmentData(TreatmentCategory category, String tbId, String providerId, DiseaseClass diseaseClass, int patientAge, String registrationNumber, DateTime treatmentStartDate) {
        this.treatmentCategory = category;
        this.tbId = tbId;
        this.providerId = providerId;
        this.diseaseClass = diseaseClass;
        this.age = patientAge;
        this.tbRegistrationNumber = registrationNumber;
        this.treatmentStartDate = treatmentStartDate;
        return this;
    }

    public CreatePatientRequest setSmearTestResults(SmearTestSampleInstance smearSampleInstance, LocalDate smearTestDate1, SmearTestResult smear_result_1, LocalDate smearTestDate2, SmearTestResult smearResult2) {
        this.smearTestResults = new SmearTestResults(smearSampleInstance, smearTestDate1, smear_result_1, smearTestDate2, smearResult2);
        return this;
    }

    public CreatePatientRequest setWeightStatistics(WeightInstance weightInstance, Double weight, LocalDate measuringDate) {
        weightStatistics = new WeightStatistics(weightInstance, weight, measuringDate);
        return this;
    }

}
