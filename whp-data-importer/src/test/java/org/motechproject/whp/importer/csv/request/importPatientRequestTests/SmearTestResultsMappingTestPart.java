package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

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
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.PreTreatment));
    }

    @Test
    public void hasEndIpSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setEndIpSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.EndIP));
    }

    @Test
    public void hasExtendedIpSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setExtendedIpSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.ExtendedIP));
    }

    @Test
    public void hasTwoMonthsIntoCpSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setTwoMonthsIntoCpSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.TwoMonthsIntoCP));
    }

    @Test
    public void hasEndTreatmentSmearTestResultWhenDateIsNotNull() {
        importPatientRequest.setEndTreatmentSmearTestDate1("01/01/2000");
        assertTrue(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.EndTreatment));
    }

    @Test
    public void doesNotHavePretreatmentSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.PreTreatment));

        importPatientRequest.setPreTreatmentSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.PreTreatment));
    }

    @Test
    public void doesNotHaveEndIpSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.EndIP));

        importPatientRequest.setEndIpSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.EndIP));
    }

    @Test
    public void doesNotHaveExtendedIpSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.ExtendedIP));

        importPatientRequest.setExtendedIpSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.ExtendedIP));
    }

    @Test
    public void doesNotHaveTwoMonthsIntoCpSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.TwoMonthsIntoCP));

        importPatientRequest.setTwoMonthsIntoCpSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.TwoMonthsIntoCP));
    }

    @Test
    public void doesNotHaveEndTreatmentSmearTestResultWhenDateIsNull() {
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.EndTreatment));

        importPatientRequest.setEndTreatmentSmearTestDate1("");
        assertFalse(importPatientRequest.hasSmearTestInstanceRecord(SmearTestSampleInstance.EndTreatment));
    }
}
