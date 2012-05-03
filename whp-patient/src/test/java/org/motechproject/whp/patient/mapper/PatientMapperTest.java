package org.motechproject.whp.patient.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
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
    public void shouldMapPatientRequestToPatientDomain() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        Patient patient = patientMapper.map(patientRequest);
        assertBasicPatientInfo(patient, patientRequest);
        assertProvidedTreatment(patient, patientRequest);
    }

    @Test
    public void shouldMapWeightStatisticsAsEmpty_WhenMissing() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withWeightStatistics(null, null, DateUtil.today()).build();
        Patient patient = patientMapper.map(patientRequest);

        assertBasicPatientInfo(patient, patientRequest);

        ProvidedTreatment providedTreatment = patient.latestProvidedTreatment();
        assertEquals(0, providedTreatment.getTreatment().getWeightStatisticsList().size());
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id(), patient.getPatientId());
        assertEquals(patientRequest.getFirst_name(), patient.getFirstName());
        assertEquals(patientRequest.getLast_name(), patient.getLastName());
        assertEquals(patientRequest.getGender(), patient.getGender());
        assertEquals(patientRequest.getPatient_type(), patient.getPatientType());
        assertEquals(patientRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(patientRequest.getPhi(), patient.getPhi());
    }

    private void assertProvidedTreatment(Patient patient, PatientRequest patientRequest) {
        ProvidedTreatment providedTreatment = patient.latestProvidedTreatment();
        assertEquals(patientRequest.getTb_id(), providedTreatment.getTbId());
        assertEquals(patientRequest.getProvider_id(), providedTreatment.getProviderId());

        assertEquals(patientRequest.getAddress(), providedTreatment.getPatientAddress());

        assertTreatment(patient, patientRequest);
    }

    private void assertTreatment(Patient patient, PatientRequest patientRequest) {
        Treatment treatment = patient.latestProvidedTreatment().getTreatment();
        assertEquals(patientRequest.getAge(), treatment.getPatientAge());
        assertEquals(patientRequest.getTreatment_category(), treatment.getTreatmentCategory());
        assertNull(treatment.getDoseStartDate());

        assertEquals(patientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(patientRequest.getTreatmentStartDate(), treatment.getStartDate());

        assertSmearTests(patientRequest, treatment);
        assertWeightStatistics(patientRequest, treatment);

    }

    private void assertSmearTests(PatientRequest patientRequest, Treatment treatment) {
        SmearTestResults smearTestResults = patientRequest.getSmearTestResults();
        assertEquals(smearTestResults.getSmear_sample_instance(), treatment.getSmearTestResults().get(0).getSmear_sample_instance());
        assertEquals(smearTestResults.getSmear_test_result_1(), treatment.getSmearTestResults().get(0).getSmear_test_result_1());
        assertEquals(smearTestResults.getSmear_test_date_1(), treatment.getSmearTestResults().get(0).getSmear_test_date_1());
        assertEquals(smearTestResults.getSmear_test_result_2(), treatment.getSmearTestResults().get(0).getSmear_test_result_2());
        assertEquals(smearTestResults.getSmear_test_date_2(), treatment.getSmearTestResults().get(0).getSmear_test_date_2());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        assertEquals(patientRequest.getWeightStatistics(), treatment.getWeightStatisticsList().get(0));
    }
}
