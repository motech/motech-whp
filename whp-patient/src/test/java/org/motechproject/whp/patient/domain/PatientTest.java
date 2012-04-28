package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class PatientTest {

    Patient patient = new Patient("patientId", "patientFirstName", "patientLastName", Gender.F, PatientType.New, "1111111111");
    ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tbId");
    ProvidedTreatment newProviderTreatment = new ProvidedTreatment("newProviderId", "newTbId");

    public PatientTest() {
        patient.addProvidedTreatment(providedTreatment);
        patient.addProvidedTreatment(newProviderTreatment);
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
        patient.addProvidedTreatment(newerProviderTreatment);

        assertArrayEquals(new Object[]{providedTreatment, newProviderTreatment}, patient.getProvidedTreatments().toArray());
    }
}
