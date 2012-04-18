package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

import static org.junit.Assert.assertEquals;

public class PatientMapperTest {

    PatientMapper patientMapper;

    @Before
    public void setUp() {
        patientMapper = new PatientMapper();
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        Treatment treatment = new Treatment();

        Patient patient = patientMapper.map(patientRequest, treatment);

        assertBasicPatientInfo(patient);
        assertProvidedTreatment(patient);
    }

    private void assertBasicPatientInfo(Patient patient) {
        assertEquals("caseId", patient.getPatientId());
        assertEquals("Foo", patient.getFirstName());
        assertEquals("Bar", patient.getLastName());
        assertEquals(Gender.Male, patient.getGender());
        assertEquals(PatientType.PHSTransfer, patient.getPatientType());
        assertEquals("12345667890", patient.getPhoneNumber());
    }

    private void assertProvidedTreatment(Patient patient) {
        ProvidedTreatment providedTreatment = patient.getLatestProvidedTreatment();
        assertEquals("providerId01seq1", providedTreatment.getTbId());
        assertEquals("providerId", providedTreatment.getProviderId());
        assertEquals(DateUtil.today(), providedTreatment.getStartDate());

        assertPatientAddress(providedTreatment.getPatientAddress());
    }

    private void assertPatientAddress(Address address) {
        assertEquals(new Address("house number", "landmark", "block", "village", "district", "state", "postal code"), address);
    }

}
