package org.motechproject.whp.patient.builder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateScenario;
import org.motechproject.whp.patient.domain.*;

import java.util.Arrays;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.builder.PatientBuilder.*;

public class PatientRequestBuilder {

    public static final String NEW_TB_ID = "newtbid";
    public static final String NEW_PROVIDER_ID = "newproviderid";
    protected final TreatmentCategory category01 = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
    protected final TreatmentCategory category10 = new TreatmentCategory("RNTCP Category 1", "10", 3, 8, 24, 4, 12, 18, 54, Arrays.asList(DayOfWeek.Monday));

    private PatientRequest patientRequest = new PatientRequest();

    public PatientRequest build() {
        return patientRequest;
    }

    public PatientRequestBuilder withDefaults() {
        patientRequest = new PatientRequest()
                .setPatientInfo(PATIENT_ID, "Foo", "Bar", Gender.M, PatientType.Chronic, "1234567890", "phi")
                .setTreatmentData(category01, TB_ID, PROVIDER_ID, DiseaseClass.P, 50, "registrationNumber", DateUtil.newDateTime(2010, 6, 21, 10, 0, 5))
                .addSmearTestResults(SputumTrackingInstance.PreTreatment, DateUtil.newDate(2010, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2010, 5, 21), SmearTestResult.Positive, "labName", "labNumber")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setWeightStatistics(SputumTrackingInstance.PreTreatment, 99.7, DateUtil.newDate(2010, 5, 19))
                .setDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 0))
                .setTbRegistrationDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 0));
        return this;
    }

    public PatientRequestBuilder withSimpleUpdateFields() {
        patientRequest = new PatientRequest()
                .setPatientInfo(PATIENT_ID, null, null, null, null, "9087654321", null)
                .setPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .addSmearTestResults(SputumTrackingInstance.EndTreatment, DateUtil.newDate(2010, 7, 19), SmearTestResult.Negative, DateUtil.newDate(2010, 9, 20), SmearTestResult.Negative, "labName", "labNumber")
                .setWeightStatistics(SputumTrackingInstance.EndTreatment, 99.7, DateUtil.newDate(2010, 9, 20))
                .setTreatmentData(null, TB_ID, null, null, 50, "newRegistrationNumber", DateUtil.newDateTime(2010, 9, 20, 10, 10, 0))
                .setTbRegistrationDate(now());
        return this;
    }

    public PatientRequestBuilder withMandatoryFieldsForOpenNewTreatment() {
        patientRequest.setDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50));
        patientRequest.setCase_id(PATIENT_ID);
        patientRequest.setDate_modified(now());
        patientRequest.setTbRegistrationDate(now());
        patientRequest.setTb_id("tbid");
        patientRequest.setTreatment_category(category10);
        patientRequest.setProvider_id("newproviderid");
        patientRequest.setDisease_class(DiseaseClass.E);
        patientRequest.setTreatmentUpdate(TreatmentUpdateScenario.New);
        patientRequest.addSmearTestResults(SputumTrackingInstance.EndIP, today(), SmearTestResult.Negative, today(), SmearTestResult.Negative, "labName", "labNumber");
        patientRequest.setWeightStatistics(SputumTrackingInstance.EndIP, 67.56, patientRequest.getDate_modified().toLocalDate());

        return this;
    }

    public PatientRequestBuilder withMandatoryFieldsForCloseTreatment() {
        patientRequest.setCase_id(PATIENT_ID);
        patientRequest.setDate_modified(now());
        patientRequest.setTb_id(TB_ID);
        patientRequest.setTreatmentUpdate(TreatmentUpdateScenario.Close);
        patientRequest.setTreatment_outcome(TreatmentOutcome.Cured);
        patientRequest.setTbRegistrationDate(now().minusDays(3));
        return this;
    }

    public PatientRequestBuilder withMandatoryFieldsForPauseTreatment() {
        patientRequest.setCase_id(PATIENT_ID);
        patientRequest.setDate_modified(now());
        patientRequest.setTb_id(TB_ID);
        patientRequest.setTreatmentUpdate(TreatmentUpdateScenario.Pause);
        patientRequest.setReason("paws");
        return this;
    }

    public PatientRequestBuilder withMandatoryFieldsForRestartTreatment() {
        patientRequest.setCase_id(PATIENT_ID);
        patientRequest.setDate_modified(now());
        patientRequest.setTreatmentUpdate(TreatmentUpdateScenario.Restart);
        patientRequest.setTb_id(TB_ID);
        patientRequest.setReason("swap");
        return this;
    }

    public PatientRequestBuilder withMandatoryFieldsForTransferInTreatment() {
        patientRequest.setProvider_id(NEW_PROVIDER_ID);
        patientRequest.setCase_id(PATIENT_ID);
        patientRequest.setDate_modified(now());
        patientRequest.setTbRegistrationDate(now());
        patientRequest.setTb_id(NEW_TB_ID);
        patientRequest.setTreatmentUpdate(TreatmentUpdateScenario.New);
        patientRequest.setPatient_type(PatientType.TransferredIn);
        return this;
    }

    public PatientRequestBuilder withMandatoryFieldsForImportPatient() {
        TreatmentCategory category = category01;
        patientRequest = new PatientRequest()
                .setPatientInfo(PATIENT_ID, "Foo", "Bar", Gender.M, PatientType.Chronic, "1234567890", "phi")
                .setTreatmentData(category, TB_ID, "123456", DiseaseClass.P, 50, "registrationNumber", DateUtil.newDateTime(2010, 6, 21, 10, 0, 5))
                .addSmearTestResults(SputumTrackingInstance.PreTreatment, DateUtil.newDate(2010, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2010, 5, 21), SmearTestResult.Positive, "labName", "labNumber")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 0))
                .setTbRegistrationDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 0));
        return this;
    }

    public PatientRequestBuilder withProviderId(String providerId) {
        patientRequest.setProvider_id(providerId.toLowerCase());
        return this;
    }

    public PatientRequestBuilder withCaseId(String caseId) {
        patientRequest.setCase_id(caseId.toLowerCase());
        return this;
    }

    public PatientRequestBuilder withTbRegistrationNumber(String tbRegistationNumber) {
        patientRequest.setTb_registration_number(tbRegistationNumber);
        return this;
    }

    public PatientRequestBuilder withPatientInfo(String patientId, String firstName, String lastName, Gender gender, PatientType patientType, String patientMobileNumber, String phi) {
        patientRequest.setPatientInfo(patientId, firstName, lastName, gender, patientType, patientMobileNumber, phi);
        return this;
    }

    public PatientRequestBuilder withFirstName(String firstName) {
        patientRequest.setFirst_name(firstName);
        return this;
    }

    public PatientRequestBuilder withPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        patientRequest.setPatientAddress(houseNumber, landmark, block, village, district, state);
        return this;
    }

    public PatientRequestBuilder withPatientAge(int age) {
        patientRequest.setAge(age);
        return this;
    }

    public PatientRequestBuilder withSmearTestResults(SputumTrackingInstance smearSputumTrackingInstance, LocalDate smearTestDate1, SmearTestResult smear_result_1, LocalDate smearTestDate2, SmearTestResult smearResult2) {
        patientRequest.addSmearTestResults(smearSputumTrackingInstance, smearTestDate1, smear_result_1, smearTestDate2, smearResult2, "labName", "labNumber");
        return this;
    }

    public PatientRequestBuilder withWeightStatistics(SputumTrackingInstance SputumTrackingInstance, Double weight, LocalDate measuringDate) {
        patientRequest.setWeightStatistics(SputumTrackingInstance, weight, measuringDate);
        return this;
    }

    public PatientRequestBuilder withPatientType(PatientType type) {
        patientRequest.setPatient_type(type);
        return this;
    }

    public PatientRequestBuilder withTbId(String tbId) {
        patientRequest.setTb_id(tbId.toLowerCase());
        return this;
    }

    public PatientRequestBuilder withLastModifiedDate(DateTime lastModifiedDate) {
        patientRequest.setDate_modified(lastModifiedDate);
        return this;
    }

    public PatientRequestBuilder withTreatmentCategory(TreatmentCategory treatmentCategory) {
        patientRequest.setTreatment_category(treatmentCategory);
        return this;
    }

    public PatientRequestBuilder withTreatmentOutcome(TreatmentOutcome treatmentOutcome) {
        patientRequest.setTreatment_outcome(treatmentOutcome);
        return this;
    }

    public PatientRequestBuilder withDateModified(DateTime dateModified) {
        patientRequest.setDateModified(dateModified);
        return this;
    }

    public PatientRequestBuilder withDiseaseClass(DiseaseClass diseaseClass) {
        patientRequest.setDisease_class(diseaseClass);
        return this;
    }

    public PatientRequestBuilder withTbRegistrationDate(DateTime dateTime) {
        patientRequest.setTbRegistrationDate(dateTime);
        return this;
    }

    public PatientRequestBuilder withAddressDistrict(String district) {
        patientRequest.setAddress(new Address("house number", "landmark", "block", "village", district, "state"));
        return this;
    }

    public PatientRequestBuilder withCloseTreatmentRemarks(String remarks) {
        patientRequest.setRemarks(remarks);
        return this;
    }
}
