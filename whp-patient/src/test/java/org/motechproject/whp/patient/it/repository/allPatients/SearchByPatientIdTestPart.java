package org.motechproject.whp.patient.it.repository.allPatients;

import org.junit.Test;
import org.motechproject.whp.patient.assertUtil.PatientAssert;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SearchByPatientIdTestPart extends AllPatientsTestPart {

    @Test
    public void patientIdShouldBeCaseInsensitive() {
        createPatient("Cha01100002", "providerId", PROVIDER_DISTRICT);

        Patient savedPatient = allPatients.findByPatientId("chA01100002");
        assertNotNull(savedPatient);
    }

    @Test
    public void shouldSaveIdsInLowerCase() {
        createPatient("Cha01100002", "providerId", PROVIDER_DISTRICT);
        Patient savedPatient = allPatients.findByPatientId("chA01100002");

        assertEquals("tbid", savedPatient.getCurrentTreatment().getTbId());
        assertEquals("providerid", savedPatient.getCurrentTreatment().getProviderId());
    }

    @Test
    public void shouldLoadTherapyUponFetch() {
        Patient patient = PatientBuilder.patient();
        allPatients.add(patient);

        Patient savedPatient = allPatients.findByPatientId(patient.getPatientId());
        PatientAssert.assertPatientEquals(patient, savedPatient);
    }

    @Test
    public void findByPatientIdShouldReturnNullIfKeywordIsNull() {
        assertEquals(null, allPatients.findByPatientId(null));
    }
}
