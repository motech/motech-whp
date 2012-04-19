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
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state", "postal code")
                .setSmearTestResults("PreTreatment", "19/07/1888", "result1", "PreTreatment", "21/09/1985", "result2")
                .setWeightStatistics(WeightInstance.PreTreatment.name(), "99.7")
                .setRegistrationDetails("registrationNumber", "20/01/1982")
                .setTreatmentData("01", "providerId01seq1", "providerId", "P", "200");
        patientRequest.setDate_modified(DateUtil.now().toString("dd/MM/YYYY HH:mm:ss"));

        return this;
    }

    public PatientRequest build() {
        return patientRequest;
    }

    public PatientRequestBuilder withRegistrationDate(String registrationDate) {
        patientRequest.setRegistration_date(registrationDate);
        return this;
    }

    public PatientRequestBuilder withLastModifiedDate(String lastModifiedDate) {
        patientRequest.setDate_modified(lastModifiedDate);
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
}
