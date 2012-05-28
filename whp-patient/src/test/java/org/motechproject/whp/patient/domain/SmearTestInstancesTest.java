package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SmearTestInstancesTest {

    @Test
    public void shouldAddANewSmearTestResults_ForPreTreatment() {
        SmearTestResults preTreatmentResults = new SmearTestResults(SmearTestSampleInstance.PreTreatment, null, null, null, null);

        SmearTestInstances smearTestInstances = new SmearTestInstances();
        smearTestInstances.add(preTreatmentResults);

        assertEquals(1, smearTestInstances.size());
        assertTrue(smearTestInstances.get(0).isOfInstance(SmearTestSampleInstance.PreTreatment));
    }

    @Test
    public void shouldAddSmearTestResults_ForBothPreTreatment_AndEndTreatment() {
        SmearTestResults preTreatmentResults = new SmearTestResults(SmearTestSampleInstance.PreTreatment, null, null, null, null);
        SmearTestResults endTreatmentResults = new SmearTestResults(SmearTestSampleInstance.EndTreatment, null, null, null, null);

        SmearTestInstances smearTestInstances = new SmearTestInstances();
        smearTestInstances.add(preTreatmentResults);
        smearTestInstances.add(endTreatmentResults);

        assertEquals(2, smearTestInstances.size());
        assertTrue(smearTestInstances.get(0).isOfInstance(SmearTestSampleInstance.PreTreatment));
        assertTrue(smearTestInstances.get(1).isOfInstance(SmearTestSampleInstance.EndTreatment));
    }

    @Test
    public void shouldMaintainHistoryOfExistingTestResults_UponAddingLatestTestResult() {
        SmearTestInstances smearTestInstances = new SmearTestInstances();
        SmearTestResults oldPreTreatmentResults = new SmearTestResults(SmearTestSampleInstance.PreTreatment, new LocalDate(2010, 10, 10), null, null, null);
        SmearTestResults newPreTreatmentResults = new SmearTestResults(SmearTestSampleInstance.PreTreatment, new LocalDate(2012, 12, 12), null, null, null);
        smearTestInstances.add(oldPreTreatmentResults);
        smearTestInstances.add(newPreTreatmentResults);

        assertEquals(oldPreTreatmentResults, smearTestInstances.get(0));
        assertEquals(newPreTreatmentResults, smearTestInstances.get(1));
    }
}
