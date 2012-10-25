package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.validation.constraints.Scope;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.domain.*;

@Data
public class PatientRequest {

    private String case_id;
    private String first_name;
    private String last_name;
    private Gender gender;
    private Integer age;
    private PatientType patient_type;
    private String phi;

    @Scope(scope = {UpdateScope.createScope, UpdateScope.openTreatmentScope, UpdateScope.transferInScope})
    private String provider_id;
    private Address address = new Address();
    private String mobile_number;

    private String tb_id;
    private TreatmentCategory treatment_category;
    private DateTime treatmentCreationDate;
    private DiseaseClass disease_class;
    private String tb_registration_number;
    private DateTime tb_registration_date;

    private SmearTestResults smearTestResults = new SmearTestResults();
    private WeightStatistics weightStatistics = new WeightStatistics();

    private TreatmentOutcome treatment_outcome;
    private String reason;
    private DateTime date_modified;
    private boolean migrated;
    private TreatmentUpdateScenario treatmentUpdate;

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
        this.treatmentCreationDate = treatmentStartDate;
        return this;
    }

    public PatientRequest addSmearTestResults(SampleInstance smearSampleInstance,
                                              LocalDate smearTestDate1,
                                              SmearTestResult smear_result_1,
                                              LocalDate smearTestDate2,
                                              SmearTestResult smearResult2,
                                              String labName, String labNumber) {
        this.smearTestResults.add(new SmearTestRecord(smearSampleInstance, smearTestDate1, smear_result_1, smearTestDate2, smearResult2, labName, labNumber));
        return this;
    }

    public PatientRequest setWeightStatistics(SampleInstance SampleInstance, Double weight, LocalDate measuringDate) {
        if (SampleInstance != null) {
            weightStatistics.add(new WeightStatisticsRecord(SampleInstance, weight, measuringDate));
        }
        return this;
    }

    public PatientRequest setDateModified(DateTime dateModified) {
        this.date_modified = dateModified;
        return this;
    }

    public PatientRequest setTbRegistrationDate(DateTime tb_registration_date) {
        this.tb_registration_date = tb_registration_date;
        return this;
    }

    public void setWeightStatistics(WeightStatistics statistics) {
        this.weightStatistics = statistics;
    }

    public UpdateScope updateScope(boolean canBeTransferred) {
        if (treatmentUpdate == null) {
            return UpdateScope.simpleUpdate;
        } else if (TreatmentUpdateScenario.New == treatmentUpdate && PatientType.TransferredIn == getPatient_type() && canBeTransferred) {
            return UpdateScope.transferIn;
        }
        return treatmentUpdate.getScope();
    }
}
