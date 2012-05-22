package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllPatientsIT extends SpringIntegrationTest {

    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;

    @Test
    public void shouldSavePatientInfo() {
        createPatient("cha01100001", "providerId");

        Patient savedPatient = allPatients.findByPatientId("cha01100001");
        Treatment treatment = savedPatient.getCurrentProvidedTreatment().getTreatment();

        assertNotNull(savedPatient);
        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());
        assertEquals(PatientType.PHCTransfer, savedPatient.getPatientType());

        SmearTestResults smearTestResults = treatment.getSmearTestInstances().latestResult();
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestResults.getSmear_sample_instance());
        assertEquals(SmearTestResult.Positive, smearTestResults.getSmear_test_result_1());
        assertEquals(DateUtil.today(), smearTestResults.getSmear_test_date_1());
    }

    @Test
    public void shouldFetchPatientsByCurrentProviderId() {
        Patient requiredPatient = createPatient("patientId1", "providerId1");
        createPatient("patientId2", "providerId2");

        assertPatientEquals(new Patient[]{requiredPatient}, allPatients.findByCurrentProviderId("providerId1").toArray());
    }

    @Test
    public void shouldFetchAllPatientsForAProvider() {
        Patient patient1 = createPatient("patientId1", "providerId1");
        Patient patient2 = createPatient("patientId2", "providerId1");

        assertPatientEquals(new Patient[]{patient1, patient2}, allPatients.findByCurrentProviderId("providerId1").toArray());
    }

    private Patient createPatient(String patientId, String providerId) {
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, Arrays.asList(DayOfWeek.Monday));
        Treatment treatment = new Treatment(treatmentCategory, DiseaseClass.P, 200);
        treatment.addSmearTestResult(smearTestResult());
        treatment.addWeightStatistics(weightStatistics());
        allTreatments.add(treatment);

        Patient patient = new Patient(patientId, "Raju", "Singh", Gender.M, PatientType.PHCTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment(providerId, "tdId");
        providedTreatment.setPatientAddress(new Address("house number", "landmark", "block", "village", "district", "state"));
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment, now());

        allPatients.add(patient);
        return patient;
    }

    private SmearTestResults smearTestResult() {
        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.setSmear_sample_instance(SmearTestSampleInstance.PreTreatment);
        smearTestResults.setSmear_test_date_1(DateUtil.today());
        smearTestResults.setSmear_test_result_1(SmearTestResult.Positive);
        smearTestResults.setSmear_test_date_2(DateUtil.today());
        smearTestResults.setSmear_test_result_2(SmearTestResult.Positive);
        return smearTestResults;
    }

    private WeightStatistics weightStatistics() {
        return new WeightStatistics(WeightInstance.PreTreatment, 88.0, DateUtil.today());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
    }
}
