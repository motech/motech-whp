package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class WeightStatisticsRecordsMappingTestPart {

    ImportPatientRequest importPatientRequest;

    @Before
    public void setup() {
        importPatientRequest = new ImportPatientRequest();
    }

    @Test
    public void hasPretreatmentWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setPreTreatmentWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.PreTreatment));
    }

    @Test
    public void hasEndIpWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setEndIpWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.EndIP));
    }

    @Test
    public void hasExtendedIpWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setExtendedIpWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.ExtendedIP));
    }

    @Test
    public void hasTwoMonthsIntoCpWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setTwoMonthsIntoCpWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.TwoMonthsIntoCP));
    }

    @Test
    public void hasEndTreatmentWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setEndTreatmentWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.EndTreatment));
    }

    @Test
    public void doesNotHavePretreatmentWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.PreTreatment));

        importPatientRequest.setPreTreatmentWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.PreTreatment));
    }

    @Test
    public void doesNotHaveEndIpWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.EndIP));

        importPatientRequest.setEndIpWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.EndIP));
    }

    @Test
    public void doesNotHaveExtendedIpWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.ExtendedIP));

        importPatientRequest.setExtendedIpWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.ExtendedIP));
    }

    @Test
    public void doesNotHaveTwoMonthsIntoCpWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.TwoMonthsIntoCP));

        importPatientRequest.setTwoMonthsIntoCpWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.TwoMonthsIntoCP));
    }

    @Test
    public void doesNotHaveEndTreatmentWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.EndIP));

        importPatientRequest.setEndTreatmentWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(SputumTrackingInstance.EndIP));
    }
}
