package org.motechproject.whp.container.it;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationContainerContext.xml")
public class AllContainersIT extends SpringIntegrationTest {
    @Autowired
    AllContainers allContainers;
    private Container container;

    private void addAndMarkForDeletion(Container container) {
        allContainers.add(container);
        markForDeletion(container);
    }

    @Before
    public void setUp() {
        container = new Container("P00001", "1234567890", "pre-treatment");
    }

    @Test
    public void shouldSaveContainerInfo() {
        addAndMarkForDeletion(container);
        Container containerReturned = allContainers.findByContainerId("1234567890");

        assertNotNull(containerReturned);
        assertEquals("1234567890", containerReturned.getContainerId());
        assertEquals("P00001", containerReturned.getProviderId());
        assertEquals("pre-treatment", containerReturned.getInstance());
    }
}