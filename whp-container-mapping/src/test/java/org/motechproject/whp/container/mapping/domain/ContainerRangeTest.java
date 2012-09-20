package org.motechproject.whp.container.mapping.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainerRangeTest {
    @Test
    public void shouldReturnWhetherGivenIdFallsWithinTheRange() {
        assertTrue(new ContainerRange(1000, 2000).includes(1200));
        assertTrue(new ContainerRange(1000, 2000).includes(1000));
        assertTrue(new ContainerRange(1000, 2000).includes(2000));
        assertFalse(new ContainerRange(1000, 2000).includes(999));
        assertFalse(new ContainerRange(1000, 2000).includes(2001));
    }
}
