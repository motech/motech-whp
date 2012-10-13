package org.motechproject.whp.adherence.criteria;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherence.criteria.TherapyStartCriteria.shouldStartOrRestartTreatment;

public class TherapyStartCriteriaTest {

    private static final String PATIENT_ID = "patientId";

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldBeTrueWhenPatientTypeIsNew() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

    @Test
    public void shouldBeTrueEvenWhenPatientIsMigrated() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).withMigrated(true).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

    @Test
    public void shouldBeTrueWhenPatientNotOnTreatment() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

    @Test
    public void shouldBeFalseWhenPatientOnTreatmentAndAdherenceIsBeingCapturedForLaterDate() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().forWeek(today().plusWeeks(2)).build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(today()).withType(PatientType.New).build();
        assertFalse(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

    @Test
    public void shouldBeTrueWhenPatientAlreadyOnTreatmentAndAdherenceIsBeingCapturedForSameWeek() {
        LocalDate monday = new LocalDate(2012, 4, 30);
        LocalDate sunday = new LocalDate(2012, 5, 6);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().forWeek(sunday).build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(monday).withType(PatientType.New).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

    @Test
    public void shouldBeTrueIfAnyOfTheDosesAreTaken() {
        new WeeklyAdherenceSummaryBuilder().withDosesTaken(3);
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        assertTrue(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

    @Test
    public void shouldBeTrueEvenIfNoneOfTheDosesAreTaken() {
        WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummaryBuilder().withDosesTaken(0).build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        assertTrue(shouldStartOrRestartTreatment(patient, adherenceSummary));
    }

}
