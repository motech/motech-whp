package org.motechproject.whp.refdata.seed;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.patient.alerts.scheduler.PatientAlertScheduler;
import org.motechproject.whp.patient.service.PatientService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientAlertScheduleSeedTest {

    @Mock
    PatientService patientService;

    @Mock
    PatientAlertScheduler patientAlertScheduler;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldCreatePatientAlertSchedulesForActivePatients() {
        PatientAlertScheduleSeed patientAlertScheduleSeed = new PatientAlertScheduleSeed(patientAlertScheduler, patientService);

        List<String> expectedPatientIds = asList("patient1", "patient2");
        when(patientService.getAllActivePatientIds()).thenReturn(expectedPatientIds);

        patientAlertScheduleSeed.scheduleActivePatients();

        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(patientAlertScheduler, times(2)).scheduleJob(patientIdCaptor.capture());
        List<String> scheduledPatientIds = patientIdCaptor.getAllValues();

        assertEquals(expectedPatientIds, scheduledPatientIds);
        verify(patientService).getAllActivePatientIds();
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(patientService);
        verifyNoMoreInteractions(patientAlertScheduler);
    }
}
