package org.motechproject.whp.webservice.it.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.builder.request.ContainerPatientMappingReportingRequestBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.ContainerPatientMappingReportingRequest;
import org.motechproject.whp.webservice.service.ContainerPatientMappingWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.domain.ContainerStatus.Closed;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.exception.WHPErrorCode.NO_LAB_RESULTS_IN_CONTAINER;
import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationWebServiceContext.xml")
public class ContainerPatientMappingWebServiceIT extends SpringIntegrationTest {

    private static final String CONTAINER_ID_1 = "11111111111";
    private static final String CONTAINER_ID_2 = "22222222222";
    private static final String PATIENT_ID_1 = "1";
    private static final String PATIENT_ID_2 = "2";
    private static final String CONTAINER_PATIENT_MAPPING_API_URL = "/containerPatientMapping/process";
    @Autowired
    ContainerPatientMappingWebService containerPatientMappingWebService;

    @Autowired
    AllPatients allPatients;

    @Autowired
    AllContainers allContainers;

    @Autowired
    AllReasonForContainerClosures allReasonForContainerClosures;

    @Autowired
    private ReportingEventURLs reportingEventURLs;

    @ReplaceWithMock
    @Autowired
    private HttpClientService httpClientService;


    private Patient patient1;
    private Patient patient2;
    private Container container1;
    private Container container2;

    @Before
    public void setup() {
        reset(httpClientService);
        patient1 = new PatientBuilder().withDefaults().build();
        patient1.setPatientId(PATIENT_ID_1);
        patient1.getCurrentTreatment().setTbId(PATIENT_ID_1);
        allPatients.add(patient1);

        patient2 = new PatientBuilder().withDefaults().build();
        patient2.setPatientId(PATIENT_ID_2);
        patient2.getCurrentTreatment().setTbId(PATIENT_ID_2);
        allPatients.add(patient2);

        LabResults labResults = new LabResults();
        container1 = new Container("providerId", CONTAINER_ID_1, null, DateTime.now(), "d1");
        container1.setStatus(Open);
        container1.setDiagnosis(Pending);
        container1.setLabResults(labResults);
        allContainers.add(container1);

        container2 = new Container("providerId", CONTAINER_ID_2, null, DateTime.now(), "d1");
        container2.setStatus(Open);
        container2.setDiagnosis(Pending);
        container2.setLabResults(labResults);
        allContainers.add(container2);

        ReasonForContainerClosure reasonForContainerClosure = new ReasonForContainerClosure("some reason", CLOSURE_DUE_TO_MAPPING);
        allReasonForContainerClosures.add(reasonForContainerClosure);

        markForDeletion(reasonForContainerClosure);
        markForDeletion(container1, container2);
        markForDeletion(patient1, patient2);
    }

    @After
    public void tearDown() {
        allContainers.removeAll();
        allPatients.removeAll();
        verifyNoMoreInteractions(httpClientService);
    }

