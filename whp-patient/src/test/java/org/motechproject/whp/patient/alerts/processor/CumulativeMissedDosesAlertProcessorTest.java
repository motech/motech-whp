package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;

public class CumulativeMissedDosesAlertProcessorTest {

    CumulativeMissedDosesAlertProcessor cumulativeMissedDosesAlertProcessor;

    @Mock
    private TreatmentWeekInstance treatmentWeekInstance;

    @Before
    public void setUp() {
        initMocks(this);
        cumulativeMissedDosesAlertProcessor  = new CumulativeMissedDosesAlertProcessor(treatmentWeekInstance);
    }

    @Test
    public void shouldReturnCumulativeMissedDosesForPatient() {
        Patient patient = mock(Patient.class);
        int cumulativeMissedDoses = 5;
        when(patient.isCurrentTreatmentPaused()).thenReturn(false);

        LocalDate previousAdherenceWeekEndDate = new LocalDate(2013, 01, 01);
        when(treatmentWeekInstance.previousAdherenceWeekEndDate()).thenReturn(previousAdherenceWeekEndDate);
        when(patient.cumulativeMissedDoses(any(LocalDate.class))).thenReturn(cumulativeMissedDoses);

        assertEquals(cumulativeMissedDoses, cumulativeMissedDosesAlertProcessor.process(patient));

        verify(patient).cumulativeMissedDoses(previousAdherenceWeekEndDate);
        verify(treatmentWeekInstance).previousAdherenceWeekEndDate();
    }

    @Test
    public void shouldReturnAlertValueAsZeroForPausedPatients() {
        Patient patient = mock(Patient.class);
        when(patient.isCurrentTreatmentPaused()).thenReturn(true);
        when(patient.cumulativeMissedDoses(any(LocalDate.class))).thenReturn(5);

        assertEquals(0, cumulativeMissedDosesAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAlertTypeAsCumulativeMissedDoses() {
        assertEquals(CumulativeMissedDoses, cumulativeMissedDosesAlertProcessor.alertType());
    }

    @After
    public void tearDown() {
        verifyZeroInteractions(treatmentWeekInstance);
    }
}
