package org.motechproject.whp.patient.builder;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.*;

public class CreatePatientRequestBuilder {

    private CreatePatientRequest createPatientRequest = new CreatePatientRequest();

    public CreatePatientRequest build() {
        return createPatientRequest;
    }

    public CreatePatientRequestBuilder withDefaults() {

        createPatientRequest = new CreatePatientRequest()
                .setPatientInfo("1234567890", "Foo", "Bar", Gender.M, PatientType.PHSTransfer, "1234567890", "phi")
                .setTreatmentData(TreatmentCategory.Category01, "12345678901", "123456", DiseaseClass.P, 50, "registrationNumber", DateUtil.newDateTime(2010, 6, 21, 10, 0, 5))
                .setSmearTestResults(SmearTestSampleInstance.PreTreatment, DateUtil.newDate(2010, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2010, 5, 21), SmearTestResult.Positive)
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setWeightStatistics(WeightInstance.PreTreatment, 99.7, DateUtil.newDate(2010, 5, 19));
        return this;
    }


    public CreatePatientRequestBuilder withSimpleUpdateFields() {
        createPatientRequest = new CreatePatientRequest()
                .setPatientInfo("1234567890", null, null, null, null, "9087654321", null)
                .setPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .setSmearTestResults(SmearTestSampleInstance.EndTreatment, DateUtil.newDate(2010, 7, 19), SmearTestResult.Negative, DateUtil.newDate(2010, 9, 20), SmearTestResult.Negative)
                .setWeightStatistics(WeightInstance.EndTreatment, 99.7, DateUtil.newDate(2010, 9, 20))
                .setTreatmentData(null, null, null, null, 50, "newRegistrationNumber", DateUtil.newDateTime(2010, 9, 20, 10, 10, 0));
        return this;
    }

    public CreatePatientRequestBuilder withCaseId(String caseId) {
        createPatientRequest.setCaseId(caseId);
        return this;
    }

}
