package org.motechproject.whp.it.patient.repository.allPatients;

import org.joda.time.DateTime;
import org.junit.After;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public abstract class AllPatientsTestPart extends SpringIntegrationTest {

    public static final String PROVIDER_DISTRICT ="provider-district";
    @Autowired
    AllPatients allPatients;

    public Patient createPatient(String patientId, String providerId, String providerDistrict) {
        return createPatientOnActiveTreatment(patientId, "Raju", providerId, providerDistrict, now());
    }

    public Patient createPatientOnActiveTreatment(String patientId, String firstName, String providerId, String providerDistrict, DateTime dateModified) {
        Therapy therapy = createTherapy();
        Patient patient = new Patient(patientId, firstName, "Singh", Gender.M, "1234567890");
        Treatment treatment = createTreatment(providerId, providerDistrict);
        patient.addTreatment(treatment, therapy, dateModified, dateModified);
        allPatients.add(patient);
        return patient;
    }

    private Treatment createTreatment(String providerId, String providerDistrict) {
        Treatment treatment = new Treatment(providerId, providerDistrict, "tbId", PatientType.New);
        treatment.setPatientAddress(new Address("house number", "landmark", "block", "village", "district", "state"));
        treatment.addSmearTestResult(smearTestResult());
        treatment.addWeightStatistics(weightStatistics());
        return treatment;
    }

    private Therapy createTherapy() {
        TreatmentCategory treatmentCategory = new TreatmentCategory("cat1", "01", 3, 12, 36, 4, 12, 22, 66, Arrays.asList(DayOfWeek.Monday));
        return new Therapy(treatmentCategory, DiseaseClass.P, 200);
    }

    private WeightStatisticsRecord weightStatistics() {
        return new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, 88.0, DateUtil.today());
    }

    private SmearTestRecord smearTestResult() {
        SmearTestRecord smearTestRecord = new SmearTestRecord();
        smearTestRecord.setSmear_sample_instance(SputumTrackingInstance.PreTreatment);
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
