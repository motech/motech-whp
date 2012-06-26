package org.motechproject.whp.webservice.request;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import static junit.framework.Assert.assertEquals;

public class PatientWebRequestTest {

    PatientWebRequest patientWebRequest;

    @Before
    public void setUp(){
        patientWebRequest = new PatientWebRequest();
    }

    @Test
    public void shouldReturnTransferInScope_WhenPatientTypeIsTransferredIn_AndPatientWasPreviouslyTransferredOut() {
        patientWebRequest.setTreatmentUpdateData("New", null);
        patientWebRequest.setPatient_type("TransferredIn");

        assertEquals(UpdateScope.transferIn, patientWebRequest.updateScope(true));
    }

    @Test
    public void shouldReturnOpenTreatmentScope_WhenPatientTypeIsTransferredIn_AndPatientWasPreviouslyNotTransferredOut() {
        patientWebRequest.setTreatmentUpdateData("New", null);
        patientWebRequest.setPatient_type("TransferredIn");

        assertEquals(UpdateScope.openTreatment, patientWebRequest.updateScope(false));
    }

    @Test
    public void shouldReturnOpenTreatmentScopeIfOpenNewTreatmentUpdateRequestComesInWithPatientTypeNotSetToTransferIn_IrrespectiveOfPatientBeingTransferredOut() {
        patientWebRequest.setTreatmentUpdateData("New", null);
        patientWebRequest.setPatient_type("New");

        assertEquals(UpdateScope.openTreatment, patientWebRequest.updateScope(true));
    }

    @Test
    public void shouldReturnApplicableScopeIfTreatmentUpdateRequestComesIn() {
        patientWebRequest.setTreatmentUpdateData("Close", null);

        assertEquals(UpdateScope.closeTreatment, patientWebRequest.updateScope(false));
    }

    @Test
    public void shouldDoCaseInsensitiveMatchingForTreatmentUpdateScenario() {
        patientWebRequest.setTreatmentUpdateData("pause", null);
        patientWebRequest.setPatient_type("New");

        assertEquals(UpdateScope.pauseTreatment, patientWebRequest.updateScope(false));
    }

}
