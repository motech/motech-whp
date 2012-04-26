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

    public CreatePatientRequest() {
    }

    public CreatePatientRequest setPatientInfo(String caseId, String firstName, String lastName, String gender, String patientType, String patientMobileNumber, String phi) {
        this.caseId = caseId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = Gender.valueOf(gender);
        this.patientType = PatientType.valueOf(patientType);
        this.mobileNumber = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public CreatePatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address = new Address(houseNumber, landmark, block, village, district, state);
        return this;
    }

    public CreatePatientRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass, int patientAge, String registrationNumber, DateTime treatmentStartDate) {
        this.treatmentCategory = TreatmentCategory.get(category);
        this.tbId = tbId;
        this.providerId = providerId;
        this.diseaseClass = DiseaseClass.valueOf(diseaseClass);
        this.age = patientAge;
        this.tbRegistrationNumber = registrationNumber;
        this.treatmentStartDate = treatmentStartDate;
        return this;
    }

    public CreatePatientRequest setSmearTestResults(String smear_sample_instance, LocalDate smearTestDate1, String smear_result_1, LocalDate smearTestDate2, String smear_result_2) {
        SmearTestSampleInstance smearTestSampleInstance = SmearTestSampleInstance.valueOf(smear_sample_instance);
        SmearTestResult smearTestResult1 = SmearTestResult.valueOf(smear_result_1);
        SmearTestResult smearTestResult2 = SmearTestResult.valueOf(smear_result_2);
        this.smearTestResults = new SmearTestResults(smearTestSampleInstance, smearTestDate1, smearTestResult1, smearTestDate2, smearTestResult2);
        return this;
    }

    public CreatePatientRequest setWeightStatistics(String weightInstance, double weight, LocalDate measuringDate) {
        weightStatistics = new WeightStatistics(WeightInstance.valueOf(weightInstance), weight, measuringDate);
        return this;
    }

}
