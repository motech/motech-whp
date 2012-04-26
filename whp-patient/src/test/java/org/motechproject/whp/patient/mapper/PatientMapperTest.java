package org.motechproject.whp.patient.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.CreatePatientRequestBuilder;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.patient.domain.Treatment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PatientMapperTest {

    PatientMapper patientMapper;

    @Before
    public void setUp() {
        patientMapper = new PatientMapper();
    }

    @Test
    public void shouldCreatePatient() {
        CreatePatientRequest createPatientRequest = new CreatePatientRequestBuilder().withDefaults().build();
        Patient patient = patientMapper.map(createPatientRequest);
        assertBasicPatientInfo(patient, createPatientRequest);
        assertProvidedTreatment(patient, createPatientRequest);
    }

    private void assertBasicPatientInfo(Patient patient, CreatePatientRequest createPatientRequest) {
        assertEquals(createPatientRequest.getCaseId(), patient.getPatientId());
        assertEquals(createPatientRequest.getFirstName(), patient.getFirstName());
        assertEquals(createPatientRequest.getLastName(), patient.getLastName());
        assertEquals(createPatientRequest.getGender(), patient.getGender());
        assertEquals(createPatientRequest.getPatientType(), patient.getPatientType());
        assertEquals(createPatientRequest.getMobileNumber(), patient.getPhoneNumber());
        assertEquals(createPatientRequest.getPhi(), patient.getPhi());
    }

    private void assertProvidedTreatment(Patient patient, CreatePatientRequest createPatientRequest) {
        ProvidedTreatment providedTreatment = patient.latestProvidedTreatment();
        assertEquals(createPatientRequest.getTbId(), providedTreatment.getTbId());
        assertEquals(createPatientRequest.getProviderId(), providedTreatment.getProviderId());
        assertEquals(createPatientRequest.getTreatmentStartDate().toLocalDate(), providedTreatment.getStartDate());

        assertEquals(createPatientRequest.getAddress(), providedTreatment.getPatientAddress());

        assertTreatment(patient, createPatientRequest);
    }

    private void assertTreatment(Patient patient, CreatePatientRequest createPatientRequest) {
        Treatment treatment = patient.latestProvidedTreatment().getTreatment();
        assertEquals(createPatientRequest.getAge(), treatment.getPatientAge());
        assertEquals(createPatientRequest.getTreatmentCategory(), treatment.getTreatmentCategory());
        assertNull(treatment.getDoseStartDate());

        assertEquals(createPatientRequest.getTbRegistrationNumber(), treatment.getTbRegistrationNumber());
        assertEquals(createPatientRequest.getTreatmentStartDate(), treatment.getStartDate());

        assertSmearTests(createPatientRequest, treatment);
        assertWeightStatistics(createPatientRequest, treatment);

    }

    private void assertSmearTests(CreatePatientRequest createPatientRequest, Treatment treatment) {
        SmearTestResults smearTestResults = createPatientRequest.getSmearTestResults();
        assertEquals(smearTestResults.getSampleInstance(), treatment.getSmearTestResults().get(0).getSampleInstance());
        assertEquals(smearTestResults.getResult1(), treatment.getSmearTestResults().get(0).getResult1());
        assertEquals(smearTestResults.getTestDate1(), treatment.getSmearTestResults().get(0).getTestDate1());
        assertEquals(smearTestResults.getResult2(), treatment.getSmearTestResults().get(0).getResult2());
        assertEquals(smearTestResults.getTestDate2(), treatment.getSmearTestResults().get(0).getTestDate2());
    }

    private void assertWeightStatistics(CreatePatientRequest createPatientRequest, Treatment treatment) {
        assertEquals(createPatientRequest.getWeightStatistics(), treatment.getWeightStatisticsList().get(0));
    }
}
