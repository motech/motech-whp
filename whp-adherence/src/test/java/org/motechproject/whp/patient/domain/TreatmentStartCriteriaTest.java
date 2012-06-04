package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.refdata.domain.PatientType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.domain.TreatmentStartCriteria.shouldStartOrRestartTreatment;

public class TreatmentStartCriteriaTest {

    private static final String PATIENT_ID = "patientId";

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldBeTrueWhenPatientTypeIsNew() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueEvenWhenPatientIsMigrated() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).withMigrated(true).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueWhenPatientNotOnTreatment() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeFalseWhenPatientOnTreatmentAndAdherenceIsBeingCapturedForLaterDate() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(today().plusWeeks(2)).build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(today()).withType(PatientType.New).build();
        assertFalse(shouldStartOrRestartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueWhenPatientAlreadyOnTreatmentAndAdherenceIsBeingCapturedForSameWeek() {
        LocalDate monday = new LocalDate(2012, 4, 30);
        LocalDate sunday = new LocalDate(2012, 5, 6);
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(sunday).build(); //not demonic any more :(

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(monday).withType(PatientType.New).build();
        assertTrue(shouldStartOrRestartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueIfAnyOfTheDosesAreTaken() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        assertTrue(shouldStartOrRestartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueEvenIfNoneOfTheDosesAreTaken() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().zeroDosesTaken().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        assertTrue(shouldStartOrRestartTreatment(patient, adherence));
    }

}
