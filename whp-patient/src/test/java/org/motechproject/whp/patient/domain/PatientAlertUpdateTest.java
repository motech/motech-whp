package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.alerts.PatientAlert;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class PatientAlertUpdateTest extends BaseUnitTest {

    private final LocalDate someDate = today();

    @Test
    public void shouldUpdateCumulativeMissedDosesAlertStatusBasedOnCurrentTreatmentStartDate() {
        Therapy therapy = mock(Therapy.class);

        LocalDate currentTreatmentStartDate = new LocalDate(2013, 01, 01);
        when(therapy.getCurrentTreatmentStartDate()).thenReturn(currentTreatmentStartDate);
        int expectedCumulativeMissedDoses = 20;

        when(therapy.getCumulativeMissedDoses(eq(currentTreatmentStartDate), any(LocalDate.class))).thenReturn(expectedCumulativeMissedDoses);

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();

        assertEquals(expectedCumulativeMissedDoses, patient.cumulativeMissedDoses(someDate));
    }

    @Test
    public void shouldUpdateCumulativeMissedDosesAlertStatusBasedOnAlertResetDate() {
        Therapy therapy = mock(Therapy.class);

        PatientAlerts patientAlerts = new PatientAlerts();
        PatientAlert cumulativeMissedDoseAlert = patientAlerts.cumulativeMissedDoseAlert();
        cumulativeMissedDoseAlert.setResetDate(new LocalDate(2013, 01, 05));

        int expectedCumulativeMissedDoses = 20;
        LocalDate currentTreatmentStartDate = new LocalDate(2013, 01, 01);
        when(therapy.getCurrentTreatmentStartDate()).thenReturn(currentTreatmentStartDate);
        when(therapy.getCumulativeMissedDoses(eq(cumulativeMissedDoseAlert.getResetDate()), any(LocalDate.class))).thenReturn(expectedCumulativeMissedDoses);

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        patient.setPatientAlerts(patientAlerts);

        assertEquals(expectedCumulativeMissedDoses, patient.cumulativeMissedDoses(someDate));
    }

    @Test
    public void shouldRaiseTherapyNotStartedAlert_whenTherapyHasNotStarted() {
        Therapy therapy = mock(Therapy.class);
        LocalDate today = DateUtil.today();
        mockCurrentDate(today);

        int elapsedDaysSinceTBRegistration = 5;

        when(therapy.hasStarted()).thenReturn(false);
        when(therapy.getCurrentTreatmentStartDate()).thenReturn(today.minusDays(elapsedDaysSinceTBRegistration));

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        PatientAlerts patientAlerts = new PatientAlerts();
        patient.setPatientAlerts(patientAlerts);

        assertEquals(elapsedDaysSinceTBRegistration, patient.getDaysSinceTherapyHasNotStarted());
    }

    @Test
    public void shouldRaiseTherapyNotStartedAlert_whenTherapyHasStarted() {
        Therapy therapy = mock(Therapy.class);
        LocalDate today = DateUtil.today();
        mockCurrentDate(today);

        int elapsedDaysSinceTBRegistration = 5;

        when(therapy.hasStarted()).thenReturn(true);
        when(therapy.getCurrentTreatmentStartDate()).thenReturn(today.minusDays(elapsedDaysSinceTBRegistration));

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        PatientAlerts patientAlerts = new PatientAlerts();
        patient.setPatientAlerts(patientAlerts);

        assertEquals(0, patient.getDaysSinceTherapyHasNotStarted());
    }

    @Test
    public void shouldReturnWeeksElapsedSinceLastDose() {
        Therapy therapy = mock(Therapy.class);

        when(therapy.getOngoingDoseInterruption()).thenReturn(new DoseInterruption(new LocalDate(2013, 01, 18).minusWeeks(3)));

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();

        LocalDate currentAdherenceWeekEndDate = new LocalDate(2013, 01, 18).minusDays(5);
        assertEquals(2, patient.getWeeksElapsedSinceLastDose(currentAdherenceWeekEndDate));
        assertEquals(1, patient.getWeeksElapsedSinceLastDose(currentAdherenceWeekEndDate.minusWeeks(1)));
    }

    @Test
    public void shouldReturnZeroWhenTillDateIsOlderThanOngoingInterruptionStartDate() {
        Therapy therapy = mock(Therapy.class);
        when(therapy.getOngoingDoseInterruption()).thenReturn(new DoseInterruption(new LocalDate(2012, 12, 25)));

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();

        assertEquals(0, patient.getWeeksElapsedSinceLastDose(new LocalDate(2012, 12, 01)));
    }

    @Test
    public void shouldReturnWeeksElapsedSinceLastDose_whenThereIsNoOngoingDoseInterruption() {
        Therapy therapy = mock(Therapy.class);
        when(therapy.getOngoingDoseInterruption()).thenReturn(null);

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        PatientAlerts patientAlerts = new PatientAlerts();
        patient.setPatientAlerts(patientAlerts);

        assertEquals(0, patient.getWeeksElapsedSinceLastDose(currentAdherenceCaptureWeek().endDate()));
    }
}