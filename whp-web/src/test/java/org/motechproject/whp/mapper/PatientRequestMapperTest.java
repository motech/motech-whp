package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.request.PatientWebRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientRequestMapperTest {

    @Mock
    AllTreatmentCategories allTreatmentCategories;

    PatientRequestMapper patientRequestMapper;

    private TreatmentCategory treatmentCategory;

    @Before
    public void setUp() {
        initMocks(this);
        treatmentCategory = new TreatmentCategory("cat1", "01", 3, 12, 22);
        patientRequestMapper = new PatientRequestMapper(allTreatmentCategories);
        when(allTreatmentCategories.findByCode("01")).thenReturn(treatmentCategory);
    }

    @Test
    public void shouldCreatePatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
        assertBasicPatientInfo(patientRequest, patientWebRequest);
        assertProvidedTreatment(patientRequest, patientWebRequest);
        assertTreatment(patientRequest, patientWebRequest);
    }

    private void assertBasicPatientInfo(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getCase_id(), patientRequest.getCaseId());
        assertEquals("Foo", patientRequest.getFirstName());
        assertEquals("Bar", patientRequest.getLastName());
        assertEquals(Gender.M, patientRequest.getGender());
        assertEquals(PatientType.PHSTransfer, patientRequest.getPatientType());
        assertEquals(patientWebRequest.getMobile_number(), patientRequest.getMobileNumber());
        assertEquals(patientWebRequest.getPhi(), patientRequest.getPhi());
    }

    private void assertProvidedTreatment(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getTb_id(), patientRequest.getTbId());
        assertEquals(patientWebRequest.getProvider_id(), patientRequest.getProviderId());
        assertEquals(patientWebRequest.getDate_modified(), patientRequest.getTreatmentStartDate().toString("dd/MM/YYYY HH:mm:ss"));

        assertPatientAddress(patientRequest.getAddress());
    }

    private void assertPatientAddress(Address address) {
        assertEquals(new Address("house number", "landmark", "block", "village", "district", "state"), address);
    }

    private void assertTreatment(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(Integer.parseInt(patientWebRequest.getAge()), patientRequest.getAge());
        assertEquals(patientWebRequest.getTreatment_category(), patientRequest.getTreatmentCategory().getCode());

        assertEquals(patientWebRequest.getTb_registration_number(), patientRequest.getTbRegistrationNumber());
        assertEquals(patientWebRequest.getDate_modified(), patientRequest.getTreatmentStartDate().toString("dd/MM/YYYY HH:mm:ss"));

        assertSmearTests(patientRequest, patientWebRequest);
        assertWeightStatistics(patientRequest, patientWebRequest);
    }

    private void assertSmearTests(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getSmear_sample_instance(), patientRequest.getSmearTestResults().getSampleInstance().name());
        assertEquals(patientWebRequest.getSmear_test_result_1(), patientRequest.getSmearTestResults().getResult1().name());
        assertEquals(patientWebRequest.getSmear_test_date_1(), patientRequest.getSmearTestResults().getTestDate1().toString("dd/MM/YYYY"));
        assertEquals(patientWebRequest.getSmear_test_result_2(), patientRequest.getSmearTestResults().getResult2().name());
        assertEquals(patientWebRequest.getSmear_test_date_2(), patientRequest.getSmearTestResults().getTestDate2().toString("dd/MM/YYYY"));
    }

    private void assertWeightStatistics(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(WeightInstance.valueOf(patientWebRequest.getWeight_instance()), patientRequest.getWeightStatistics().getWeightInstance());
        assertEquals(Double.parseDouble(patientWebRequest.getWeight()), patientRequest.getWeightStatistics().getWeight(), 0.0);
        assertEquals("10/10/2010", patientRequest.getWeightStatistics().getMeasuringDate().toString("dd/MM/YYYY"));
    }


}
