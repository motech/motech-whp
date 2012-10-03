package org.motechproject.whp.webservice.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.ContainerStatus;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.exception.WHPErrorCode.INVALID_CONTAINER_ID;
import static org.motechproject.whp.common.exception.WHPErrorCode.NO_LAB_RESULTS_IN_CONTAINER;

public class ContainerPatientMappingWebServiceTest extends BaseWebServiceTest {
    private ContainerPatientMappingWebService webService;

    @Mock
    private ContainerService containerService;

    @Mock
    private PatientService patientService;

    @Mock
    private RequestValidator beanValidator;

    @Before
    public void setup() {
        initMocks(this);
        webService = new ContainerPatientMappingWebService(containerService, patientService, beanValidator);
    }

    @Test
    public void shouldThrowWhpCaseException_uponEncounteringValidationFailure() {
        expectWHPCaseException(INVALID_CONTAINER_ID);
        ContainerPatientMappingWebRequest request = buildTestRequest();

        when(containerService.exists(any(String.class))).thenReturn(false);

        webService.updateCase(request);

        verify(containerService, times(1)).exists(anyString());
    }

    @Test
    public void shouldMapNewContainerWithNewPatient() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().setTbId(request.getTb_id());
        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);

        webService.updateCase(request);

        verify(patientService, times(2)).findByPatientId(anyString());
        verify(containerService, times(2)).getContainer(request.getCase_id());

        Container fetchedContainer = containerService.getContainer(request.getCase_id());

        assertEquals(request.getPatient_id(), fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer.getStatus());
        assertEquals(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()), fetchedContainer.getMappingInstance());
    }

    @Test
    public void shouldUnMapContainerFromPreviousPatient_uponMappingWithNewPatient() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        String oldPatientId = "oldPatientId";
        String oldTbId = "oldTbId";
        container.mapWith(oldPatientId, oldTbId, SputumTrackingInstance.TwoMonthsIntoCP);

        Patient patient = new PatientBuilder().withDefaults().build();
        String tbId = request.getTb_id();
        patient.getCurrentTreatment().setTbId(tbId);
        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);

        webService.updateCase(request);

        verify(patientService, times(2)).findByPatientId(anyString());
        verify(containerService, times(2)).getContainer(request.getCase_id());

        Container fetchedContainer = containerService.getContainer(request.getCase_id());
        assertEquals(request.getPatient_id(), fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer.getStatus());
        assertEquals(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()), fetchedContainer.getMappingInstance());
        assertEquals(tbId, fetchedContainer.getTbId());
    }

    @Test
    public void shouldNotUnMapContainerFromPreviousPatient_uponMappingValidationFailure() {
        expectWHPCaseException(NO_LAB_RESULTS_IN_CONTAINER);

        Container container = new Container();
        String oldPatientId = "oldPatientId";
        SputumTrackingInstance instance = SputumTrackingInstance.EndIP;


        ContainerPatientMappingWebRequest request = buildTestRequest();
        container.setContainerId(request.getCase_id());
        String tbId = request.getTb_id();
        container.mapWith(oldPatientId, tbId, instance);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);

        webService.updateCase(request);

        assertEquals(oldPatientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
        assertEquals(instance, container.getMappingInstance());
        assertEquals(tbId, container.getTbId());
    }

    @Test
    public void shouldNotPerformAnyChanges_uponDuplicateMappingRequest() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        String tbId = request.getTb_id();
        SputumTrackingInstance instance = SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance());
        container.mapWith(request.getPatient_id(), tbId, instance);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId(request.getPatient_id());
        patient.getCurrentTreatment().setTbId(tbId);

        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);
        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);
        when(containerService.findByPatientId(request.getPatient_id())).thenReturn(asList(container));

        webService.updateCase(request);

        verify(containerService, never()).update(any(Container.class));
    }

    @Test
    public void shouldCreateAdditionalMapping_forSamePatientTreatment_withContainerHavingDifferentInstance() {
        ContainerPatientMappingWebRequest request = buildTestRequest();

        // Existing Patient-Container mapping
        Container previousContainer = new Container();
        String previousContainerId = "previousContainer";
        previousContainer.setContainerId(previousContainerId);
        SputumTrackingInstance previousContainerInstance = SputumTrackingInstance.EndIP;

        String tbId = request.getTb_id();
        previousContainer.mapWith(request.getPatient_id(), tbId, previousContainerInstance);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId(request.getPatient_id());
        patient.getCurrentTreatment().setTbId(tbId);

        // Newly arrived Patient-Container mapping with different instance
        Container nextContainer = new Container();
        nextContainer.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        nextContainer.setLabResults(labResults);

        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);
        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(previousContainer.getContainerId())).thenReturn(previousContainer);
        when(containerService.getContainer(nextContainer.getContainerId())).thenReturn(nextContainer);
        when(containerService.findByPatientId(request.getPatient_id())).thenReturn(asList(previousContainer));

        webService.updateCase(request);

        //Should not UnMap Patient from previous container as it was for a different instance
        assertSame(request.getPatient_id(), previousContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, previousContainer.getStatus());
        assertEquals(previousContainerInstance, previousContainer.getMappingInstance());
        assertEquals(tbId, previousContainer.getTbId());

        //Should Map Patient with new container as it is for a different instance
        assertEquals(request.getPatient_id(), nextContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, nextContainer.getStatus());
        assertEquals(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()), nextContainer.getMappingInstance());
        assertEquals(tbId, nextContainer.getTbId());
    }

    @Test
    public void shouldUnMapPreviousContainer_andCreateNewMappingForSamePatientTreatment_withContainerHavingSameInstance() {
        ContainerPatientMappingWebRequest request = buildTestRequest();

        // Existing Patient-Container mapping
        Container previousContainer = new Container();
        String previousContainerId = "previousContainer";
        previousContainer.setContainerId(previousContainerId);
        SputumTrackingInstance previousContainerInstance = SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance());

        String tbId = request.getTb_id();
        previousContainer.mapWith(request.getPatient_id(), tbId, previousContainerInstance);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId(request.getPatient_id());
        patient.getCurrentTreatment().setTbId(tbId);

        // Newly arrived Patient-Container mapping with different instance
        Container nextContainer = new Container();
        nextContainer.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        nextContainer.setLabResults(labResults);

        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);
        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(previousContainer.getContainerId())).thenReturn(previousContainer);
        when(containerService.getContainer(nextContainer.getContainerId())).thenReturn(nextContainer);
        when(containerService.findByPatientId(request.getPatient_id())).thenReturn(asList(previousContainer));

        webService.updateCase(request);

        //Should UnMap Patient from previous container as it was for same instance
        assertNotSame(request.getPatient_id(), previousContainer.getPatientId());
        assertEquals(ContainerStatus.Open, previousContainer.getStatus());
        assertNull(previousContainer.getMappingInstance());
        assertNull(previousContainer.getTbId());

        //Should Map Patient with new container
        assertEquals(request.getPatient_id(), nextContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, nextContainer.getStatus());
        assertEquals(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()), nextContainer.getMappingInstance());
        assertEquals(tbId, nextContainer.getTbId());
    }

    @Test
    public void shouldUnMapContainerFromPreviousTreatment_andMapWithNewTreatment_forSamePatient_withContainerHavingDifferentTbId() {
        ContainerPatientMappingWebRequest request = buildTestRequest();

        // Existing Patient-Container mapping for Treatment T1
        Container container = new Container();
        String containerId = request.getCase_id();
        container.setContainerId(containerId);
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        SputumTrackingInstance instance = SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance());

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId(request.getPatient_id());
        String previousTbId = "oldtbid";
        Treatment treatment = new Treatment("providerId", "district", previousTbId, patient.getCurrentTreatment().getPatientType());
        patient.getCurrentTherapy().addTreatment(treatment, now());

        container.mapWith(request.getPatient_id(), previousTbId, instance);

        // New patient-container mapping for Treatment T2
        String tbId = request.getTb_id();
        patient.getCurrentTreatment().setTbId(tbId);

        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);
        when(containerService.exists(containerId)).thenReturn(true);
        when(containerService.getContainer(containerId)).thenReturn(container);
        when(containerService.findByPatientId(request.getPatient_id())).thenReturn(asList(container));

        webService.updateCase(request);

        //Should UnMap container from previous treatment and map with new treatment
        assertEquals(tbId, container.getTbId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
        assertEquals(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()), container.getMappingInstance());
    }

    private ContainerPatientMappingWebRequest buildTestRequest() {
        return new ContainerPatientMappingWebRequestBuilder().
                withCaseId("12345678912")
                .withDateModified(now().toString())
                .withInstance(SampleInstance.PreTreatment.name())
                .withPatientId("patient")
                .withTbId("test")
                .build();
    }
}
