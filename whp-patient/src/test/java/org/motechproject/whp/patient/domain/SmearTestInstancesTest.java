package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SmearTestInstancesTest {

    @Test
    public void shouldAddANewSmearTestResults_ForPreTreatment() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, null, null, null, null, null, null);

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(preTreatmentRecord);

        assertEquals(1, smearTestResults.size());
        assertTrue(smearTestResults.get(0).isOfInstance(SputumTrackingInstance.PreTreatment));
    }

    @Test
    public void shouldAddSmearTestResults_ForBothPreTreatment_AndEndTreatment() {
        SmearTestRecord preTreatmentRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, null, null, null, null, null, null);
        SmearTestRecord endTreatmentRecord = new SmearTestRecord(SputumTrackingInstance.EndTreatment, null, null, null, null, null, null);

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(preTreatmentRecord);
        smearTestResults.add(endTreatmentRecord);

        assertEquals(2, smearTestResults.size());
        assertTrue(smearTestResults.get(0).isOfInstance(SputumTrackingInstance.PreTreatment));
        assertTrue(smearTestResults.get(1).isOfInstance(SputumTrackingInstance.EndTreatment));
    }

    @Test
    public void shouldUpdateCurrentSmearTestResults_ForPreTreatment() {
        SmearTestRecord oldPreTreatmentRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, new LocalDate(2010, 10, 10), null, null, null, null, null);
        SmearTestRecord newPreTreatmentRecord = new SmearTestRecord(SputumTrackingInstance.PreTreatment, new LocalDate(2012, 12, 12), null, null, null, null, null);

        SmearTestResults smearTestResults = new SmearTestResults();

        smearTestResults.add(oldPreTreatmentRecord);
        assertEquals(1, smearTestResults.size());
        assertEquals(oldPreTreatmentRecord, smearTestResults.get(0));

        smearTestResults.add(newPreTreatmentRecord);
        assertEquals(1, smearTestResults.size());
        assertEquals(newPreTreatmentRecord, smearTestResults.get(0));
    }
}
