package org.motechproject.whp.patient.builder;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.domain.SmearTestResult;
import org.motechproject.whp.patient.domain.WeightInstance;

public class CreatePatientRequestBuilder {

    private CreatePatientRequest createPatientRequest = new CreatePatientRequest();

    public CreatePatientRequestBuilder withDefaults() {

        createPatientRequest = new CreatePatientRequest()
                .setPatientInfo("1234567890", "Foo", "Bar", Gender.M.getValue(), PatientType.PHSTransfer.name(), "1234567890", "phi")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("PreTreatment", DateUtil.newDate(2010, 5, 19), SmearTestResult.Positive.name(), DateUtil.newDate(2010, 5, 21), SmearTestResult.Positive.name())
                .setWeightStatistics(WeightInstance.PreTreatment.name(), 99.7, DateUtil.newDate(2010, 5, 19))
                .setTreatmentData("Category01", "12345678901", "123456", "P", 50, "registrationNumber", DateUtil.newDateTime(2010, 6, 21, 10, 0, 5));
        return this;
    }

    public CreatePatientRequest build() {
        return createPatientRequest;
    }

}
