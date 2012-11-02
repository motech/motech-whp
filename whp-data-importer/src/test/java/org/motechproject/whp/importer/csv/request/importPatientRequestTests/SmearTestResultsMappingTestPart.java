package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class SmearTestResultsMappingTestPart {

    ImportPatientRequest importPatientRequest;

    @Before
    public void setup() {
        importPatientRequest = new ImportPatientRequest();
    }

    @Test
    public void hasPretreatmentSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setPreTreatmentSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.PreTreatment));
    }

    @Test
    public void hasEndIpSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setEndIpSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.EndIP));
    }

    @Test
    public void hasExtendedIpSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setExtendedIpSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.ExtendedIP));
    }

    @Test
    public void hasTwoMonthsIntoCpSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setTwoMonthsIntoCpSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.TwoMonthsIntoCP));
    }

    @Test
    public void hasEndTreatmentSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setEndTreatmentSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.EndTreatment));
    }

    @Test
    public void doesNotHavePretreatmentSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.PreTreatment));

        importPatientRequest.setPreTreatmentSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.PreTreatment));
    }

    @Test
    public void doesNotHaveEndIpSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.EndIP));

        importPatientRequest.setEndIpSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.EndIP));
    }

    @Test
    public void doesNotHaveExtendedIpSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.ExtendedIP));

        importPatientRequest.setExtendedIpSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.ExtendedIP));
    }

    @Test
    public void doesNotHaveTwoMonthsIntoCpSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.TwoMonthsIntoCP));

        importPatientRequest.setTwoMonthsIntoCpSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.TwoMonthsIntoCP));
    }

    @Test
    public void doesNotHaveEndTreatmentSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.EndTreatment));

        importPatientRequest.setEndTreatmentSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SputumTrackingInstance.EndTreatment));
    }
}
