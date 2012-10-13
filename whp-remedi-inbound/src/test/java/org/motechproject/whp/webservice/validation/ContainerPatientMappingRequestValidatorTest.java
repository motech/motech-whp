package org.motechproject.whp.webservice.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.exception.WHPErrorCode.*;

public class ContainerPatientMappingRequestValidatorTest {
    private ContainerPatientMappingRequestValidator validator;

    @Mock
    private ContainerService containerService;

    @Mock
    private PatientService patientService;

    @Mock
    private RequestValidator beanValidator;

    @Before
    public void setup() {
        initMocks(this);
        validator = new ContainerPatientMappingRequestValidator(containerService, patientService, beanValidator);
    }

    @Test
    public void shouldNotReturnValidationError_whenAllMappingInfoIsNull() {
        ContainerPatientMappingWebRequest request = buildUnMappingTestRequest();

        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        when(containerService.exists(any(String.class))).thenReturn(true);
        when(containerService.getContainer(anyString())).thenReturn(container);

        List<WHPError> validationErrors = validator.validate(request);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void shouldReturnValidationError_whenContainerIsNotRegistered() {
        ContainerPatientMappingWebRequest request = buildTestRequest();

        when(containerService.exists(any(String.class))).thenReturn(false);
        when(patientService.findByPatientId(anyString())).thenReturn(null);

        List<WHPError> validationErrors = validator.validate(request);

        assertTrue(validationErrors.contains(new WHPError(INVALID_CONTAINER_ID)));

    }

    @Test
    public void shouldReturnValidationError_whenContainerDoesNotHaveLabResults() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(anyString())).thenReturn(container);

        List<WHPError> validationErrors = validator.validate(request);

        assertTrue(validationErrors.contains(new WHPError(NO_LAB_RESULTS_IN_CONTAINER)));
    }

    @Test
    public void shouldReturnValidationError_whenPatientIsNotRegistered() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(anyString())).thenReturn(container);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(patientService.findByPatientId(anyString())).thenReturn(null);
        List<WHPError> validationErrors = validator.validate(request);

        assertTrue(validationErrors.contains(new WHPError(PATIENT_NOT_FOUND)));
    }

    @Test
    public void shouldReturnValidationError_whenTbIdIsInvalid() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        request.setTb_id("TbId");
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(anyString())).thenReturn(container);

        when(containerService.exists(request.getCase_id())).thenReturn(true);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().setTbId("123");
        when(patientService.findByPatientId(anyString())).thenReturn(patient);
        List<WHPError> validationErrors = validator.validate(request);

        assertTrue(validationErrors.contains(new WHPError(NO_SUCH_TREATMENT_EXISTS)));

    }

    @Test
    public void shouldReturnValidationError_whenSputumTestInstanceIsInvalid() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        request.setSmear_sample_instance(SputumTrackingInstance.InTreatment.name());
        Container container = new Container();
        container.setContainerId(request.getCase_id());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(anyString())).thenReturn(container);

        when(containerService.exists(request.getCase_id())).thenReturn(true);

        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().setTbId(request.getTb_id());
        when(patientService.findByPatientId(anyString())).thenReturn(patient);
        List<WHPError> validationErrors = validator.validate(request);

        assertTrue(validationErrors.contains(new WHPError(INVALID_SPUTUM_TEST_INSTANCE)));
    }

    private ContainerPatientMappingWebRequest buildTestRequest() {
        return new ContainerPatientMappingWebRequestBuilder().
                withCaseId("12345678912")
                .withDateModified(now().toString())
                .withInstance(SampleInstance.PreTreatment.name())
                .withPatientId("patient")
                .withTbId("tbid")
                .build();
    }

    private ContainerPatientMappingWebRequest buildUnMappingTestRequest() {
        return new ContainerPatientMappingWebRequestBuilder().
                withCaseId("12345678912")
                .withDateModified(now().toString())
                .withInstance("")
                .withPatientId("")
                .withTbId("")
                .build();
    }
}
