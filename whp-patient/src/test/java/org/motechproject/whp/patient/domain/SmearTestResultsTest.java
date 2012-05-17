package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmearTestResultsTest {

    @Test
    public void checkResultForSmearTestInstance() {
        SmearTestResults preTreatmentResults = new SmearTestResults(SmearTestSampleInstance.PreTreatment, null, null, null, null);

        assertFalse(preTreatmentResults.isOfInstance(SmearTestSampleInstance.EndTreatment));
        assertFalse(preTreatmentResults.isOfInstance(SmearTestSampleInstance.ExtendedIP));
        assertTrue(preTreatmentResults.isOfInstance(SmearTestSampleInstance.PreTreatment));
    }
}
