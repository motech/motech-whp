package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;

@Data
public class PatientRequest {

    private String case_id;
    private String first_name;
    private String last_name;
    private Gender gender;
    private Integer age;
    private PatientType patient_type;
    private String phi;

    private String provider_id;
    private Address address = new Address();
    private String mobile_number;

    private String tb_id;
    private TreatmentCategory treatment_category;
    private DateTime treatmentStartDate;
    private DiseaseClass disease_class;
    private String tb_registration_number;

    private SmearTestResults smearTestResults = new SmearTestResults();
    private WeightStatistics weightStatistics = new WeightStatistics();

    private TreatmentOutcome treatment_outcome;
    private String reason;
    private DateTime date_modified;
    private boolean migrated;

    public PatientRequest() {
    }

    public PatientRequest setPatientInfo(String caseId,
                                         String firstName,
                                         String lastName,
                                         Gender gender,
                                         PatientType patientType,
                                         String patientMobileNumber,
                                         String phi) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.mobile_number = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public PatientRequest setPatientAddress(String houseNumber,
                                            String landmark,
                                            String block,
                                            String village,
                                            String district,
                                            String state) {
        this.address = new Address(houseNumber, landmark, block, village, district, state);
        return this;
    }

    public PatientRequest setTreatmentData(TreatmentCategory category,
                                           String tbId,
                                           String providerId,
                                           DiseaseClass diseaseClass,
                                           int patientAge,
                                           String registrationNumber,
                                           DateTime treatmentStartDate) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        this.tb_registration_number = registrationNumber;
        this.treatmentStartDate = treatmentStartDate;
        return this;
    }

    public PatientRequest addSmearTestResults(SmearTestSampleInstance smearSampleInstance,
                                              LocalDate smearTestDate1,
                                              SmearTestResult smear_result_1,
                                              LocalDate smearTestDate2,
                                              SmearTestResult smearResult2) {
        this.smearTestResults.add(new SmearTestRecord(smearSampleInstance, smearTestDate1, smear_result_1, smearTestDate2, smearResult2));
        return this;
    }

    public PatientRequest setWeightStatistics(WeightInstance weightInstance, Double weight, LocalDate measuringDate) {
        if (weightInstance != null) {
            weightStatistics.add(new WeightStatisticsRecord(weightInstance, weight, measuringDate));
        }
        return this;
    }

    public PatientRequest setDateModified(DateTime dateModified) {
        this.date_modified = dateModified;
        return this;
    }

    public void setWeightStatistics(WeightStatistics statistics) {
        this.weightStatistics = statistics;
    }
}
