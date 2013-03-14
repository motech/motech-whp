package org.motechproject.whp.webservice.builder;

import org.joda.time.DateTime;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.domain.DiseaseClass;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.webservice.contract.TreatmentUpdateScenario;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.motechproject.whp.patient.builder.PatientBuilder.TB_ID;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_TB_ID;

public class PatientWebRequestBuilder {

    public static final String CASE_ID = "caseId";
    private PatientWebRequest patientWebRequest = new PatientWebRequest();

    public PatientWebRequestBuilder withDefaults() {

        patientWebRequest = new PatientWebRequest()
                .setPatientInfo(CASE_ID, "Foo", "Bar", Gender.M.name(), PatientType.Chronic.name(), "1234567890", "phi")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("PreTreatment", "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name(), "labName", "labNumber")
                .setWeightStatistics(SputumTrackingInstance.PreTreatment.name(), "99.7")
                .setTreatmentData("01", TB_ID, "providerId", "P", "40", "registrationNumber")
                .setTreatmentDetails("hiv_status", "6", "provider_name", "dot_centre", "provider_type", "cmf_doctor", "contact_person_name", "contact_person_phone_number" );
        patientWebRequest.setDate_modified("10/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("10/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setDate_of_birth("10/10/1981");
        setDefaultTreatmentDetails();
        return this;
    }

    public PatientWebRequestBuilder withSimpleUpdateFields() {
        patientWebRequest = new PatientWebRequest()
                .setPatientInfo(CASE_ID, null, null, null, null, "9087654321", null)
                .setPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .setSmearTestResults(SputumTrackingInstance.EndTreatment.name(), "19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name(), "labName", "labNumber")
                .setWeightStatistics(SputumTrackingInstance.EndTreatment.name(), "99.7")
                .setTreatmentData(null, TB_ID, null, null, "50", null);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("15/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        return this;
    }

    public PatientWebRequestBuilder withOnlyRequiredTreatmentUpdateFields() {
        patientWebRequest = new PatientWebRequest()
                .setPatientInfo(CASE_ID, null, null, null, null, null, null)
                .setTreatmentData("01", "tbtbtbtbtbt", "elevenDigit", null, null, null)
                .setTreatmentUpdateData(TreatmentUpdateScenario.Close.name(), "Cured");
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("15/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setPatient_type("TransferredIn");


        setDefaultTreatmentDetails();
        return this;
    }

    private void setDefaultTreatmentDetails() {
        patientWebRequest.setDistrict_with_code("district_with_code");
        patientWebRequest.setTb_unit_with_code("tb_with_code");
        patientWebRequest.setEp_site("ep_site");
        patientWebRequest.setOther_investigations("others");
        patientWebRequest.setPrevious_treatment_history("treatment_history");
        patientWebRequest.setHiv_status("hiv_status");
        patientWebRequest.setHiv_test_date("15/10/2010");
        patientWebRequest.setMembers_below_six_years("6");
        patientWebRequest.setPhc_referred("phc_referred");
        patientWebRequest.setProvider_name("provider_name");
        patientWebRequest.setDot_centre("dot_center");
        patientWebRequest.setProvider_type("provider_type");
        patientWebRequest.setCmf_doctor("cmf doctor");
        patientWebRequest.setContact_person_name("person name");
        patientWebRequest.setContact_person_phone_number("phone number");
        patientWebRequest.setXpert_test_result("xpert test result");
        patientWebRequest.setXpert_device_number("xpert device number");
        patientWebRequest.setXpert_test_date("15/10/2010");
        patientWebRequest.setRif_resistance_result("rif resistance result");
    }

    public PatientWebRequestBuilder withDefaultsForTransferIn() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData("New", null)
                .setTreatmentData("01", NEW_TB_ID, "newProviderId", "P", null, null)
                .setSmearTestResults(SputumTrackingInstance.EndTreatment.name(), "19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name(), "labName", "labNumber")
                .setWeightStatistics(SputumTrackingInstance.EndTreatment.name(), "99.7");
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("15/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setPatient_type("TransferredIn");

        setDefaultTreatmentDetails();
        return withDefaultAddress().withDefaultMobileNumber();
    }

    public PatientWebRequestBuilder withDefaultsForNewTreatment() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData("New", null)
                .setTreatmentData("01", NEW_TB_ID, "newProviderId", "P", null, null)
                .setSmearTestResults(SputumTrackingInstance.EndTreatment.name(), "19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name(), "labName", "labNumber")
                .setWeightStatistics(SputumTrackingInstance.EndTreatment.name(), "99.7");
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("15/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setPatient_type("New");
        setDefaultTreatmentDetails();

        return withDefaultAddress().withDefaultMobileNumber();
    }

    public PatientWebRequestBuilder withDefaultsForPauseTreatment() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData(TreatmentUpdateScenario.Pause.name(), null)
                .setTreatmentData(null, TB_ID, "newProviderId", null, null, null);
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("15/10/2010");
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
        patientWebRequest.setTb_registration_date("15/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setReason("Coz he shouldn die");
        return this;
    }

    public PatientWebRequestBuilder withDefaultsForCloseTreatment() {
        patientWebRequest = new PatientWebRequest()
                .setTreatmentUpdateData("Close", "Died");
        patientWebRequest.setCase_id(CASE_ID);
        patientWebRequest.setDate_modified("15/10/2010 10:10:10");
        patientWebRequest.setTb_registration_date("15/10/2010");
        patientWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        patientWebRequest.setTb_id(TB_ID);
        patientWebRequest.setRemarks("testRemarks");
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

    public PatientWebRequestBuilder withWeightStatistics(String SputumTrackingInstance, String weight) {
        patientWebRequest.setWeightStatistics(SputumTrackingInstance, weight);
        return this;
    }

    public PatientWebRequestBuilder withSmearTestResults(String smearSputumTrackingInstance, String testResultDate1, String testResult1, String testResultDate2, String testResult2, String labName, String labNumber) {
        patientWebRequest.setSmearTestResults(smearSputumTrackingInstance, testResultDate1, testResult1, testResultDate2, testResult2, labName, labNumber);
        return this;
    }

    public PatientWebRequestBuilder withAPIKey(String api_key) {
        patientWebRequest.setApi_key(api_key);
        return this;
    }

    public PatientWebRequestBuilder withDate_Modified(DateTime date_modified) {
        patientWebRequest.setDate_modified(date_modified.toString(DATE_TIME_FORMAT));
        return this;
    }

    public PatientWebRequestBuilder withTbRegistartionDate(DateTime tbRegistartionDate) {
        patientWebRequest.setTb_registration_date(tbRegistartionDate.toString(DATE_FORMAT));
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

    public PatientWebRequestBuilder withLabName(String labName) {
        patientWebRequest.setLab_name(labName);
        return this;
    }

    public PatientWebRequestBuilder withLabNumber(String labNumber) {
        patientWebRequest.setLab_number(labNumber);
        return this;
    }

    public PatientWebRequestBuilder withPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        patientWebRequest.setPatientAddress(houseNumber, landmark, block, village, district, state);
        return this;
    }

    public PatientWebRequestBuilder withRemarks(String remarks) {
        patientWebRequest.setRemarks(remarks);
        return this;
    }

    public PatientWebRequestBuilder withTreatmentStatus(String status) {
        patientWebRequest.setTreatment_update(status);
        return this;
    }

    public PatientWebRequestBuilder withTbRegistrationNumber(String tbRegistrationNumber) {
        patientWebRequest.setTb_registration_number(tbRegistrationNumber);
        return this;
    }

    public PatientWebRequestBuilder withPatientDistrict(String district) {
        patientWebRequest.setAddress_district(district);
        return this;
    }

    public PatientWebRequestBuilder withDefaultAddress() {
        patientWebRequest.setPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state");
        return this;
    }

    public PatientWebRequestBuilder withDefaultMobileNumber() {
        patientWebRequest.setMobile_number("1234567890");
        return this;
    }

    public PatientWebRequestBuilder withMobileNumber(String mobileNumber) {
        patientWebRequest.setMobile_number(mobileNumber);
        return this;
    }

    public PatientWebRequestBuilder withOptionalTreatmentDetailsAsNull() {
        patientWebRequest.setDistrict_with_code(null);
        patientWebRequest.setTb_unit_with_code(null);
        patientWebRequest.setEp_site(null);
        patientWebRequest.setOther_investigations(null);
        patientWebRequest.setPrevious_treatment_history(null);
        patientWebRequest.setHiv_test_date(null);
        patientWebRequest.setPhc_referred(null);
        patientWebRequest.setXpert_test_result(null);
        patientWebRequest.setXpert_device_number(null);
        patientWebRequest.setXpert_test_date(null);
        patientWebRequest.setRif_resistance_result(null);
        return this;
    }

    public PatientWebRequestBuilder withMandatoryTreatmentDetailsAsNull() {
        patientWebRequest.setHiv_status(null);
        patientWebRequest.setMembers_below_six_years(null);
        patientWebRequest.setProvider_name(null);
        patientWebRequest.setDot_centre(null);
        patientWebRequest.setProvider_type(null);
        patientWebRequest.setCmf_doctor(null);
        patientWebRequest.setContact_person_name(null);
        patientWebRequest.setContact_person_phone_number(null);
        return this;
    }

    public PatientWebRequestBuilder withDateOfBirth(String dateOfBirth) {
        patientWebRequest.setDate_of_birth(dateOfBirth);
        return this;
    }
}
