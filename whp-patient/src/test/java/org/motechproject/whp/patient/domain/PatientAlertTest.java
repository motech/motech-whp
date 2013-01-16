package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientAlertTest extends BaseUnitTest {

    @Test
    public void shouldUpdateCumulativeMissedDosesAlertStatusBasedOnCurrentTreatmentStartDate() {
        Therapy therapy = mock(Therapy.class);

        LocalDate currentTreatmentStartDate = new LocalDate(2013, 01, 01);
        when(therapy.getCurrentTreatmentStartDate()).thenReturn(currentTreatmentStartDate);
        int expectedCumulativeMissedDoses = 20;

        when(therapy.getCumulativeMissedDoses(currentTreatmentStartDate)).thenReturn(expectedCumulativeMissedDoses);

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();

        patient.updateCumulativeMissedDoseAlertStatus();

        assertEquals(expectedCumulativeMissedDoses, patient.getPatientAlerts().cumulativeMissedDoseAlert().getValue());
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
        when(therapy.getCumulativeMissedDoses(cumulativeMissedDoseAlert.getResetDate())).thenReturn(expectedCumulativeMissedDoses);

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        patient.setPatientAlerts(patientAlerts);

        patient.updateCumulativeMissedDoseAlertStatus();

        assertEquals(expectedCumulativeMissedDoses, patient.getPatientAlerts().cumulativeMissedDoseAlert().getValue());
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

        patient.updateTreatmentNotStartedAlertStatus();

        assertEquals(elapsedDaysSinceTBRegistration, patient.getPatientAlerts().treatmentNotStartedAlert().getValue());
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

        patient.updateTreatmentNotStartedAlertStatus();

        assertEquals(0, patient.getPatientAlerts().treatmentNotStartedAlert().getValue());
    }

    @Test
    public void shouldRaiseAdherenceMissingAlert() {
        Therapy therapy = mock(Therapy.class);
        mockCurrentDate(new LocalDate(2012, 01, 01));
        when(therapy.getOngoingDoseInterruption()).thenReturn(new DoseInterruption(DateUtil.today().minusWeeks(3)));

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        PatientAlerts patientAlerts = new PatientAlerts();
        patient.setPatientAlerts(patientAlerts);

        patient.updateAdherenceMissingAlert();

        assertEquals(3, patient.getPatientAlerts().adherenceMissingAlert().getValue());
    }

    @Test
    public void shouldRaiseAdherenceMissingAlert_whenThereIsNoOngoingDoseInterruption() {
        Therapy therapy = mock(Therapy.class);
        when(therapy.getOngoingDoseInterruption()).thenReturn(null);

        Patient patient = new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
        PatientAlerts patientAlerts = new PatientAlerts();
        patient.setPatientAlerts(patientAlerts);

        patient.updateAdherenceMissingAlert();

        assertEquals(0, patient.getPatientAlerts().adherenceMissingAlert().getValue());
    }
}