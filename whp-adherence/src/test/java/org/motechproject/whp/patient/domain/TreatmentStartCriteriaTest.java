package org.motechproject.whp.patient.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.builder.AdherenceBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TreatmentStartCriteriaTest {

    private static final String PATIENT_ID = "patientId";

    @Mock
    private AllPatients allPatients;

    private TreatmentStartCriteria treatmentStartCriteria;

    @Before
    public void setup() {
        initMocks(this);
        treatmentStartCriteria = new TreatmentStartCriteria(allPatients);
    }

    @Test
    public void shouldBeTrueWhenPatientTypeIsNew() {
        Adherence adherence = new AdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        assertTrue(treatmentStartCriteria.shouldStartTreatment(PATIENT_ID, adherence));
    }

    @Test
    public void shouldBeTrueWhenPatientNotOnTreatment() {
        Adherence adherence = new AdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        assertTrue(treatmentStartCriteria.shouldStartTreatment(PATIENT_ID, adherence));
    }

    @Test
    public void shouldBeFalseWhenPatientTypeIsNotNew() {
        Adherence adherence = new AdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.PHSTransfer).build();
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        assertFalse(treatmentStartCriteria.shouldStartTreatment(PATIENT_ID, adherence));
    }

    @Test
    public void shouldBeFalseWhenPatientOnTreatment() {
        Adherence adherence = new AdherenceBuilder().withDefaultLogs().build();

        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).onTreatmentFrom(DateUtil.today()).withType(PatientType.New).build();
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        assertFalse(treatmentStartCriteria.shouldStartTreatment(PATIENT_ID, adherence));
    }

    @Test
    public void shouldBeTrueIfAnyOfTheDosesAreTaken() {
        Adherence adherence = new AdherenceBuilder().withDefaultLogs().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        assertTrue(treatmentStartCriteria.shouldStartTreatment(PATIENT_ID, adherence));
    }

    @Test
    public void shouldBeFalseIfNoneOfTheDosesAreTaken() {
        Adherence adherence = new AdherenceBuilder().zeroDosesTaken().build();
        Patient patient = new PatientBuilder().withDefaults().withPatientId(PATIENT_ID).withType(PatientType.New).build();

        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        assertFalse(treatmentStartCriteria.shouldStartTreatment(PATIENT_ID, adherence));
    }

}
