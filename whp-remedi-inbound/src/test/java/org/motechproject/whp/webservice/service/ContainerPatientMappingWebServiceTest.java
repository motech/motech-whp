package org.motechproject.whp.webservice.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerPatientMappingWebServiceTest extends BaseWebServiceTest{
    private ContainerPatientMappingWebService webService;

    @Mock
    private ContainerService containerService;

    @Mock
    private PatientService patientService;

    @Before
    public void setup() {
        initMocks(this);
        webService = new ContainerPatientMappingWebService(containerService, patientService);
    }

    @Test
    public void shouldNotPerformContainerPatientMapping_whenContainerIsNotRegistered() {
        expectWHPCaseException(WHPErrorCode.INVALID_CONTAINER_ID);
        ContainerPatientMappingWebRequest request = buildTestRequest();

        when(containerService.exists(any(String.class))).thenReturn(false);

        webService.updateCase(request);

        verify(containerService, times(1)).exists(anyString());

    }

    @Test
    public void shouldNotPerformContainerPatientMapping_whenPatientIsNotRegistered() {
        expectWHPCaseException(WHPErrorCode.PATIENT_NOT_FOUND);
        ContainerPatientMappingWebRequest request = buildTestRequest();

        when(containerService.exists(request.getCase_id())).thenReturn(true);
        when(patientService.findByPatientId(anyString())).thenReturn(null);
        webService.updateCase(request);

        verify(patientService,times(1)).findByPatientId(anyString());

    }

    @Test
    public void shouldNotPerformContainerPatientMapping_whenTbIdIsInvalid() {
        expectWHPCaseException(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
        ContainerPatientMappingWebRequest request = buildTestRequest();

        when(containerService.exists(request.getCase_id())).thenReturn(true);

        Patient patient = new PatientBuilder().withDefaults().build();
        when(patientService.findByPatientId(anyString())).thenReturn(patient);
        webService.updateCase(request);

        verify(patientService, times(1)).findByPatientId(anyString());

    }

    private ContainerPatientMappingWebRequest buildTestRequest() {
        return new ContainerPatientMappingWebRequestBuilder().
            withCaseId("12345678912")
                    .withDateModified(now().toString())
                    .withInstance(SampleInstance.PreTreatment.name())
                    .withPatientId("patient")
                    .withTbId("tbId")
                    .build();
    }
}
