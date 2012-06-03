package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        assertEquals("qwer",patient.getPatientId());

        patient = new Patient("QWER","","",Gender.M,"");
        assertEquals("qwer",patient.getPatientId());

        patient.setPatientId(null);
        assertEquals(null,patient.getPatientId());
    }

    @Test
    public void settingIdsShouldHandleNullValues() {
        Patient patient = new Patient("","","",Gender.F,"");
        patient.setPatientId(null);
        assertEquals(null,patient.getPatientId());

        patient = new Patient(null,"","",Gender.F,"");
        assertEquals(null,patient.getPatientId());

    }

}
