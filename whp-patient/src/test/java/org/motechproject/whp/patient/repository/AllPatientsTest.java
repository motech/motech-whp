package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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
        allPatients.add(createPatient());

        Patient savedPatient = allPatients.findByPatientId("cha01100001");
        Treatment treatment = savedPatient.getCurrentProvidedTreatment().getTreatment();

        assertNotNull(savedPatient);
        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());
        assertEquals(PatientType.PHSTransfer, savedPatient.getPatientType());

        SmearTestResults smearTestResults = treatment.latestSmearTestResult();
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestResults.getSampleInstance());
        assertEquals(SmearTestResult.Positive, smearTestResults.getResult1());
        assertEquals(DateUtil.today(), smearTestResults.getTestDate1());
    }

    private Patient createPatient() {
        Treatment treatment = new Treatment(TreatmentCategory.Category01, DiseaseClass.P, 200);
        treatment.addSmearTestResult(smearTestResult());
        treatment.addWeightStatistics(weightStatistics());
        allTreatments.add(treatment);

        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.M, PatientType.PHSTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tdId");
        providedTreatment.setPatientAddress(new Address("house number", "landmark", "block", "village", "district", "state"));
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment);
        return patient;
    }

    private SmearTestResults smearTestResult() {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.setSampleInstance(SmearTestSampleInstance.PreTreatment);
        smearTestResults.setTestDate1(DateUtil.today());
        smearTestResults.setResult1(SmearTestResult.Positive);
        smearTestResults.setTestDate2(DateUtil.today());
        smearTestResults.setResult2(SmearTestResult.Positive);
        return smearTestResults;
    }

    private WeightStatistics weightStatistics() {
        return new WeightStatistics(WeightInstance.PreTreatment, 88.0, DateUtil.today());
    }
}
