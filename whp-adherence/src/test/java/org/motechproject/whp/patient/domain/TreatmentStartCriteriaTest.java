package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.domain.TreatmentStartCriteria.shouldStartTreatment;

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
        assertTrue(shouldStartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueWhenPatientNotOnTreatment() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        assertTrue(shouldStartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeFalseWhenPatientTypeIsNotNew() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.PHSTransfer).build();
        assertFalse(shouldStartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeFalseWhenPatientOnTreatmentAndAdherenceIsBeingCapturedForLaterDate() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(today().plusWeeks(2)).build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(today()).withType(PatientType.New).build();
        assertFalse(shouldStartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueWhenPatientOnTreatmentButAdherenceIsBeingCapturedForEarlierDate() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogsForWeek(new LocalDate(1983, 1, 30)).build(); //demonic

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(today()).withType(PatientType.New).build();
        assertTrue(shouldStartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeTrueIfAnyOfTheDosesAreTaken() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        assertTrue(shouldStartTreatment(patient, adherence));
    }

    @Test
    public void shouldBeFalseIfNoneOfTheDosesAreTaken() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().zeroDosesTaken().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        assertFalse(shouldStartTreatment(patient, adherence));
    }

}
