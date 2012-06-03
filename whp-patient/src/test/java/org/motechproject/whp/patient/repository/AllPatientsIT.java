package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
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
    AllTherapies allTherapies;

    @Test
    public void shouldSavePatientInfo() {
        createPatient("cha01100001", "providerId");

        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        assertNotNull(savedPatient);
        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());

        SmearTestRecord smearTestRecord = savedPatient.getCurrentTreatment().getSmearTestResults().latestResult();
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.today(), smearTestRecord.getSmear_test_date_1());
    }

    @Test
    public void PatientIdShouldBeCaseInsensitive() {
        createPatient("Cha01100002", "providerId");

        Patient savedPatient = allPatients.findByPatientId("chA01100002");
        assertNotNull(savedPatient);
    }

    @Test
    public void shouldSaveIdsInLowerCase() {
        createPatient("Cha01100002", "providerId");
        Patient savedPatient = allPatients.findByPatientId("chA01100002");
        assertEquals("tbid",savedPatient.getCurrentTreatment().getTbId());
        assertEquals("providerid",savedPatient.getCurrentTreatment().getProviderId());

    }

    @Test
    public void findByPatientIdShouldReturnNullIfKeywordIsNull() {
       assertEquals(null,allPatients.findByPatientId(null));
    }

    @Test
    public void shouldFetchPatientsByCurrentProviderId() {
        Patient requiredPatient = createPatient("patientId1", "providerId1");
        createPatient("patientId2", "providerId2");

        assertPatientEquals(new Patient[]{requiredPatient}, allPatients.findByCurrentProviderId("providerId1").toArray());
    }

    @Test
    public void fetchByCurrentProviderIdShouldReturnEmptyListIfKeywordIsNull() {
       assertEquals(new ArrayList<Patient>(),allPatients.findByCurrentProviderId(null));
    }

    @Test
    public void shouldFetchAllPatientsForAProvider() {
        Patient patient1 = createPatient("patientId1", "providerId1");
        Patient patient2 = createPatient("patientId2", "providerId1");

        assertPatientEquals(new Patient[]{patient1, patient2}, allPatients.findByCurrentProviderId("providerId1").toArray());
    }

    private Patient createPatient(String patientId, String providerId) {
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, 24, 54, Arrays.asList(DayOfWeek.Monday));
        Therapy therapy = new Therapy(treatmentCategory, DiseaseClass.P, 200);
        allTherapies.add(therapy);

        Patient patient = new Patient(patientId, "Raju", "Singh", Gender.M, "1234567890");
        Treatment treatment = new Treatment(providerId, "tbId", PatientType.New);
        treatment.setPatientAddress(new Address("house number", "landmark", "block", "village", "district", "state"));
        treatment.setTherapy(therapy);
        treatment.addSmearTestResult(smearTestResult());
        treatment.addWeightStatistics(weightStatistics());
        patient.addTreatment(treatment, now());

        allPatients.add(patient);
        return patient;
    }

    private SmearTestRecord smearTestResult() {
        SmearTestRecord smearTestRecord = new SmearTestRecord();
        smearTestRecord.setSmear_sample_instance(SmearTestSampleInstance.PreTreatment);
        smearTestRecord.setSmear_test_date_1(DateUtil.today());
        smearTestRecord.setSmear_test_result_1(SmearTestResult.Positive);
        smearTestRecord.setSmear_test_date_2(DateUtil.today());
        smearTestRecord.setSmear_test_result_2(SmearTestResult.Positive);
        return smearTestRecord;
    }

    private WeightStatisticsRecord weightStatistics() {
        return new WeightStatisticsRecord(WeightInstance.PreTreatment, 88.0, DateUtil.today());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTherapies.getAll().toArray());
    }
}
