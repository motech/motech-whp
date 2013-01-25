package org.motechproject.whp.patient.alerts.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Patient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientAlertServiceTest {

    @Mock
    AllAlertProcessors allAlertProcessors;

    PatientAlertService patientAlertService;

    @Before
    public void setUp() {
        initMocks(this);
        patientAlertService = new PatientAlertService(allAlertProcessors);
    }

    @Test
    public void shouldFetchPatientAndUpdateDoseInterruptions() {
        Patient patient = mock(Patient.class);
        patientAlertService.updatePatientAlerts(patient);

        verify(allAlertProcessors).process(patient);
    }

}
