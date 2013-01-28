package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class PatientAlertUpdateTestPart extends TreatmentUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldUpdatePatientAlertsForGivenPatientId() {
        Patient patient = mock(Patient.class);
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);

        Set<LocalDate> adherenceDates = new HashSet<>();
        when(whpAdherenceService.getAdherenceDates(patient)).thenReturn(adherenceDates);

        treatmentUpdateOrchestrator.processAlertsBasedOnConfiguration(PATIENT_ID);

        verify(patient).updateDoseInterruptions(adherenceDates);
        verify(patientService).updateBasedOnAlertConfiguration(patient);
    }

}
