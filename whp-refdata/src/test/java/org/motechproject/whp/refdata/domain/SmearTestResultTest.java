package org.motechproject.whp.refdata.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.SmearTestResult;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.SmearTestResult.*;

public class SmearTestResultTest {

    @Test
    public void shouldSpaceOutIntermediateSmearTestResultsForWordWrapping() {
        assertEquals("Indeterminate / Spoiled / Poor", SmearTestResult.Indeterminate.value());
    }

    @Test
    public void shouldComputeCumulativeLabResults(){
        assertThat(Positive.cumulativeResult(Negative), is(Positive));
        assertThat(Positive.cumulativeResult(Positive), is(Positive));
        assertThat(Negative.cumulativeResult(Positive), is(Positive));
        assertThat(Negative.cumulativeResult(Negative), is(Negative));
        assertThat(Indeterminate.cumulativeResult(Negative), is(Indeterminate));
        assertThat(Indeterminate.cumulativeResult(Indeterminate), is(Indeterminate));
        assertThat(Positive.cumulativeResult(Indeterminate), is(Positive));
    }

}
