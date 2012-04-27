package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.Address;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.domain.WeightInstance;
import org.motechproject.whp.request.PatientRequest;

import static org.junit.Assert.assertEquals;

public class PatientRequestMapperTest {

    PatientRequestMapper patientRequestMapper;

    @Before
    public void setUp() {
        patientRequestMapper = new PatientRequestMapper();
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        CreatePatientRequest createPatientRequest = patientRequestMapper.map(patientRequest);
        assertBasicPatientInfo(createPatientRequest, patientRequest);
        assertProvidedTreatment(createPatientRequest, patientRequest);
        assertTreatment(createPatientRequest, patientRequest);
    }

    private void assertBasicPatientInfo(CreatePatientRequest createPatientRequest, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id(), createPatientRequest.getCaseId());
        assertEquals("Foo", createPatientRequest.getFirstName());
        assertEquals("Bar", createPatientRequest.getLastName());
        assertEquals(Gender.M, createPatientRequest.getGender());
        assertEquals(PatientType.PHSTransfer, createPatientRequest.getPatientType());
        assertEquals(patientRequest.getMobile_number(), createPatientRequest.getMobileNumber());
        assertEquals(patientRequest.getPhi(), createPatientRequest.getPhi());
    }

    private void assertProvidedTreatment(CreatePatientRequest createPatientRequest, PatientRequest patientRequest) {
        assertEquals(patientRequest.getTb_id(), createPatientRequest.getTbId());
        assertEquals(patientRequest.getProvider_id(), createPatientRequest.getProviderId());
        assertEquals(patientRequest.getDate_modified(), createPatientRequest.getTreatmentStartDate().toString("dd/MM/YYYY HH:mm:ss"));

        assertPatientAddress(createPatientRequest.getAddress());
    }

    private void assertPatientAddress(Address address) {
        assertEquals(new Address("house number", "landmark", "block", "village", "district", "state"), address);
    }

    private void assertTreatment(CreatePatientRequest createPatientRequest, PatientRequest patientRequest) {
        assertEquals(Integer.parseInt(patientRequest.getAge()), createPatientRequest.getAge());
        assertEquals(patientRequest.getTreatment_category(), createPatientRequest.getTreatmentCategory().value());

        assertEquals(patientRequest.getTb_registration_number(), createPatientRequest.getTbRegistrationNumber());
        assertEquals(patientRequest.getDate_modified(), createPatientRequest.getTreatmentStartDate().toString("dd/MM/YYYY HH:mm:ss"));

        assertSmearTests(createPatientRequest, patientRequest);
        assertWeightStatistics(createPatientRequest, patientRequest);
    }

    private void assertSmearTests(CreatePatientRequest createPatientRequest, PatientRequest patientRequest) {
        assertEquals(patientRequest.getSmear_sample_instance(), createPatientRequest.getSmearTestResults().getSampleInstance().name());
        assertEquals(patientRequest.getSmear_test_result_1(), createPatientRequest.getSmearTestResults().getResult1().name());
        assertEquals(patientRequest.getSmear_test_date_1(), createPatientRequest.getSmearTestResults().getTestDate1().toString("dd/MM/YYYY"));
        assertEquals(patientRequest.getSmear_test_result_2(), createPatientRequest.getSmearTestResults().getResult2().name());
        assertEquals(patientRequest.getSmear_test_date_2(), createPatientRequest.getSmearTestResults().getTestDate2().toString("dd/MM/YYYY"));
    }

    private void assertWeightStatistics(CreatePatientRequest createPatientRequest, PatientRequest patientRequest) {
        assertEquals(WeightInstance.valueOf(patientRequest.getWeight_instance()), createPatientRequest.getWeightStatistics().getWeightInstance());
        assertEquals(Double.parseDouble(patientRequest.getWeight()), createPatientRequest.getWeightStatistics().getWeight(), 0.0);
        assertEquals("10/10/2010", createPatientRequest.getWeightStatistics().getMeasuringDate().toString("dd/MM/YYYY"));
    }


}
