package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.patient.domain.Patient;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.motechproject.scheduler.MotechSchedulerService.JOB_ID_KEY;

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

        MotechEvent motechEvent = new MotechEvent(EventKeys.PATIENT_ALERTS_UPDATE);
        motechEvent.getParameters().put(JOB_ID_KEY, PATIENT_ID);
        motechEvent.getParameters().put(EventKeys.PATIENT_ALERTS_UPDATE_PATIENT_ID_PARAM, PATIENT_ID);

        treatmentUpdateOrchestrator.processAlertsBasedOnConfiguration(motechEvent);

        verify(patient).updateDoseInterruptions(adherenceDates);
        verify(patientService).updateBasedOnAlertConfiguration(patient);
    }

}
