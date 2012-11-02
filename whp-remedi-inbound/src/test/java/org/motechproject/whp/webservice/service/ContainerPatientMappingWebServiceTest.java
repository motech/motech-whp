package org.motechproject.whp.webservice.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.motechproject.whp.webservice.validation.ContainerPatientMappingRequestValidator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.exception.WHPErrorCode.INVALID_CONTAINER_ID;

public class ContainerPatientMappingWebServiceTest extends BaseWebServiceTest {
    private ContainerPatientMappingRequestValidator validator;
    private ContainerPatientMappingWebService webService;

    @Mock
    private ContainerService containerService;

    @Mock
    private PatientService patientService;

    @Mock
    private RequestValidator requestValidator;

    @Before
    public void setup() {
        initMocks(this);
        validator = new ContainerPatientMappingRequestValidator(containerService, patientService, requestValidator);
        webService = new ContainerPatientMappingWebService(containerService, validator);
    }

    @Test
    public void shouldThrowWhpCaseException_uponEncounteringValidationFailure() {
        expectWHPCaseException(INVALID_CONTAINER_ID);
        ContainerPatientMappingWebRequest request = buildMappingRequest();

        when(containerService.exists(any(String.class))).thenReturn(false);

        webService.updateCase(request);

        verify(containerService, times(1)).exists(anyString());
    }

    @Test
    public void shouldUnMapContainerForUnMappingRequest() {
        ContainerPatientMappingWebRequest request = buildUnMappingRequest();

        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        container.mapWith("oldPatientId", "oldTbId", SputumTrackingInstance.TwoMonthsIntoCP, mock(ReasonForContainerClosure.class), new LocalDate());

        Patient patient = new PatientBuilder().withDefaults().build();
        String tbId = request.getTb_id();
        patient.getCurrentTreatment().setTbId(tbId);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);

        webService.updateCase(request);

        verify(containerService, times(2)).getContainer(request.getCase_id());

        Container fetchedContainer = containerService.getContainer(request.getCase_id());
        assertNull(fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Open, fetchedContainer.getStatus());
        assertNull(fetchedContainer.getMappingInstance());
        assertNull(fetchedContainer.getTbId());
    }

    @Test
    public void shouldMapContainerWithPatientInstance_forMappingRequest() {
        ReasonForContainerClosure reasonForContainerClosure = new ReasonForContainerClosure("some reason", "0");
        DateTime tbRegistrationDate = new DateTime(1986, 11, 20, 0, 0, 0);

        ContainerPatientMappingWebRequest request = buildMappingRequest();

        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        Patient patient = new PatientBuilder().withDefaults().build();
        String tbId = request.getTb_id();
        patient.getCurrentTreatment().setTbId(tbId);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);
        when(containerService.getClosureReasonForMapping()).thenReturn(reasonForContainerClosure);
        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);

        webService.updateCase(request);

        verify(containerService, times(2)).getContainer(request.getCase_id());
        verify(containerService).getClosureReasonForMapping();

        Container fetchedContainer = containerService.getContainer(request.getCase_id());
        assertEquals(request.getPatient_id(),fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer.getStatus());
        assertEquals(request.getSmear_sample_instance().toLowerCase(), fetchedContainer.getMappingInstance().name().toLowerCase());
        assertEquals(request.getTb_id(),fetchedContainer.getTbId());
        assertEquals(reasonForContainerClosure.getCode(),fetchedContainer.getReasonForClosure());
        assertEquals(tbRegistrationDate.toLocalDate(),fetchedContainer.getConsultationDate());
    }

    @Test
    public void shouldMapContainerWithoutConsultationDateOnInTreatmentPhase_forMappingRequest() {
        ReasonForContainerClosure reasonForContainerClosure = new ReasonForContainerClosure("some reason", "0");
        DateTime tbRegistrationDate = new DateTime(1986, 11, 20, 0, 0, 0);

        ContainerPatientMappingWebRequest request = buildMappingRequest();
        request.setSmear_sample_instance(SputumTrackingInstance.EndIP.name());
        request.setTb_registration_date("");

        Container container = new Container();
        container.setContainerId(request.getCase_id());
        container.setConsultationDate(tbRegistrationDate.toLocalDate());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        Patient patient = new PatientBuilder().withDefaults().build();
        String tbId = request.getTb_id();
        patient.getCurrentTreatment().setTbId(tbId);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(container.getContainerId())).thenReturn(container);
        when(containerService.getClosureReasonForMapping()).thenReturn(reasonForContainerClosure);
        when(patientService.findByPatientId(request.getPatient_id())).thenReturn(patient);

        webService.updateCase(request);

        verify(containerService, times(2)).getContainer(request.getCase_id());
        verify(containerService).getClosureReasonForMapping();

        Container fetchedContainer = containerService.getContainer(request.getCase_id());
        assertEquals(request.getPatient_id(),fetchedContainer.getPatientId());
        assertEquals(ContainerStatus.Closed, fetchedContainer.getStatus());
        assertEquals(request.getSmear_sample_instance(), fetchedContainer.getMappingInstance().name());
        assertEquals(request.getTb_id(),fetchedContainer.getTbId());
        assertEquals(reasonForContainerClosure.getCode(),fetchedContainer.getReasonForClosure());
        assertNull(fetchedContainer.getConsultationDate());
    }

    private ContainerPatientMappingWebRequest buildMappingRequest() {
        return new ContainerPatientMappingWebRequestBuilder().
                withCaseId("12345678912")
                .withDateModified(now().toString())
                .withInstance(SputumTrackingInstance.PreTreatment.name().toLowerCase())
                .withPatientId("patient")
                .withTbId("test")
                .withTbRegistrationDate("20/11/1986")
                .build();
    }

    private ContainerPatientMappingWebRequest buildUnMappingRequest() {
        return new ContainerPatientMappingWebRequestBuilder().
                withCaseId("12345678912")
                .withDateModified(now().toString())
                .withInstance("")
                .withPatientId("")
                .withTbId("")
                .withTbRegistrationDate("")
                .build();
    }
}
