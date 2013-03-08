package org.motechproject.whp.refdata.seed;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.seed.version5.PatientReportSeed;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientReportSeedTest {

    PatientReportSeed patientReportSeed;

    @Mock
    PatientService patientService;

    @Before
    public void setUp() {
        initMocks(this);
        patientReportSeed = new PatientReportSeed(patientService);
    }

    @Test
    public void shouldMigrateExistingPatients() {
        Patient patient1 = mock(Patient.class);
        Patient patient2 = mock(Patient.class);

        List patientList1 = asList(patient1);
        List patientList2 = asList(patient2);
        List emptyList = new ArrayList();

        when(patientService.getAll(anyInt(), anyInt())).thenReturn(patientList1).thenReturn(patientList2).thenReturn(emptyList);

        patientReportSeed.migrateAllPatients();

        verify(patientService).getAll(eq(1), anyInt());
        verify(patientService).getAll(eq(2), anyInt());
        verify(patientService).getAll(eq(3), anyInt());
        verify(patientService).update(patient1);
        verify(patientService).update(patient2);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(patientService);
    }
}
