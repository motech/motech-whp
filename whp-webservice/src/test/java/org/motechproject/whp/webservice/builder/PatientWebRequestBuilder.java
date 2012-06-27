package org.motechproject.whp.webservice.builder;

import org.joda.time.DateTime;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.refdata.domain.*;
import org.motechproject.whp.webservice.contract.TreatmentUpdateScenario;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static org.motechproject.whp.patient.builder.PatientBuilder.TB_ID;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_TB_ID;

public class PatientWebRequestBuilder {

    public static final String CASE_ID = "caseId";
    private PatientWebRequest patientWebRequest = new PatientWebRequest();

    public PatientWebRequestBuilder withDefaults() {

        patientWebRequest = new PatientWebRequest()
                .setPatientInfo(CASE_ID, "Foo", "Bar", Gender.M.name(), PatientType.Chronic.name(), "1234567890", "phi")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("PreTreatment", "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .setWeightStatistics(WeightInstance.PreTreatment.name(), "99.7")
                .setTreatmentData("01", TB_ID, "providerId", "P", "40", "registrationNumber");
        patientWebRequest.setDate_modified("10/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        return this;
    }

    public PatientWebRequestBuilder withSimpleUpdateFields() {
        patientWebRequest = new PatientWebRequest()
                .setPatientInfo(CASE_ID, null, null, null, null, "9087654321", null)
                .setPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .setSmearTestResults(SmearTestSampleInstance.EndTreatment.name(), "19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name())
                .setWeightStatistics(WeightInstance.EndTreatment.name(), "99.7")
                .setTreatmentData(null, TB_ID, null, null, "50", null);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        return this;
    }

    public PatientWebRequestBuilder withOnlyRequiredTreatmentUpdateFields() {
        patientWebRequest = new PatientWebRequest()
                .setPatientInfo(CASE_ID, null, null, null, null, null, null)
                .setTreatmentData("01", "tbtbtbtbtbt", "elevenDigit", null, null, null)
                .setTreatmentUpdateData(TreatmentUpdateScenario.Close.name(), "Cured");
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setPatient_type("TransferredIn");
        return this;
    }

    public PatientWebRequestBuilder withDefaultsForTransferIn() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData("New", null)
                .setTreatmentData("01", NEW_TB_ID, "newProviderId", "P", null, null)
                .setSmearTestResults(SmearTestSampleInstance.EndTreatment.name(), "19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name())
                .setWeightStatistics(WeightInstance.EndTreatment.name(), "99.7");
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setPatient_type("TransferredIn");
        return this;
    }

    public PatientWebRequestBuilder withDefaultsForPauseTreatment() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData(TreatmentUpdateScenario.Pause.name(), null)
                .setTreatmentData(null, TB_ID, "newProviderId", null, null, null);
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setReason("Chillaxing");
        return this;
    }

    public PatientWebRequestBuilder withDefaultsForRestartTreatment() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData(TreatmentUpdateScenario.Restart.name(), null)
                .setTreatmentData(null, TB_ID, "newProviderId", null, null, null);
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setReason("Coz he shouldn die");
        return this;
    }

    public PatientWebRequestBuilder withDefaultsForCloseTreatment() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData("Close", "Died");
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setTb_id(TB_ID);
        return this;
    }

    public PatientWebRequest build() {
        return patientWebRequest;
    }

    public PatientWebRequestBuilder withCaseId(String caseId) {
        patientWebRequest.setCase_id(caseId);
        return this;
    }

    public PatientWebRequestBuilder withTreatmentOutcome(String treatmentOutcome) {
        patientWebRequest.setTreatmentUpdateData(TreatmentUpdateScenario.Close.name(), treatmentOutcome);
        return this;
    }

    public PatientWebRequestBuilder withLastModifiedDate(String lastModifiedDate) {
        patientWebRequest.setDate_modified(lastModifiedDate);
        return this;
    }

    public PatientWebRequestBuilder withProviderId(String providerId) {
        patientWebRequest.setProvider_id(providerId);
        return this;
    }

    public PatientWebRequestBuilder withTreatmentCategory(String category) {
        patientWebRequest.setTreatment_category(category);
        return this;
    }

    public PatientWebRequestBuilder withSmearTestDate1(String smearTestDate1) {
        patientWebRequest.setSmear_test_date_1(smearTestDate1);
        return this;
    }

    public PatientWebRequestBuilder withSmearTestDate2(String smearTestDate2) {
        patientWebRequest.setSmear_test_date_2(smearTestDate2);
        return this;
    }

    public PatientWebRequestBuilder withMobileNumber(String mobileNumber) {
        patientWebRequest.setMobile_number(mobileNumber);
        return this;
    }

    public PatientWebRequestBuilder withSmearTestResult1(String smearTestResult) {
        patientWebRequest.setSmear_test_result_1(smearTestResult);
        return this;
    }

    public PatientWebRequestBuilder withTbId(String tbId) {
        patientWebRequest.setTb_id(tbId);
        return this;
    }

    public PatientWebRequestBuilder withGender(String gender) {
        patientWebRequest.setGender(gender);
        return this;
    }

    public PatientWebRequestBuilder withAge(String age) {
        patientWebRequest.setAge(age);
        return this;
    }

    public PatientWebRequestBuilder withWeight(String weight) {
        patientWebRequest.setWeight(weight);
        return this;
    }

    public PatientWebRequestBuilder withWeightStatistics(String weightInstance, String weight) {
        patientWebRequest.setWeightStatistics(weightInstance, weight);
        return this;
    }

    public PatientWebRequestBuilder withSmearTestResults(String smearSampleInstance, String testResultDate1, String testResult1, String testResultDate2, String testResult2) {
        patientWebRequest.setSmearTestResults(smearSampleInstance, testResultDate1, testResult1, testResultDate2, testResult2);
        return this;
    }

    public PatientWebRequestBuilder withAPIKey(String api_key) {
        patientWebRequest.setApi_key(api_key);
        return this;
    }

    public PatientWebRequestBuilder withDate_Modified(DateTime date_modified) {
        patientWebRequest.setDate_modified(date_modified.toString(WHPConstants.DATE_TIME_FORMAT));
        return this;
    }

    public PatientWebRequestBuilder withReasonForPause(String reasonForPause) {
        patientWebRequest.setReason(reasonForPause);
        return this;
    }

    public PatientWebRequestBuilder withReasonForRestart(String reasonForRestart) {
        patientWebRequest.setReason(reasonForRestart);
        return this;
    }

    public PatientWebRequestBuilder withPatientType(String patientType) {
        patientWebRequest.setPatient_type(patientType);
        return this;
    }

    public PatientWebRequestBuilder withDiseaseClass(DiseaseClass diseaseClass) {
        patientWebRequest.setDisease_class(diseaseClass.name());
        return this;
    }

    public PatientWebRequestBuilder withSmearTestInstance(String instance) {
        patientWebRequest.setSmear_sample_instance(instance);
        return this;
    }

    public PatientWebRequestBuilder withSmearTestResult2(String result) {
        patientWebRequest.setSmear_test_result_2(result);
        return this;
    }
}
