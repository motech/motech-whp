package org.motechproject.whp.patient.contract;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.domain.PatientType;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.contract.TreatmentUpdateScenario.*;

public class PatientRequestTest {

    PatientRequest patientWebRequest;

    @Before
    public void setUp() {
        patientWebRequest = new PatientRequest();
    }

    @Test
    public void shouldReturnTransferInScope_WhenPatientTypeIsTransferredIn_AndPatientWasPreviouslyTransferredOut() {
        patientWebRequest.setTreatmentUpdate(New);
        patientWebRequest.setPatient_type(PatientType.TransferredIn);

        assertEquals(UpdateScope.transferIn, patientWebRequest.updateScope(true));
    }

    @Test
    public void shouldReturnOpenTreatmentScope_WhenPatientTypeIsTransferredIn_AndPatientWasPreviouslyNotTransferredOut() {
        patientWebRequest.setTreatmentUpdate(New);
        patientWebRequest.setPatient_type(PatientType.New);

        assertEquals(UpdateScope.openTreatment, patientWebRequest.updateScope(false));
    }

    @Test
    public void shouldReturnOpenTreatmentScopeIfOpenNewTreatmentUpdateRequestComesInWithPatientTypeNotSetToTransferIn_IrrespectiveOfPatientBeingTransferredOut() {
        patientWebRequest.setTreatmentUpdate(New);
        patientWebRequest.setPatient_type(PatientType.New);

        assertEquals(UpdateScope.openTreatment, patientWebRequest.updateScope(true));
    }

    @Test
    public void shouldReturnApplicableScopeIfTreatmentUpdateRequestComesIn() {
        patientWebRequest.setTreatmentUpdate(Close);

        assertEquals(UpdateScope.closeTreatment, patientWebRequest.updateScope(false));
    }

    @Test
    public void shouldDoCaseInsensitiveMatchingForTreatmentUpdateScenario() {
        patientWebRequest.setTreatmentUpdate(Pause);
        patientWebRequest.setPatient_type(PatientType.New);

        assertEquals(UpdateScope.pauseTreatment, patientWebRequest.updateScope(false));
    }
}
