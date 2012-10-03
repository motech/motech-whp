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
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

import static junit.framework.Assert.assertEquals;
import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
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
    public void shouldReturnValidationError_whenContainerIsNotRegistered() {
        ContainerPatientMappingWebRequest request = buildTestRequest();

        when(containerService.exists(any(String.class))).thenReturn(false);

        WHPError validationError = validator.validate(request);

        verify(containerService, times(1)).exists(anyString());
        assertEquals(INVALID_CONTAINER_ID, validationError.getErrorCode());

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
        WHPError validationError = validator.validate(request);

        verify(patientService, times(1)).findByPatientId(anyString());
        assertEquals(PATIENT_NOT_FOUND, validationError.getErrorCode());
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
        WHPError validationError = validator.validate(request);

        verify(patientService, times(2)).findByPatientId(anyString());
        assertEquals(NO_SUCH_TREATMENT_EXISTS, validationError.getErrorCode());

    }

    @Test
    public void shouldReturnValidationError_whenContainerDoesNotHaveLabResults() {
        ContainerPatientMappingWebRequest request = buildTestRequest();
        Container container = new Container();
        container.setContainerId(request.getCase_id());

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(containerService.getContainer(anyString())).thenReturn(container);

        WHPError validationError = validator.validate(request);

        verify(containerService, times(1)).getContainer(request.getCase_id());
        assertEquals(NO_LAB_RESULTS_IN_CONTAINER, validationError.getErrorCode());
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
        WHPError validationError = validator.validate(request);

        assertEquals(INVALID_SPUTUM_TEST_INSTANCE, validationError.getErrorCode());
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
}
