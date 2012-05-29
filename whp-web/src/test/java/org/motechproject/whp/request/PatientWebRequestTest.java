package org.motechproject.whp.request;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;

import static junit.framework.Assert.assertEquals;

public class PatientWebRequestTest {

    PatientWebRequest patientWebRequest;

    @Before
    public void setUp(){
        patientWebRequest = new PatientWebRequest();
    }

    @Test
    public void shouldReturnTransferInScopeIfOpenNewTreatmentRequestComesInWithPatientTypeSetToTransferIn() {
        patientWebRequest.setTreatmentUpdateData("New", null);
        patientWebRequest.setPatient_type("TransferredIn");

        assertEquals(UpdateScope.transferIn, patientWebRequest.updateScope());
    }

    @Test
    public void shouldReturnOpenTreatmentScopeIfOpenNewTreatmentUpdateRequestComesInWIthPatientTypeNotSetToTransferIn() {
        patientWebRequest.setTreatmentUpdateData("New", null);
        patientWebRequest.setPatient_type("New");

        assertEquals(UpdateScope.openTreatment, patientWebRequest.updateScope());
    }

    @Test
    public void shouldReturnApplicableScopeIfTreatmentUpdateRequestComesIn() {
        patientWebRequest.setTreatmentUpdateData("Close", null);

        assertEquals(UpdateScope.closeTreatment, patientWebRequest.updateScope());
    }

    @Test
    public void shouldDoCaseInsensitiveMatchingForTreatmentUpdateScenario() {
        patientWebRequest.setTreatmentUpdateData("pause", null);
        patientWebRequest.setPatient_type("New");

        assertEquals(UpdateScope.pauseTreatment, patientWebRequest.updateScope());
    }

}
