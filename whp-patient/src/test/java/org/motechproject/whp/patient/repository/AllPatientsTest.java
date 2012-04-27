package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
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
        Treatment treatment = new Treatment(TreatmentCategory.Category01, DiseaseClass.P, 200);
        allTreatments.add(treatment);

        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.M, PatientType.PHSTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tdId");
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment);
        allPatients.add(patient);

        Patient patientReturned = allPatients.findByPatientId("cha01100001");
        assertNotNull(patientReturned);
        assertEquals("Raju", patientReturned.getFirstName());
        assertEquals("Singh", patientReturned.getLastName());
        assertEquals(Gender.M, patientReturned.getGender());
        assertEquals(PatientType.PHSTransfer, patientReturned.getPatientType());
    }

    @Test
    public void shouldSaveSmearTestDetails() {
        Treatment treatment = new Treatment(TreatmentCategory.Category01, DiseaseClass.P, 200);
        treatment.setSmearTestResults(smearTestResults());
        allTreatments.add(treatment);

        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.M, PatientType.PHSTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tdId");
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment);
        allPatients.add(patient);

        Patient patientReturned = allPatients.findByPatientId("cha01100001");
        assertNotNull(patientReturned);

        Treatment treatmentReturned = patientReturned.getCurrentProvidedTreatment().getTreatment();

        SmearTestResults smearTestResults1 = treatmentReturned.getSmearTestResults().get(0);
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestResults1.getSampleInstance());
        assertEquals(SmearTestResult.Positive, smearTestResults1.getResult1());
        assertEquals(DateUtil.today(), smearTestResults1.getTestDate1());

        SmearTestResults smearTestResults2 = treatmentReturned.getSmearTestResults().get(1);
        assertEquals(SmearTestSampleInstance.TwoMonthsIntoCP, smearTestResults2.getSampleInstance());
        assertEquals(SmearTestResult.Positive, smearTestResults2.getResult1());
        assertEquals(DateUtil.today(), smearTestResults2.getTestDate1());
    }

    private List<SmearTestResults> smearTestResults() {
        List<SmearTestResults> smearTestResults = new ArrayList<SmearTestResults>();
        smearTestResults.add(smearTestResult1());
        smearTestResults.add(smearTestResult2());
        return smearTestResults;
    }

    private SmearTestResults smearTestResult2() {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.setSampleInstance(SmearTestSampleInstance.TwoMonthsIntoCP);
        smearTestResults.setTestDate1(DateUtil.today());
        smearTestResults.setResult1(SmearTestResult.Positive);
        return smearTestResults;
    }

    private SmearTestResults smearTestResult1() {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.setSampleInstance(SmearTestSampleInstance.PreTreatment);
        smearTestResults.setTestDate1(DateUtil.today());
        smearTestResults.setResult1(SmearTestResult.Positive);
        return smearTestResults;
    }
}
