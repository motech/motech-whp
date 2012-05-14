package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.motechproject.util.DateUtil.now;

public class PatientTest {

    Patient patient = new Patient("patientId", "patientFirstName", "patientLastName", Gender.F, PatientType.New, "1111111111");
    ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tbId");
    ProvidedTreatment newProviderTreatment = new ProvidedTreatment("newProviderId", "newTbId");

    public PatientTest() {
        patient.addProvidedTreatment(providedTreatment, now());
        patient.addProvidedTreatment(newProviderTreatment, now());
    }

    @Test
    public void shouldUpdateCurrentProviderTreatmentWhenNewTreatmentIsAdded() {
        assertEquals(newProviderTreatment, patient.latestProvidedTreatment());
    }

    @Test
    public void shouldUpdateProviderTreatmentHistoryWhenNewTreatmentIdAdded() {
        assertArrayEquals(new Object[]{providedTreatment}, patient.getProvidedTreatments().toArray());
    }

    @Test
    public void shouldNotHaveAnyHistoryWhenProvidedTreatmentHasNeverBeenUpdated() {
        Patient patientWithoutProvidedTreatment = new Patient("patientId", "firstName", "lastName", Gender.F, PatientType.New, "1111111111");
        assertTrue(patientWithoutProvidedTreatment.getProvidedTreatments().isEmpty());
    }

    @Test
    public void shouldUpdateProviderTreatmentHistoryWhenNewTreatmentIdAddedForPatientWhoHasAHistory() {
        ProvidedTreatment newerProviderTreatment = new ProvidedTreatment("newerProviderId", "newerTbId");
        patient.addProvidedTreatment(newerProviderTreatment, now());

        assertArrayEquals(new Object[]{providedTreatment, newProviderTreatment}, patient.getProvidedTreatments().toArray());
    }

    @Test
    public void shouldCloseCurrentProvidedTreatment() {
        ProvidedTreatment providedTreatment = mock(ProvidedTreatment.class);

        DateTime now = now();
        patient.addProvidedTreatment(providedTreatment, now);

        patient.closeCurrentTreatment("Cured", now);
        assertEquals(now, patient.getLastModifiedDate());
        verify(providedTreatment, times(1)).close("Cured", now);
    }
}
