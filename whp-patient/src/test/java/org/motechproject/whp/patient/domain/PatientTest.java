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
    ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tbId", PatientType.New);
    ProvidedTreatment newProviderTreatment = new ProvidedTreatment("newProviderId", "newTbId", PatientType.TreatmentAfterDefault);

    public PatientTest() {
        patient.addProvidedTreatment(providedTreatment, now());
        patient.addProvidedTreatment(newProviderTreatment, now());
    }

    @Test
    public void shouldUpdateCurrentProviderTreatmentWhenNewTreatmentIsAdded() {
        assertEquals(newProviderTreatment, patient.getCurrentProvidedTreatment());
    }

    @Test
    public void shouldUpdateProviderTreatmentHistoryWhenNewTreatmentIdAdded() {
        assertArrayEquals(new Object[]{providedTreatment}, patient.getProvidedTreatments().toArray());
    }

    @Test
    public void shouldNotHaveAnyHistoryWhenProvidedTreatmentHasNeverBeenUpdated() {
        Patient patientWithoutProvidedTreatment = new Patient("patientId", "firstName", "lastName", Gender.F, "1111111111");
        assertTrue(patientWithoutProvidedTreatment.getProvidedTreatments().isEmpty());
    }

    @Test
    public void shouldUpdateProviderTreatmentHistoryWhenNewTreatmentIdAddedForPatientWhoHasAHistory() {
        ProvidedTreatment newerProviderTreatment = new ProvidedTreatment("newerProviderId", "newerTbId", PatientType.Chronic);
        patient.addProvidedTreatment(newerProviderTreatment, now());

        assertArrayEquals(new Object[]{providedTreatment, newProviderTreatment}, patient.getProvidedTreatments().toArray());
    }

    @Test
    public void shouldCloseCurrentProvidedTreatment() {
        ProvidedTreatment providedTreatment = mock(ProvidedTreatment.class);

        DateTime now = now();
        patient.addProvidedTreatment(providedTreatment, now);

        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(providedTreatment, times(1)).close(TreatmentOutcome.Cured, now);
    }

    @Test
    public void shouldPauseCurrentProvidedTreatment() {
        ProvidedTreatment providedTreatment = mock(ProvidedTreatment.class);

        DateTime now = now();
        patient.addProvidedTreatment(providedTreatment, now);

        patient.pauseCurrentTreatment("paws", now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(providedTreatment, times(1)).pause("paws", now);
    }

    @Test
    public void shouldRestartCurrentProvidedTreatment() {
        ProvidedTreatment providedTreatment = mock(ProvidedTreatment.class);

        DateTime now = now();
        patient.addProvidedTreatment(providedTreatment, now);

        patient.restartCurrentTreatment("swap", now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(providedTreatment, times(1)).resume("swap", now);
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
