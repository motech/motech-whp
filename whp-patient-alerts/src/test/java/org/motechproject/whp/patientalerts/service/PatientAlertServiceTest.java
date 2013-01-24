package org.motechproject.whp.patientalerts.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientAlertServiceTest {

    @Mock
    private PatientService patientService;
    @Mock
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    @Mock
    AllAlertProcessors allAlertProcessors;

    PatientAlertService patientAlertService;

    @Before
    public void setUp() {
        initMocks(this);
        patientAlertService = new PatientAlertService(patientService, treatmentUpdateOrchestrator, allAlertProcessors);
    }

    @Test
    public void shouldFetchPatientAndUpdateDoseInterruptions() {
        Patient patient = mock(Patient.class);
        String patientId = "patient-id";
        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        patientAlertService.updatePatientAlerts(patientId);

        verify(patientService).findByPatientId(patientId);
        verify(treatmentUpdateOrchestrator).updateDoseInterruptions(patient);
        verify(allAlertProcessors).process(patient);
        verify(patientService).update(patient);
    }

}
