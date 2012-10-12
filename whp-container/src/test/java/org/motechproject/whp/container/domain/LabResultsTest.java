package org.motechproject.whp.container.domain;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.motechproject.whp.refdata.domain.SmearTestResult.Negative;
import static org.motechproject.whp.refdata.domain.SmearTestResult.Positive;

public class LabResultsTest {

    @Test
    public void shouldSetCumulativeSmearTestResult(){
        LabResults labResults = new LabResults();
        labResults.setSmearTestResult1(Positive);
        labResults.setSmearTestResult2(Negative);

        labResults.updateCumulativeResult();
        assertThat(labResults.getCumulativeResult(), Is.is(Positive.cumulativeResult(Negative)));
    }
}
