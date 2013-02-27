package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;

public class CumulativeMissedDosesAlertProcessorTest {

    CumulativeMissedDosesAlertProcessor cumulativeMissedDosesAlertProcessor;

    @Mock
    private CumulativeMissedDosesCalculator cumulativeMissedDosesCalculator;

    @Before
    public void setUp() {
        initMocks(this);
        cumulativeMissedDosesAlertProcessor  = new CumulativeMissedDosesAlertProcessor(cumulativeMissedDosesCalculator);
    }

    @Test
    public void shouldReturnCumulativeMissedDosesForPatient() {
        Patient patient = mock(Patient.class);
        int cumulativeMissedDoses = 5;
        when(patient.isCurrentTreatmentPaused()).thenReturn(false);

        when(cumulativeMissedDosesCalculator.getCumulativeMissedDoses(patient)).thenReturn(cumulativeMissedDoses);

        assertEquals((double) cumulativeMissedDoses, cumulativeMissedDosesAlertProcessor.process(patient));

        verify(cumulativeMissedDosesCalculator).getCumulativeMissedDoses(patient);
    }

    @Test
    public void shouldReturnAlertValueAsZeroForPausedPatients() {
        Patient patient = mock(Patient.class);
        when(patient.isCurrentTreatmentPaused()).thenReturn(true);
        when(patient.cumulativeMissedDoses(any(LocalDate.class))).thenReturn(5);

        assertEquals((double) 0, cumulativeMissedDosesAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAlertTypeAsCumulativeMissedDoses() {
        assertEquals(CumulativeMissedDoses, cumulativeMissedDosesAlertProcessor.alertType());
    }
}
