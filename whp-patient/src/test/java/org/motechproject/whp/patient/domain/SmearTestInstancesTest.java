package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SmearTestInstancesTest {

    @Test
    public void shouldAddANewSmearTestResults_ForPreTreatment() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SmearTestSampleInstance.PreTreatment, null, null, null, null);

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(preTreatmentRecord);

        assertEquals(1, smearTestResults.size());
        assertTrue(smearTestResults.get(0).isOfInstance(SmearTestSampleInstance.PreTreatment));
    }

    @Test
    public void shouldAddSmearTestResults_ForBothPreTreatment_AndEndTreatment() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SmearTestSampleInstance.PreTreatment, null, null, null, null);
        SmearTestRecord endTreatmentRecord = new SmearTestRecord(SmearTestSampleInstance.EndTreatment, null, null, null, null);

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(preTreatmentRecord);
        smearTestResults.add(endTreatmentRecord);

        assertEquals(2, smearTestResults.size());
        assertTrue(smearTestResults.get(0).isOfInstance(SmearTestSampleInstance.PreTreatment));
        assertTrue(smearTestResults.get(1).isOfInstance(SmearTestSampleInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentSmearTestResults_ForPreTreatment() {
        SmearTestRecord oldPreTreatmentRecord = new SmearTestRecord(SmearTestSampleInstance.PreTreatment, new LocalDate(2010, 10, 10), null, null, null);
        SmearTestRecord newPreTreatmentRecord = new SmearTestRecord(SmearTestSampleInstance.PreTreatment, new LocalDate(2012, 12, 12), null, null, null);

        SmearTestResults smearTestResults = new SmearTestResults();

        smearTestResults.add(oldPreTreatmentRecord);
        assertEquals(1, smearTestResults.size());
        assertEquals(oldPreTreatmentRecord, smearTestResults.get(0));

        smearTestResults.add(newPreTreatmentRecord);
        assertEquals(1, smearTestResults.size());
        assertEquals(newPreTreatmentRecord, smearTestResults.get(0));
    }
}
