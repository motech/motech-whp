package org.motechproject.whp.webservice.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.webservice.builder.ContainerPatientMappingWebRequestBuilder;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

import static org.joda.time.DateTime.now;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerPatientMappingWebServiceTest extends BaseWebServiceTest{
    private ContainerPatientMappingWebService webService;

    @Mock
    private ContainerService containerService;

    @Before
    public void setup() {
        initMocks(this);
        webService = new ContainerPatientMappingWebService(containerService);
    }

    @Test(expected = WHPCaseException.class)
    public void shouldNotPerformContainerPatientMapping_whenContainerIsNotRegistered() {
        ContainerPatientMappingWebRequest request = new ContainerPatientMappingWebRequestBuilder().
        withCaseId("12345678912")
                .withDateModified(now().toString())
                .withInstance(SampleInstance.PreTreatment.name())
                .withPatientId("patient")
                .withTbId("tbId")
                .build();

        when(containerService.exists(any(String.class))).thenReturn(false);

        webService.updateCase(request);

        expectWHPCaseException(WHPErrorCode.INVALID_CONTAINER_ID);
        verify(containerService, times(1)).exists(any(String.class));

    }
}
