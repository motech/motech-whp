package org.motechproject.whp.refdata.domain;

import org.junit.Test;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReasonForContainerClosureTest {
    @Test
    public void shouldReturnIfIsTbNegative() {
        assertTrue(new ReasonForContainerClosure("some name", "1").isTbNegative());
        assertFalse(new ReasonForContainerClosure("some other name", "2").isTbNegative());
    }
}
