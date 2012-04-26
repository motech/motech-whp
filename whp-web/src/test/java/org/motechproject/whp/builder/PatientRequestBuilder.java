package org.motechproject.whp.builder;

import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.domain.SmearTestResult;
import org.motechproject.whp.patient.domain.WeightInstance;
import org.motechproject.whp.request.PatientRequest;

public class PatientRequestBuilder {

    private PatientRequest patientRequest = new PatientRequest();

    public PatientRequestBuilder withDefaults() {

        patientRequest = new PatientRequest()
                .setPatientInfo("1234567890", "Foo", "Bar", Gender.M.getValue(), PatientType.PHSTransfer.name(), "1234567890", "phi")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("PreTreatment", "19/07/1888", SmearTestResult.Positive.name(), "21/09/1985", SmearTestResult.Positive.name())
                .setWeightStatistics(WeightInstance.PreTreatment.name(), "99.7")
                .setTreatmentData("01", "12345678901", "123456", "P", "200", "registrationNumber");
        patientRequest.setDate_modified("10/10/2010 10:10:10");
        return this;
    }

    public PatientRequest build() {
        return patientRequest;
    }

    public PatientRequestBuilder withCaseId(String caseId) {
        patientRequest.setCase_id(caseId);
        return this;
    }

    public PatientRequestBuilder withLastModifiedDate(String lastModifiedDate) {
        patientRequest.setDate_modified(lastModifiedDate);
        return this;
    }

    public PatientRequestBuilder withProviderId(String providerId) {
        patientRequest.setProvider_id(providerId);
        return this;
    }

    public PatientRequestBuilder withTreatmentCategory(String category) {
        patientRequest.setTreatment_category(category);
        return this;
    }

    public PatientRequestBuilder withSmearTestDate1(String smearTestDate1) {
        patientRequest.setSmear_test_date_1(smearTestDate1);
        return this;
    }

    public PatientRequestBuilder withSmearTestDate2(String smearTestDate2) {
        patientRequest.setSmear_test_date_2(smearTestDate2);
        return this;
    }

    public PatientRequestBuilder withMobileNumber(String mobileNumber) {
        patientRequest.setMobile_number(mobileNumber);
        return this;
    }

    public PatientRequestBuilder withSmearTestResult1(String smearTestResult) {
        patientRequest.setSmear_test_result_1(smearTestResult);
        return this;
    }

    public PatientRequestBuilder withTBId(String tbId) {
        patientRequest.setTb_id(tbId);
        return this;
    }

    public PatientRequestBuilder withGender(String gender) {
        patientRequest.setGender(gender);
        return this;
    }

    public PatientRequestBuilder withAge(String age) {
        patientRequest.setAge(age);
        return this;
    }
}
