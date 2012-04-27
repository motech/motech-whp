package org.motechproject.whp.patient.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
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
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        Patient patient = patientMapper.map(patientRequest);
        assertBasicPatientInfo(patient, patientRequest);
        assertProvidedTreatment(patient, patientRequest);
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCaseId(), patient.getPatientId());
        assertEquals(patientRequest.getFirstName(), patient.getFirstName());
        assertEquals(patientRequest.getLastName(), patient.getLastName());
        assertEquals(patientRequest.getGender(), patient.getGender());
        assertEquals(patientRequest.getPatientType(), patient.getPatientType());
        assertEquals(patientRequest.getMobileNumber(), patient.getPhoneNumber());
        assertEquals(patientRequest.getPhi(), patient.getPhi());
    }

    private void assertProvidedTreatment(Patient patient, PatientRequest patientRequest) {
        ProvidedTreatment providedTreatment = patient.latestProvidedTreatment();
        assertEquals(patientRequest.getTbId(), providedTreatment.getTbId());
        assertEquals(patientRequest.getProviderId(), providedTreatment.getProviderId());
        assertEquals(patientRequest.getTreatmentStartDate().toLocalDate(), providedTreatment.getStartDate());

        assertEquals(patientRequest.getAddress(), providedTreatment.getPatientAddress());

        assertTreatment(patient, patientRequest);
    }

    private void assertTreatment(Patient patient, PatientRequest patientRequest) {
        Treatment treatment = patient.latestProvidedTreatment().getTreatment();
        assertEquals(patientRequest.getAge(), treatment.getPatientAge());
        assertEquals(patientRequest.getTreatmentCategory(), treatment.getTreatmentCategory());
        assertNull(treatment.getDoseStartDate());

        assertEquals(patientRequest.getTbRegistrationNumber(), treatment.getTbRegistrationNumber());
        assertEquals(patientRequest.getTreatmentStartDate(), treatment.getStartDate());

        assertSmearTests(patientRequest, treatment);
        assertWeightStatistics(patientRequest, treatment);

    }

    private void assertSmearTests(PatientRequest patientRequest, Treatment treatment) {
        SmearTestResults smearTestResults = patientRequest.getSmearTestResults();
        assertEquals(smearTestResults.getSampleInstance(), treatment.getSmearTestResults().get(0).getSampleInstance());
        assertEquals(smearTestResults.getResult1(), treatment.getSmearTestResults().get(0).getResult1());
        assertEquals(smearTestResults.getTestDate1(), treatment.getSmearTestResults().get(0).getTestDate1());
        assertEquals(smearTestResults.getResult2(), treatment.getSmearTestResults().get(0).getResult2());
        assertEquals(smearTestResults.getTestDate2(), treatment.getSmearTestResults().get(0).getTestDate2());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        assertEquals(patientRequest.getWeightStatistics(), treatment.getWeightStatisticsList().get(0));
    }
}
