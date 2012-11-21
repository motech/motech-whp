package org.motechproject.whp.remedi.service;

import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.service.CaseLogService;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.service.RemediProperties;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.util.RemediXmlRequestBuilder;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemediServiceTest {
    @Mock
    RemediXmlRequestBuilder remediXmlRequestBuilder;
    @Mock
    HttpClientService httpClientService;
    @Mock
    RemediProperties remediProperties;
    @Mock
    private CaseLogService caseLogService;

    RemediService remediService;
    private final String remediUrl = "remediUrl";

    @Before
    public void setUp() {
        initMocks(this);
        when(remediProperties.getUrl()).thenReturn(remediUrl);
        remediService = new RemediService(httpClientService, caseLogService, remediXmlRequestBuilder, remediProperties);
    }

    @Test
    public void shouldSendContainerRegistrationDetails() throws IOException, TemplateException {
        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel("", "", RegistrationInstance.PreTreatment, DateUtil.now());
        String xmlRequestToBeSent = "xml Request";

        when(remediXmlRequestBuilder.buildTemplatedXmlFor(containerRegistrationModel)).thenReturn(xmlRequestToBeSent);

        remediService.sendContainerRegistrationResponse(containerRegistrationModel);

        verify(httpClientService).post(remediUrl, xmlRequestToBeSent);
    }

    @Test
    public void shouldLogWhileSendingContainerRegistrationDetails() throws IOException, TemplateException {
        String containerId = "containerId";
        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(containerId, "", RegistrationInstance.PreTreatment, DateUtil.now());
        String xmlRequestToBeSent = "xml Request";

        when(remediXmlRequestBuilder.buildTemplatedXmlFor(containerRegistrationModel)).thenReturn(xmlRequestToBeSent);

        remediService.sendContainerRegistrationResponse(containerRegistrationModel);

        InOrder order = inOrder(httpClientService, caseLogService);
        order.verify(httpClientService).post(remediUrl, xmlRequestToBeSent);
        ArgumentCaptor<CaseLog> captor = ArgumentCaptor.forClass(CaseLog.class);
        order.verify(caseLogService).add(captor.capture());
        CaseLog actualLog = captor.getValue();

        assertEquals(containerId, actualLog.getEntityId());
        assertEquals(RemediService.CONTAINER_REGISTRATION_REQUEST_TYPE, actualLog.getRequestType());
        assertEquals(remediUrl, actualLog.getEndpoint());
        assertEquals(xmlRequestToBeSent, actualLog.getRequest());
        assertNotNull(actualLog.getLogDate());
        assertFalse(actualLog.getHasException());
    }
}
