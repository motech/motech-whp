package org.motechproject.whp.refdata.seed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.reporting.PatientReportingService;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.seed.version5.PatientReportSeed;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientReportSeedTest {

    PatientReportSeed patientReportSeed;

    @Mock
    PatientService patientService;

    @Mock
    PatientReportingService patientReportingService;

    @Before
    public void setUp() {
        initMocks(this);
        patientReportSeed = new PatientReportSeed(patientService, patientReportingService);
    }

    @Test
    public void shouldMigrateExistingPatients() {
        Patient patient1 = mock(Patient.class);
        Patient patient2 = mock(Patient.class);
        List patientList = asList(patient1, patient2);

        when(patientService.getAll()).thenReturn(patientList);

        patientReportSeed.migratePatients();

        verify(patientService).getAll();
        verify(patientReportingService).reportPatient(patient1);
        verify(patientReportingService).reportPatient(patient2);
    }
}
