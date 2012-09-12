package org.motechproject.whp.container.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.Instance;
import org.motechproject.whp.container.repository.AllContainers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerServiceTest {
    @Mock
    private AllContainers allContainers;
    private ContainerService containerService;

    @Before
    public void setUp() {
        initMocks(this);
        containerService = new ContainerService(allContainers);
    }

    @Test
    public void shouldRegisterAContainer() {
        String providerId = "provider_one";
        String containerId = "1234567890";
        String instance = Instance.IN_TREATMENT.getDisplayText();

        containerService.registerContainer(new RegistrationRequest(providerId, containerId, instance));

        ArgumentCaptor<Container> captor = ArgumentCaptor.forClass(Container.class);
        verify(allContainers).add(captor.capture());
        Container actualContainer = captor.getValue();

        assertEquals(providerId, actualContainer.getProviderId());
        assertEquals(containerId, actualContainer.getContainerId());
        assertEquals(instance, actualContainer.getInstance());
    }

    @Test
    public void shouldReturnWhetherAContainerAlreadyExistsOrNot() {
        String containerId = "containerId";
        when(allContainers.findByContainerId(containerId)).thenReturn(new Container());

        assertTrue(containerService.exists(containerId));
        assertFalse(containerService.exists("non-existent-containerId"));

        verify(allContainers).findByContainerId(containerId);
    }
}
