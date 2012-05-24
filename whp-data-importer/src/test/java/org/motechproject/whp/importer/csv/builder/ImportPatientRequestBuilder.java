package org.motechproject.whp.importer.csv.builder;

import org.joda.time.DateTime;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.refdata.domain.*;

public class ImportPatientRequestBuilder {

    private ImportPatientRequest importPatientRequest = new ImportPatientRequest();

    public ImportPatientRequestBuilder withDefaults() {

        importPatientRequest = new ImportPatientRequest()
                .setPatientInfo("1234567890", "Foo", "Bar", Gender.M.name(), PatientType.PHCTransfer.name(), "1234567890", "phi")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("PreTreatment", "19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .setWeightStatistics("22/09/2000",WeightInstance.PreTreatment.name(), "99.7")
                .setTreatmentData("01", "12345678901", "123456", "P", "40", "registrationNumber");
        importPatientRequest.setDate_modified("10/10/2010 10:10:10");
        return this;
    }

    public ImportPatientRequestBuilder withSimpleUpdateFields() {
        importPatientRequest = new ImportPatientRequest()
                .setPatientInfo("1234567890", null, null, null, null, "9087654321", null)
                .setPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .setSmearTestResults(SmearTestSampleInstance.EndTreatment.name(), "19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name())
                .setWeightStatistics("20/07/2010",WeightInstance.EndTreatment.name(), "99.7")
                .setTreatmentData(null, "elevenDigit", null, null, "50", null);
        importPatientRequest.setDate_modified("15/10/2010 10:10:10");
        return this;
    }

    public ImportPatientRequestBuilder withOnlyRequiredTreatmentUpdateFields() {
        importPatientRequest = new ImportPatientRequest()
                .setPatientInfo("1234567890", null, null, null, null, null, null)
                .setTreatmentData("01", "tbtbtbtbtbt", "providerId", null, null, null);
        importPatientRequest.setDate_modified("15/10/2010 10:10:10");
        return this;
    }

    public ImportPatientRequestBuilder withDefaultsForTransferIn() {
        importPatientRequest = new ImportPatientRequest()
                .setTreatmentData(null, "elevenDigit", "newProviderId", null, null, null);
        importPatientRequest.setCase_id("12345");
        importPatientRequest.setDate_modified("15/10/2010 10:10:10");
        return this;
    }

    public ImportPatientRequestBuilder withDefaultsForPauseTreatment() {
        importPatientRequest = new ImportPatientRequest()
                .setTreatmentData(null, "elevenDigit", "newProviderId", null, null, null);
        importPatientRequest.setCase_id("12345");
        importPatientRequest.setDate_modified("15/10/2010 10:10:10");
        return this;
    }

    public ImportPatientRequestBuilder withDefaultsForRestartTreatment() {
        importPatientRequest = new ImportPatientRequest()
                .setTreatmentData(null, "elevenDigit", "newProviderId", null, null, null);
        importPatientRequest.setCase_id("12345");
        importPatientRequest.setDate_modified("15/10/2010 10:10:10");
        return this;
    }

    public ImportPatientRequest build() {
        return importPatientRequest;
    }

    public ImportPatientRequestBuilder withCaseId(String caseId) {
        importPatientRequest.setCase_id(caseId);
        return this;
    }


    public ImportPatientRequestBuilder withLastModifiedDate(String lastModifiedDate) {
        importPatientRequest.setDate_modified(lastModifiedDate);
        return this;
    }

    public ImportPatientRequestBuilder withProviderId(String providerId) {
        importPatientRequest.setProvider_id(providerId);
        return this;
    }

    public ImportPatientRequestBuilder withTreatmentCategory(String category) {
        importPatientRequest.setTreatment_category(category);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestDate1(String smearTestDate1) {
        importPatientRequest.setSmear_test_date_1(smearTestDate1);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestDate2(String smearTestDate2) {
        importPatientRequest.setSmear_test_date_2(smearTestDate2);
        return this;
    }

    public ImportPatientRequestBuilder withMobileNumber(String mobileNumber) {
        importPatientRequest.setMobile_number(mobileNumber);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestResult1(String smearTestResult) {
        importPatientRequest.setSmear_test_result_1(smearTestResult);
        return this;
    }

    public ImportPatientRequestBuilder withTBId(String tbId) {
        importPatientRequest.setTb_id(tbId);
        return this;
    }

    public ImportPatientRequestBuilder withGender(String gender) {
        importPatientRequest.setGender(gender);
        return this;
    }

    public ImportPatientRequestBuilder withAge(String age) {
        importPatientRequest.setAge(age);
        return this;
    }

    public ImportPatientRequestBuilder withWeight(String weight) {
        importPatientRequest.setWeight(weight);
        return this;
    }
    public ImportPatientRequestBuilder withWeightMeasuredDate(String date) {
        importPatientRequest.setWeight_date(date);
        return this;
    }

    public ImportPatientRequestBuilder withWeightStatistics(String weightDate,String weightInstance, String weight) {
        importPatientRequest.setWeightStatistics(weightDate,weightInstance, weight);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestResults(String smearSampleInstance, String testResultDate1, String testResult1, String testResultDate2, String testResult2) {
        importPatientRequest.setSmearTestResults(smearSampleInstance, testResultDate1, testResult1, testResultDate2, testResult2);
        return this;
    }

    public ImportPatientRequestBuilder withDate_Modified(DateTime date_modified) {
        importPatientRequest.setDate_modified(date_modified.toString("dd/MM/YYYY HH:mm:ss"));
        return this;
    }

    public ImportPatientRequestBuilder withPatientType(String patientType) {
        importPatientRequest.setPatient_type(patientType);
        return this;
    }
}
