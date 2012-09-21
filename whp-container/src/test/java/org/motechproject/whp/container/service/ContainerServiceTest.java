package org.motechproject.whp.container.service;

import freemarker.template.TemplateException;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.service.RemediService;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerServiceTest extends BaseUnitTest {
    @Mock
    private AllContainers allContainers;
    @Mock
    private RemediService remediService;
    private ContainerService containerService;

    @Before
    public void setUp() {
        initMocks(this);
        containerService = new ContainerService(allContainers, remediService);
    }

    @Test
    public void shouldRegisterAContainer() throws IOException, TemplateException {
        DateTime creationDate = DateUtil.now();
        mockCurrentDate(creationDate);
        String providerId = "provider_one";
        String containerId = "1234567890";
        SputumTrackingInstance instance = SputumTrackingInstance.IN_TREATMENT;

        containerService.registerContainer(new RegistrationRequest(providerId, containerId, instance.getDisplayText()));

        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).add(captor.capture());
        Container actualContainer = captor.getValue();
        assertEquals(providerId.toLowerCase(), actualContainer.getProviderId());
        assertEquals(containerId, actualContainer.getContainerId());
        assertEquals(creationDate, actualContainer.getCreationDate());
        assertEquals(instance, actualContainer.getInstance());

        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(containerId, providerId, instance, creationDate);
        verify(remediService).sendContainerRegistrationResponse(containerRegistrationModel);
    }

    @Test
    public void shouldReturnWhetherAContainerAlreadyExistsOrNot() {
        String containerId = "containerId";
        when(allContainers.findByContainerId(containerId)).thenReturn(new Container());

        assertTrue(containerService.exists(containerId));
        assertFalse(containerService.exists("non-existent-containerId"));

        verify(allContainers).findByContainerId(containerId);
        verify(allContainers).findByContainerId("non-existent-containerId");
    }

    @Test
    public void shouldGetContainerByContainerId() {
        String containerId = "containerId";
        Container expectedContainer = new Container("providerId", containerId, SputumTrackingInstance.IN_TREATMENT, DateUtil.now());
        when(allContainers.findByContainerId(containerId)).thenReturn(expectedContainer);

        Container container = containerService.getContainer(containerId);

        assertThat(container, is(expectedContainer));
        verify(allContainers).findByContainerId(containerId);
    }

    @Test
    public void shouldUpdateContainer() {
        Container container = new Container("providerId", "containerId", SputumTrackingInstance.IN_TREATMENT, DateUtil.now());

        containerService.update(container);

        verify(allContainers).update(container);
    }

    @After
    public void verifyNoMoreInteractionsOnMocks() {
        verifyNoMoreInteractions(allContainers);
    }
}
