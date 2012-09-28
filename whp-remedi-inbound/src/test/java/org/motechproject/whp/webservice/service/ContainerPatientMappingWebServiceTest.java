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
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.ContainerStatus;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

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
    public void shouldMapContainerWithNewPatient_uponSuccessfulValidationForMapping() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        container.setInstance(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()));

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
    }

    @Test
    public void shouldUnMapContainerFromPreviousPatient_uponSuccessfulValidationForReMapping() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        container.setInstance(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()));
        String oldPatientId = "oldPatientId";
        container.mapWith(oldPatientId);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().setTbId(request.getTb_id());
        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);

        webService.updateCase(request);

        verify(patientService, times(2)).findByPatientId(anyString());
        verify(containerService, times(2)).getContainer(request.getCase_id());

        Container fetchedContainer = containerService.getContainer(request.getCase_id());
        assertNotSame(oldPatientId, fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer.getStatus());
    }

    @Test
    public void shouldNotUnMapContainerFromPreviousPatient_uponMappingValidationFailure() {
        expectWHPCaseException(NO_LAB_RESULTS_IN_CONTAINER);

        Container container = new Container();
        String oldPatientId = "oldPatientId";
        container.mapWith(oldPatientId);

        ContainerPatientMappingWebRequest request = buildTestRequest();
        container.setContainerId(request.getCase_id());

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);

        webService.updateCase(request);

        assertEquals(oldPatientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
    }

    @Test
    public void shouldUnMapPatientFromPreviousContainer_uponSuccessfulRemappingWithNewContainer() {
        ContainerPatientMappingWebRequest request = buildTestRequest();

        Container previousContainer = new Container();
        String previousContainerId = "previousContainer";
        previousContainer.setContainerId(previousContainerId);
        previousContainer.mapWith(request.getPatient_id());

        Container nextContainer = new Container();
        nextContainer.setContainerId(request.getCase_id());
        nextContainer.setInstance(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()));
        LabResults labResults = new LabResults();
        nextContainer.setLabResults(labResults);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId(request.getPatient_id());
        patient.getCurrentTreatment().setTbId(request.getTb_id());

        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);
        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(nextContainer.getContainerId())).thenReturn(nextContainer);
        when(containerService.getContainer(previousContainer.getContainerId())).thenReturn(previousContainer);
        when(containerService.findByPatientId(request.getPatient_id())).thenReturn(previousContainer);

        webService.updateCase(request);

        //UnMap from previous container
        assertNotSame(request.getPatient_id(), previousContainer.getPatientId());
        assertEquals(ContainerStatus.Open, previousContainer.getStatus());

        //Map with new container
        assertEquals(request.getPatient_id(), nextContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, nextContainer.getStatus());
    }

    @Test
    public void shouldNotPerformAnyChanges_uponDuplicateMappingRequest() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        container.setInstance(SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()));
        container.mapWith(request.getPatient_id());

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId(request.getPatient_id());
        patient.getCurrentTreatment().setTbId(request.getTb_id());

        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);
        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);
        when(containerService.findByPatientId(request.getPatient_id())).thenReturn(container);

        webService.updateCase(request);

        verify(containerService, never()).update(any(Container.class));
    }

    @Test
    public void shouldCreateNewMapping() {
        
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
