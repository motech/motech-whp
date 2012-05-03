package org.motechproject.whp.mapper;

import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientRequestMapperTest extends SpringIntegrationTest {

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    @Autowired
    DozerBeanMapper mapper;

    PatientRequestMapper patientRequestMapper;

    @Before
    public void setUp() {
        initMocks(this);
        TreatmentCategory treatmentCategory = new TreatmentCategory("cat1", "01", 3, 12, 22, Arrays.asList(DayOfWeek.Monday));
        patientRequestMapper = new PatientRequestMapper(allTreatmentCategories, mapper);
        allTreatmentCategories.add(treatmentCategory);
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
        assertEquals(patientWebRequest.getCase_id(), patientRequest.getCase_id());
        assertEquals("Foo", patientRequest.getFirst_name());
        assertEquals("Bar", patientRequest.getLast_name());
        assertEquals(Gender.M, patientRequest.getGender());
        assertEquals(PatientType.PHSTransfer, patientRequest.getPatient_type());
        assertEquals(patientWebRequest.getMobile_number(), patientRequest.getMobile_number());
        assertEquals(patientWebRequest.getPhi(), patientRequest.getPhi());
    }

    private void assertProvidedTreatment(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getTb_id(), patientRequest.getTb_id());
        assertEquals(patientWebRequest.getProvider_id(), patientRequest.getProvider_id());

        assertPatientAddress(patientRequest.getAddress());
    }

    private void assertPatientAddress(Address address) {
        assertEquals(new Address("house number", "landmark", "block", "village", "district", "state"), address);
    }

    private void assertTreatment(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(Integer.parseInt(patientWebRequest.getAge()), patientRequest.getAge());
        assertEquals(patientWebRequest.getTreatment_category(), patientRequest.getTreatment_category().getCode());

        assertEquals(patientWebRequest.getTb_registration_number(), patientRequest.getTb_registration_number());

        assertSmearTests(patientRequest, patientWebRequest);
        assertWeightStatistics(patientRequest, patientWebRequest);
    }

    private void assertSmearTests(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getSmear_sample_instance(), patientRequest.getSmearTestResults().getSmear_sample_instance().name());
        assertEquals(patientWebRequest.getSmear_test_result_1(), patientRequest.getSmearTestResults().getSmear_test_result_1().name());
        assertEquals(patientWebRequest.getSmear_test_date_1(), patientRequest.getSmearTestResults().getSmear_test_date_1().toString("dd/MM/YYYY"));
        assertEquals(patientWebRequest.getSmear_test_result_2(), patientRequest.getSmearTestResults().getSmear_test_result_2().name());
        assertEquals(patientWebRequest.getSmear_test_date_2(), patientRequest.getSmearTestResults().getSmear_test_date_2().toString("dd/MM/YYYY"));
    }

    private void assertWeightStatistics(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(WeightInstance.valueOf(patientWebRequest.getWeight_instance()), patientRequest.getWeightStatistics().getWeight_instance());
        assertEquals(Double.parseDouble(patientWebRequest.getWeight()), patientRequest.getWeightStatistics().getWeight(), 0.0);
        assertEquals("10/10/2010", patientRequest.getWeightStatistics().getMeasuringDate().toString("dd/MM/YYYY"));
    }

    @After
    public void tearDown(){
        allTreatmentCategories.removeAll();
    }
}
