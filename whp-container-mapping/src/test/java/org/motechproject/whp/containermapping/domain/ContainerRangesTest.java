package org.motechproject.whp.containermapping.domain;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ContainerRangesTest {

    @Test
    public void shouldCheckIfContainerIdExistsInARange(){
        ContainerRanges containerRanges = new ContainerRanges();
        containerRanges.add(new ContainerRange(100, 200));
        containerRanges.add(new ContainerRange(300, 400));

        assertTrue(containerRanges.hasContainerId(100));
        assertTrue(containerRanges.hasContainerId(200));
        assertTrue(containerRanges.hasContainerId(150));
        assertTrue(containerRanges.hasContainerId(350));
        assertFalse(containerRanges.hasContainerId(250));
    }
}