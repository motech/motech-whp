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
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

public class AdherenceMissingAlertProcessorTest {

    private AdherenceMissingAlertProcessor adherenceMissingAlertProcessor;

    @Mock
    private TreatmentWeekInstance treatmentWeekInstance;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor(treatmentWeekInstance);
    }

    @Test
    public void shouldReturnMissingWeeksOfAdherenceForPatient() {
        Patient patient = mock(Patient.class);
        int weeksElapsedSinceLastDose = 5;
        when(patient.isCurrentTreatmentPaused()).thenReturn(false);
        when(patient.getWeeksElapsedSinceLastDose(any(LocalDate.class))).thenReturn(weeksElapsedSinceLastDose);

        assertEquals(weeksElapsedSinceLastDose, adherenceMissingAlertProcessor.process(patient));

        verify(patient).getWeeksElapsedSinceLastDose(any(LocalDate.class));
        verify(treatmentWeekInstance).previousAdherenceWeekEndDate();
    }

    @Test
    public void shouldReturnAlertValueAsZeroForPausedPatients() {
        Patient patient = mock(Patient.class);
        when(patient.isCurrentTreatmentPaused()).thenReturn(true);
        when(patient.getWeeksElapsedSinceLastDose(any(LocalDate.class))).thenReturn(5);

        AdherenceMissingAlertProcessor adherenceMissingAlertProcessor = this.adherenceMissingAlertProcessor;
        assertEquals(0, adherenceMissingAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAlertTypeAsAdherenceMissing() {
        assertEquals(AdherenceMissing, adherenceMissingAlertProcessor.alertType());
    }

    @After
    public void tearDown() {
         verifyNoMoreInteractions(treatmentWeekInstance);
    }
}
