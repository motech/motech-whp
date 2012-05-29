package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.refdata.domain.WeightInstance;

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
        assertTrue(importPatientRequest.hasWeightInstanceRecord(WeightInstance.PreTreatment));
    }

    @Test
    public void hasEndIpWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setEndIpWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(WeightInstance.EndIP));
    }

    @Test
    public void hasExtendedIpWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setExtendedIpWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(WeightInstance.ExtendedIP));
    }

    @Test
    public void hasTwoMonthsIntoCpWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setTwoMonthsIntoCpWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(WeightInstance.TwoMonthsIntoCP));
    }

    @Test
    public void hasEndTreatmentWeightStatisticsWhenWeightDateIsNotNull() {
        importPatientRequest.setEndTreatmentWeightDate("19/11/2011");
        assertTrue(importPatientRequest.hasWeightInstanceRecord(WeightInstance.EndTreatment));
    }

    @Test
    public void doesNotHavePretreatmentWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.PreTreatment));

        importPatientRequest.setPreTreatmentWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.PreTreatment));
    }

    @Test
    public void doesNotHaveEndIpWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.EndIP));

        importPatientRequest.setEndIpWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.EndIP));
    }

    @Test
    public void doesNotHaveExtendedIpWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.ExtendedIP));

        importPatientRequest.setExtendedIpWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.ExtendedIP));
    }

    @Test
    public void doesNotHaveTwoMonthsIntoCpWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.TwoMonthsIntoCP));

        importPatientRequest.setTwoMonthsIntoCpWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.TwoMonthsIntoCP));
    }

    @Test
    public void doesNotHaveEndTreatmentWeightStatisticsWhenWeightDateIsNull() {
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.EndIP));

        importPatientRequest.setEndTreatmentWeightDate("");
        assertFalse(importPatientRequest.hasWeightInstanceRecord(WeightInstance.EndIP));
    }
}
