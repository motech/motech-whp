package org.motechproject.whp.patient.repository.allPatients;

import org.junit.After;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public abstract class AllPatientsTestPart extends SpringIntegrationTest {

    @Autowired
    AllPatients allPatients;

    public Patient createPatient(String patientId, String providerId) {
        return createPatientOnActiveTreatment(patientId, "Raju", providerId);
    }

    public Patient createPatientOnActiveTreatment(String patientId, String firstName, String providerId) {
        Therapy therapy = createTherapy();
        Patient patient = new Patient(patientId, firstName, "Singh", Gender.M, "1234567890");
        Treatment treatment = createTreatment(providerId);
        patient.addTreatment(treatment, therapy, now());
        allPatients.add(patient);
        return patient;
    }

    private Treatment createTreatment(String providerId) {
        Treatment treatment = new Treatment(providerId, "tbId", PatientType.New);
        treatment.setPatientAddress(new Address("house number", "landmark", "block", "village", "district", "state"));
        treatment.addSmearTestResult(smearTestResult());
        treatment.addWeightStatistics(weightStatistics());
        return treatment;
    }

    private Therapy createTherapy() {
        TreatmentCategory treatmentCategory = new TreatmentCategory("cat1", "01", 3, 12, 36, 4, 12, 22, 66, Arrays.asList(DayOfWeek.Monday));
        Therapy therapy = new Therapy(treatmentCategory, DiseaseClass.P, 200);
        return therapy;
    }

    private WeightStatisticsRecord weightStatistics() {
        return new WeightStatisticsRecord(SampleInstance.PreTreatment, 88.0, DateUtil.today());
    }

    private SmearTestRecord smearTestResult() {
        SmearTestRecord smearTestRecord = new SmearTestRecord();
        smearTestRecord.setSmear_sample_instance(SampleInstance.PreTreatment);
        test1(smearTestRecord);
        test2(smearTestRecord);
        return smearTestRecord;
    }

    private void test1(SmearTestRecord smearTestRecord) {
        smearTestRecord.setSmear_test_date_1(DateUtil.today());
        smearTestRecord.setSmear_test_result_1(SmearTestResult.Positive);
    }

    private void test2(SmearTestRecord smearTestRecord) {
        smearTestRecord.setSmear_test_date_2(DateUtil.today());
        smearTestRecord.setSmear_test_result_2(SmearTestResult.Positive);
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
    }
}
