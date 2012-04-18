package org.motechproject.whp.builder;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.domain.WeightInstance;
import org.motechproject.whp.request.PatientRequest;

public class PatientRequestBuilder {

    private PatientRequest patientRequest= new PatientRequest();

    public PatientRequestBuilder withDefaults() {
        patientRequest = new PatientRequest()
                .setPatientInfo("caseId", "Foo", "Bar", "M", PatientType.PHSTransfer.name(), "12345667890")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("Pre-treatment1", DateUtil.today().minusDays(10).toString(), "result1", "Pre-treatment2", DateUtil.today().minusDays(5).toString(), "result2")
                .setWeightStatistics(WeightInstance.preTreatment.name(), "99.7")
                .setRegistrationDetails("registrationNumber", DateUtil.today().toString())
                .setTreatmentData("01", "providerId01seq1", "providerId", "P")  ;
        return this;
    }

    public PatientRequest build() {
        return patientRequest;
    }
}
