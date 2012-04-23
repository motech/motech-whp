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

        assertBasicPatientInfo(patient, patientRequest);
        assertProvidedTreatment(patient, patientRequest);
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id(), patient.getPatientId());
        assertEquals("Foo", patient.getFirstName());
        assertEquals("Bar", patient.getLastName());
        assertEquals(Gender.Male, patient.getGender());
        assertEquals(PatientType.PHSTransfer, patient.getPatientType());
        assertEquals(patientRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(patientRequest.getDate_modified(), patient.getLastModifiedDate().toString("dd/MM/YYYY HH:mm:ss"));
    }

    private void assertProvidedTreatment(Patient patient, PatientRequest patientRequest) {
        ProvidedTreatment providedTreatment = patient.getLatestProvidedTreatment();
        assertEquals(patientRequest.getTb_id(), providedTreatment.getTbId());
        assertEquals(patientRequest.getProvider_id(), providedTreatment.getProviderId());
        assertEquals(DateUtil.today(), providedTreatment.getStartDate());

        assertPatientAddress(providedTreatment.getPatientAddress());
    }

    private void assertPatientAddress(Address address) {
        assertEquals(new Address("house number", "landmark", "block", "village", "district", "state"), address);
    }

}
