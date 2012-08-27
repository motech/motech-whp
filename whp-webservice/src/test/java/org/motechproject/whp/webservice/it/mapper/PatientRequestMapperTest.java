package org.motechproject.whp.webservice.it.mapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Address;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.refdata.repository.AllTreatmentCategories;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.mapper.PatientRequestMapper;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;


@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
public class PatientRequestMapperTest extends SpringIntegrationTest {

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    @Autowired
    PatientRequestMapper patientRequestMapper;

    @Before
    public void setUp() {
        initMocks(this);
        TreatmentCategory treatmentCategory = new TreatmentCategory("cat1", "01", 3, 12, 36, 4, 12, 22, 66, Arrays.asList(DayOfWeek.Monday));
        allTreatmentCategories.add(treatmentCategory);
    }

    @Test
    public void shouldMapBasicInformationWhenMappingPatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
        assertBasicPatientInfo(patientRequest, patientWebRequest);
    }

    @Test
    public void shouldMapTreatmentWhenMappingPatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
        assertTreatment(patientRequest, patientWebRequest);
    }

    @Test
    public void shouldMapTherapyWhenMappingPatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
        assertTherapy(patientRequest, patientWebRequest);
    }

    @Test
    public void shouldCreateTreatmentUpdateRequest() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().build();
        PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);

        assertEquals(patientWebRequest.getCase_id(), patientRequest.getCase_id());
        assertEquals(patientWebRequest.getDate_modified(), patientRequest.getDate_modified().toString(DATE_TIME_FORMAT));
        assertEquals(patientWebRequest.getPatient_type(), patientRequest.getPatient_type().name());
        assertEquals(patientWebRequest.getProvider_id(), patientRequest.getProvider_id());
        assertEquals(TreatmentOutcome.valueOf(patientWebRequest.getTreatment_outcome()), patientRequest.getTreatment_outcome());
        assertEquals(patientWebRequest.getTb_id(), patientRequest.getTb_id());
        assertEquals(patientWebRequest.getTreatment_category(), patientRequest.getTreatment_category().getCode());
    }

    private void assertBasicPatientInfo(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getCase_id(), patientRequest.getCase_id());
        assertEquals(patientWebRequest.getFirst_name(), patientRequest.getFirst_name());
        assertEquals(patientWebRequest.getLast_name(), patientRequest.getLast_name());
        assertEquals(patientWebRequest.getGender(), patientRequest.getGender().name());
        assertEquals(patientWebRequest.getPatient_type(), patientRequest.getPatient_type().name());
        assertEquals(patientWebRequest.getMobile_number(), patientRequest.getMobile_number());
        assertEquals(patientWebRequest.getPhi(), patientRequest.getPhi());
    }

    private void assertTreatment(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getTb_id(), patientRequest.getTb_id());
        assertEquals(patientWebRequest.getProvider_id(), patientRequest.getProvider_id());
        assertPatientAddress(patientRequest.getAddress(), patientWebRequest);
    }

    private void assertPatientAddress(Address address, PatientWebRequest patientWebRequest) {
        assertEquals(
                new Address(
                        patientWebRequest.getAddress_location(),
                        patientWebRequest.getAddress_landmark(),
                        patientWebRequest.getAddress_block(),
                        patientWebRequest.getAddress_village(),
                        patientWebRequest.getAddress_district(),
                        patientWebRequest.getAddress_state()
                ), address
        );
    }

    private void assertTherapy(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals((Integer) Integer.parseInt(patientWebRequest.getAge()), patientRequest.getAge());
        assertEquals(patientWebRequest.getTreatment_category(), patientRequest.getTreatment_category().getCode());
        assertEquals(patientWebRequest.getTb_registration_number(), patientRequest.getTb_registration_number());
        assertSmearTests(patientRequest, patientWebRequest);
        assertWeightStatistics(patientRequest, patientWebRequest);
    }

    private void assertSmearTests(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(patientWebRequest.getSmear_sample_instance(), patientRequest.getSmearTestResults().get(0).getSmear_sample_instance().name());
        assertEquals(patientWebRequest.getSmear_test_result_1(), patientRequest.getSmearTestResults().get(0).getSmear_test_result_1().name());
        assertEquals(patientWebRequest.getSmear_test_date_1(), patientRequest.getSmearTestResults().get(0).getSmear_test_date_1().toString(DATE_FORMAT));
        assertEquals(patientWebRequest.getSmear_test_result_2(), patientRequest.getSmearTestResults().get(0).getSmear_test_result_2().name());
        assertEquals(patientWebRequest.getSmear_test_date_2(), patientRequest.getSmearTestResults().get(0).getSmear_test_date_2().toString(DATE_FORMAT));
    }

    private void assertWeightStatistics(PatientRequest patientRequest, PatientWebRequest patientWebRequest) {
        assertEquals(SampleInstance.valueOf(patientWebRequest.getWeight_instance()), patientRequest.getWeightStatistics().get(0).getWeight_instance());
        assertEquals(Double.parseDouble(patientWebRequest.getWeight()), patientRequest.getWeightStatistics().get(0).getWeight(), 0.0);
        assertEquals("10/10/2010", patientRequest.getWeightStatistics().get(0).getMeasuringDate().toString(DATE_FORMAT));
    }

    @After
    public void tearDown() {
        allTreatmentCategories.removeAll();
    }
}
