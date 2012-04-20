package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllPatientsTest extends SpringIntegrationTest {

    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
    }

    @Test
    public void shouldSavePatientInfo() {
        Treatment treatment = new Treatment(Category.Category1, DateUtil.today(), DiseaseClass.P, 200);
        allTreatments.add(treatment);

        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.Male, PatientType.PHSTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tdId", DateUtil.today());
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment);
        allPatients.add(patient);

        Patient patientReturned = allPatients.findByPatientId("cha01100001");
        assertNotNull(patientReturned);
        assertEquals("Raju", patientReturned.getFirstName());
        assertEquals("Singh", patientReturned.getLastName());
        assertEquals(Gender.Male, patientReturned.getGender());
        assertEquals(PatientType.PHSTransfer, patientReturned.getPatientType());
    }

    @Test
    public void shouldSaveSmearTestDetails() {
        Treatment treatment = new Treatment(Category.Category1, DateUtil.today(), DiseaseClass.P, 200);
        treatment.setSmearTestResults(smearTestResults());
        allTreatments.add(treatment);

        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.Male, PatientType.PHSTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tdId", DateUtil.today());
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment);
        allPatients.add(patient);

        Patient patientReturned = allPatients.findByPatientId("cha01100001");
        assertNotNull(patientReturned);

        Treatment treatmentReturned = patientReturned.getProvidedTreatments().get(0).getTreatment();
        SmearTestResult smearTestResult = treatmentReturned.getSmearTestResults().get(0);

        assertEquals(SmearTestSampleInstance.PreTreatment,smearTestResult.getSampleInstance1());
        assertEquals("Passed",smearTestResult.getResult1());
        assertEquals(DateUtil.today(),smearTestResult.getTestDate1());

        SmearTestResult smearTestResult2 = treatmentReturned.getSmearTestResults().get(1);
        assertEquals(SmearTestSampleInstance.TwoMonthsIntoCP,smearTestResult2.getSampleInstance1());
        assertEquals("Passed",smearTestResult2.getResult1());
        assertEquals(DateUtil.today(),smearTestResult2.getTestDate1());

    }

    private List<SmearTestResult> smearTestResults() {
        List<SmearTestResult> smearTestResults = new ArrayList<SmearTestResult>() ;
        smearTestResults.add(smearTestResult1());
        smearTestResults.add(smearTestResult2());
        return smearTestResults;
    }

    private SmearTestResult smearTestResult2() {
        SmearTestResult smearTestResult = new SmearTestResult();
        smearTestResult.setSampleInstance1(SmearTestSampleInstance.TwoMonthsIntoCP);
        smearTestResult.setTestDate1(DateUtil.today());
        smearTestResult.setResult1("Passed");
        return smearTestResult;
    }

    private SmearTestResult smearTestResult1() {
        SmearTestResult smearTestResult = new SmearTestResult();
        smearTestResult.setSampleInstance1(SmearTestSampleInstance.PreTreatment);
        smearTestResult.setTestDate1(DateUtil.today());
        smearTestResult.setResult1("Passed");
        return smearTestResult;
    }
}
