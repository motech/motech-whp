package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.now;

public class PatientTest {

    Patient patient = new Patient("patientId", "patientFirstName", "patientLastName", Gender.F, "1111111111");
    Treatment treatment = new Treatment("providerId", "tbId", PatientType.New);
    Treatment newProviderTreatment = new Treatment("newProviderId", "newTbId", PatientType.TreatmentAfterDefault);

    public PatientTest() {
        patient.addTreatment(treatment, now());
        patient.addTreatment(newProviderTreatment, now());
    }

    @Test
    public void shouldUpdateCurrentProviderTreatmentWhenNewTreatmentIsAdded() {
        assertEquals(newProviderTreatment, patient.getCurrentTreatment());
    }

    @Test
    public void shouldUpdateProviderTreatmentHistoryWhenNewTreatmentIdAdded() {
        assertArrayEquals(new Object[]{treatment}, patient.getTreatments().toArray());
    }

    @Test
    public void shouldNotHaveAnyHistoryWhenTreatmentHasNeverBeenUpdated() {
        Patient patientWithoutTreatment = new Patient("patientId", "firstName", "lastName", Gender.F, "1111111111");
        assertTrue(patientWithoutTreatment.getTreatments().isEmpty());
    }

    @Test
    public void shouldUpdateProviderTreatmentHistoryWhenNewTreatmentIdAddedForPatientWhoHasAHistory() {
        Treatment newerProviderTreatment = new Treatment("newerProviderId", "newerTbId", PatientType.Chronic);
        patient.addTreatment(newerProviderTreatment, now());

        assertArrayEquals(new Object[]{treatment, newProviderTreatment}, patient.getTreatments().toArray());
    }

    @Test
    public void shouldCloseCurrentTreatment() {
        Treatment treatment = mock(Treatment.class);

        DateTime now = now();
        patient.addTreatment(treatment, now);

        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(treatment, times(1)).close(TreatmentOutcome.Cured, now);
    }

    @Test
    public void shouldPauseCurrentTreatment() {
        Treatment treatment = mock(Treatment.class);

        DateTime now = now();
        patient.addTreatment(treatment, now);

        patient.pauseCurrentTreatment("paws", now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(treatment, times(1)).pause("paws", now);
    }

    @Test
    public void shouldRestartCurrentTreatment() {
        Treatment treatment = mock(Treatment.class);

        DateTime now = now();
        patient.addTreatment(treatment, now);

        patient.restartCurrentTreatment("swap", now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(treatment, times(1)).resume("swap", now);
    }

    @Test
    public void shouldStoreIdsInLowerCase() {
        Patient patient = new Patient();
        patient.setPatientId("QWER");
        assertEquals("qwer", patient.getPatientId());

        patient = new Patient("QWER", "", "", Gender.M, "");
        assertEquals("qwer", patient.getPatientId());

        patient.setPatientId(null);
        assertEquals(null, patient.getPatientId());
    }

    @Test
    public void shouldReturnAllTreatmentForPatientChronologically() {
        Treatment treatment1 = new TreatmentBuilder().withDefaults().withProviderId("provider1").build();
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatment(treatment1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withProviderId("provider2").build();
        Treatment treatment3 = new TreatmentBuilder().withDefaults().withProviderId("provider3").build();
        Treatment treatment4 = new TreatmentBuilder().withDefaults().withProviderId("provider4").build();
        patient.addTreatment(treatment2, now());
        patient.addTreatment(treatment3, now());
        patient.addTreatment(treatment4, now());

        List<Treatment> treatments = patient.allTreatmentsChronologically();
        assertArrayEquals(new Treatment[]{treatment1, treatment2, treatment3, treatment4}, treatments.toArray());
    }

    @Test
    public void shouldGetCurrentTreatmentIfGivenDateIsInCurrentTreatmentPeriod() {
        Patient patient = new PatientBuilder().withDefaults().build();
        String therapyDocId = "therapyDocId";
        patient.getCurrentTreatment().setTherapyDocId(therapyDocId);
        LocalDate startDate = new LocalDate(2012, 1, 2);
        LocalDate endDate = new LocalDate(2012, 3, 15);
        patient.getCurrentTreatment().setStartDate(startDate);
        patient.getCurrentTreatment().setEndDate(endDate);
        assertEquals(patient.getCurrentTreatment(), patient.getTreatmentForDateInTherapy(startDate, therapyDocId));
        assertEquals(patient.getCurrentTreatment(), patient.getTreatmentForDateInTherapy(endDate, therapyDocId));
        assertEquals(patient.getCurrentTreatment(), patient.getTreatmentForDateInTherapy(startDate.plusDays(15), therapyDocId));
    }

    @Test
    public void shouldReturnTreatmentForGivenDate() {
        Patient patient = new PatientBuilder().withDefaults().build();
        LocalDate startDate1 = new LocalDate(2012, 1, 2);
        LocalDate startDate2 = new LocalDate(2012, 2, 2);
        LocalDate startDate3 = new LocalDate(2012, 3, 2);

        patient.setCurrentTreatment(new TreatmentBuilder().withDefaults().withStartDate(startDate3).withTherapyDocId("1").build());
        Treatment treatment1 = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(startDate1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withTherapyDocId("2").withStartDate(startDate2).build();

        patient.setTreatments(asList(treatment1, treatment2));
        assertEquals(treatment1, patient.getTreatmentForDateInTherapy(startDate1, "1"));
        assertNull(patient.getTreatmentForDateInTherapy(startDate2, "1"));
        assertEquals(treatment2, patient.getTreatmentForDateInTherapy(startDate2, "2"));
        assertNull(patient.getTreatmentForDateInTherapy(startDate3, "2"));
        assertEquals(patient.getCurrentTreatment(), patient.getTreatmentForDateInTherapy(startDate3, "1"));
        assertEquals(patient.getCurrentTreatment(), patient.getTreatmentForDateInTherapy(startDate3.plusMonths(10), "1"));
    }

    @Test
    public void settingIdsShouldHandleNullValues() {
        Patient patient = new Patient("", "", "", Gender.F, "");
        patient.setPatientId(null);
        assertEquals(null, patient.getPatientId());

        patient = new Patient(null, "", "", Gender.F, "");
        assertEquals(null, patient.getPatientId());

    }

}
