package org.motechproject.whp.refdata.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SmearTestResultTest {

    @Test
    public void shouldSpaceOutIntermediateSmearTestResultsForWordWrapping() {
        assertEquals("Indeterminate / Spoiled / Poor", SmearTestResult.Indeterminate.value());
    }

}