    @Test
    public void shouldNotMapContainerWithPatient_uponValidationFailure() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"" + CONTAINER_ID_1 + "\" date_modified=\"03/04/2012 11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>patient_mapping</update_type>\n" +
                "<patient_id>" + PATIENT_ID_1 + "</patient_id>\n" +
                "<tb_id></tb_id>\n" +
                "<smear_sample_instance>PreTreatment</smear_sample_instance>\n" +
                "</update>\n" +
                "</case>";

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(containsString("No such treatment exists for patient"))));

        Container fetchedContainer = allContainers.findByContainerId(CONTAINER_ID_1);
        assertNull(fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Open, fetchedContainer.getStatus());
        verifyZeroInteractions(httpClientService);
    }

    @Test
    public void shouldNotUnMapContainerFromPreviousPatient_uponMappingValidationFailure() throws Exception {
        container1.mapWith(PATIENT_ID_1, patient1.getCurrentTreatment().getTbId(), SputumTrackingInstance.TwoMonthsIntoCP, new ReasonForContainerClosure(), new LocalDate());
        container2.setLabResults(null);
        allContainers.update(container1);
        allContainers.update(container2);

        String request = buildMappingRequestFor(PATIENT_ID_1, CONTAINER_ID_2);
        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(containsString(NO_LAB_RESULTS_IN_CONTAINER.getMessage()))));

        Container fetchedContainer1 = allContainers.findByContainerId(CONTAINER_ID_1);
        assertEquals(PATIENT_ID_1, fetchedContainer1.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer1.getStatus());

        Container fetchedContainer2 = allContainers.findByContainerId(CONTAINER_ID_2);
        assertNull(fetchedContainer2.getPatientId());
        assertEquals(ContainerStatus.Open, fetchedContainer2.getStatus());
        verifyZeroInteractions(httpClientService);
    }

    @Test
    public void shouldMapNewContainerWithNewPatient() throws Exception {
        String request = buildMappingRequestFor(PATIENT_ID_1, CONTAINER_ID_1);

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        Container fetchedContainer = allContainers.findByContainerId(CONTAINER_ID_1);
        assertEquals("1", fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer.getStatus());
        assertEquals(SputumTrackingInstance.PreTreatment, fetchedContainer.getMappingInstance());
        verifyMappingReportingEventPublication(fetchedContainer);
    }

    @Test
    public void shouldUnMapContainerFromPreviousPatient_uponMappingWithNewPatient() throws Exception {
        String mapContainer1withPatient1 = buildMappingRequestFor(PATIENT_ID_1, CONTAINER_ID_1);
        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(mapContainer1withPatient1.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());
        Container fetchedContainer1 = allContainers.findByContainerId(CONTAINER_ID_1);
        assertEquals(PATIENT_ID_1, fetchedContainer1.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer1.getStatus());
        verifyMappingReportingEventPublication(fetchedContainer1);


        String mapContainer1withPatient2 = buildMappingRequestFor(PATIENT_ID_2, CONTAINER_ID_1);

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(mapContainer1withPatient2.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        fetchedContainer1 = allContainers.findByContainerId(CONTAINER_ID_1);
        assertEquals(PATIENT_ID_2, fetchedContainer1.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer1.getStatus());
        verifyMappingReportingEventPublication(fetchedContainer1);
    }

    @Test
    public void shouldCreateAdditionalMapping_forSamePatientTreatment_withContainerHavingDifferentInstance() throws Exception {
        String mapContainer1withPatient1 = buildMappingRequestFor(PATIENT_ID_1, CONTAINER_ID_1);
        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(mapContainer1withPatient1.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        String mapContainer2withPatient1 = buildMappingRequestFor(PATIENT_ID_1, CONTAINER_ID_2);

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(mapContainer2withPatient1.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        //Should not UnMap Patient from previous container as it was for a different instance
        Container fetchedContainer1 = allContainers.findByContainerId(CONTAINER_ID_1);
        assertEquals(PATIENT_ID_1, fetchedContainer1.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer1.getStatus());
        verifyMappingReportingEventPublication(fetchedContainer1);

        //Should Map Patient with new container as it is for a different instance
        Container fetchedContainer2 = allContainers.findByContainerId(CONTAINER_ID_2);
        assertEquals(PATIENT_ID_1, fetchedContainer2.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer2.getStatus());
        verifyMappingReportingEventPublication(fetchedContainer2);
    }

    @Test
    public void shouldUnMapPatientFromContainer_forUnMappingRequest() throws Exception {
        container1.mapWith(PATIENT_ID_1, patient1.getCurrentTreatment().getTbId(), SputumTrackingInstance.TwoMonthsIntoCP, new ReasonForContainerClosure(), new LocalDate());
        allContainers.update(container1);

        String unMapContainer1 = buildUnMappingRequestFor(CONTAINER_ID_1);
        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(unMapContainer1.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        Container fetchedContainer1 = allContainers.findByContainerId(CONTAINER_ID_1);
        assertNull(fetchedContainer1.getPatientId());
        assertEquals(ContainerStatus.Open, fetchedContainer1.getStatus());
        verifyMappingReportingEventPublication(fetchedContainer1);
    }

    @Test
    public void shouldReturnErrorCode_forMalformedMappingXmlRequest() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"\" date_modified=\"03/04/2012 11:23:40\" user_id=\"motech\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>patient_mapping</update_type>\n" +
                "<patient_id>" + PATIENT_ID_1 + "</patient_id>\n" +
                "<tb_id>" + PATIENT_ID_1 + "</tb_id>\n" +
                "<smear_sample_instance>Pre-Treatment</smear_sample_instance>\n" +
                "</update>\n" +
                "</case>";

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(containsString("field:case_id:value should not be null"), containsString("field:smear_sample_instance:The value should be one of : [PreTreatment, EndIP, ExtendedIP, TwoMonthsIntoCP, EndTreatment, TreatmentInterruption1, TreatmentInterruption2, TreatmentInterruption3]"))));
        verifyZeroInteractions(httpClientService);
    }

    @Test
    public void shouldReturnSuccessResponse_uponSuccessfulMappingRequestExecution() throws Exception {
        String request = buildMappingRequestFor(PATIENT_ID_1, CONTAINER_ID_1);

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        Container fetchedContainer = allContainers.findByContainerId(CONTAINER_ID_1);
        verifyMappingReportingEventPublication(fetchedContainer);
    }

    @Test
    public void shouldReturnSuccessResponse_forSuccessfulUnMappingRequestExecution() throws Exception {
        String request = buildUnMappingRequestFor(CONTAINER_ID_1);

        standaloneSetup(containerPatientMappingWebService).build().perform(post(CONTAINER_PATIENT_MAPPING_API_URL).body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());

        Container fetchedContainer = allContainers.findByContainerId(CONTAINER_ID_1);
        verifyMappingReportingEventPublication(fetchedContainer);
    }

    private void verifyMappingReportingEventPublication(Container container) {
        ContainerPatientMappingReportingRequest request = new ContainerPatientMappingReportingRequestBuilder().forContainer(container).build();
        if (container.getStatus() == Closed) {
            assertNotNull(request.getClosureDate());
        } else {
            assertNull(request.getClosureDate());
        }
        if(container.getConsultationDate() != null) {
            Assert.assertEquals(container.getConsultationDate().toDate(), request.getConsultationDate());
        } else {
            assertNull(request.getConsultationDate());
        }
        Assert.assertEquals(container.getContainerId(), request.getContainerId());
        if(container.getMappingInstance() != null) {
            Assert.assertEquals(container.getMappingInstance().name(), request.getMappingInstance());
        } else {
            assertNull(request.getMappingInstance());
        }
        Assert.assertEquals(container.getPatientId(), request.getPatientId());
        Assert.assertEquals(container.getReasonForClosure(), request.getReasonForClosure());
        Assert.assertEquals(container.getStatus().name(), request.getStatus());
        Assert.assertEquals(container.getTbId(), request.getTbId());
        verify(httpClientService).post(reportingEventURLs.getContainerPatientMappingLogURL(), request);
    }

    private String buildMappingRequestFor(String patientId, String containerId) {
        return "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"" + containerId + "\" date_modified=\"03/04/2012 11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>patient_mapping</update_type>\n" +
                "<patient_id>" + patientId + "</patient_id>\n" +
                "<tb_id>" + patientId + "</tb_id>\n" +
                "<smear_sample_instance>PreTreatment</smear_sample_instance>\n" +
                "<tb_registration_date>20/11/1986</tb_registration_date>\n" +
                "</update>\n" +
                "</case>";
    }

    private String buildUnMappingRequestFor(String containerId) {
        return "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"" + containerId + "\" date_modified=\"03/04/2012 11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>patient_mapping</update_type>\n" +
                "<patient_id></patient_id>\n" +
                "<tb_id></tb_id>\n" +
                "<smear_sample_instance></smear_sample_instance>\n" +
                "</update>\n" +
                "</case>";
    }
}
