package org.motechproject.whp.container.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ContainerIdTest {

    @Test
    public void shouldCreateContainerIdFromUserIdAndContainerIdSuffixForProviderMode() {
        ContainerId containerId = new ContainerId("providerId", "00001", ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
        assertEquals(containerId.value(), "sproviderId00001");
    }

    @Test
    public void shouldUseGivenContainerIdForNewContainerMode() {
        ContainerId containerId = new ContainerId("providerId", "12345678901", ContainerRegistrationMode.NEW_CONTAINER);
        assertEquals(containerId.value(), "12345678901");
    }
}
