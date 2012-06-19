package org.motechproject.whp.patient.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.common.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.patient.domain.WeightStatisticsRecord;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.WeightInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class TreatmentServiceIT extends SpringIntegrationTest {

    private static final String CASE_ID = "TestCaseId";
    private static final String TB_ID = "tb_id";

    @Autowired
    private AllTherapies allTherapies;
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private TreatmentService treatmentService;
    @Autowired
    private PatientService patientService;

    @Before
    public void setup() {
        createTestPatient();
    }

    private void createTestPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(CASE_ID)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withTbId(TB_ID)
                .build();
        patientService.createPatient(patientRequest);
    }

    @Test
    public void shouldCreateActiveTreatmentForPatientByDefault() {
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertTrue(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldCreateActiveTreatmentForPatientOnOpen() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.closeTreatment, updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.openTreatment, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertTrue(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldMarkPatientAsNotHavingActiveTreatmentOnClose() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.closeTreatment, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertFalse(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldMarkPatientAsHavingActiveTreatmentOnTransferIn() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.closeTreatment, updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.transferIn, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertTrue(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldUpdateDiseaseClassOnTransferIn() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.closeTreatment, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertEquals(DiseaseClass.P, updatedPatient.latestTherapy().getDiseaseClass());

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDiseaseClass(DiseaseClass.E)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.transferIn, updatePatientRequest);

        updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertEquals(DiseaseClass.E, updatedPatient.latestTherapy().getDiseaseClass());
    }

    @Test
    public void shouldCaptureNewSmearTestResultsAndWeightStatisticsIfSent() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(UpdateScope.closeTreatment, updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withSmearTestResults(SmearTestSampleInstance.PreTreatment, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive)
                .withWeightStatistics(WeightInstance.PreTreatment, 30.00, DateUtil.newDate(2012, 5, 19))
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(UpdateScope.transferIn, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        SmearTestRecord smearTestRecord = updatedPatient.getSmearTestResults().get(0);
        WeightStatisticsRecord weightStatisticsRecord = updatedPatient.getWeightStatistics().get(0);
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_1());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_2());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_2());

        assertEquals(WeightInstance.PreTreatment, weightStatisticsRecord.getWeight_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), weightStatisticsRecord.getMeasuringDate());
        assertEquals(30.00, weightStatisticsRecord.getWeight());
    }

    @Test
    public void shouldUpdateWithOldTreatmentSmearTestResultsAndWeightStatisticsIfNotSent() {
        Patient patient = allPatients.findByPatientId(CASE_ID);
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withSmearTestResults(SmearTestSampleInstance.PreTreatment, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive)
                .withWeightStatistics(WeightInstance.PreTreatment, 30.00, DateUtil.newDate(2012, 5, 19))
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(UpdateScope.simpleUpdate, updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(UpdateScope.closeTreatment, updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(UpdateScope.transferIn, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        SmearTestRecord smearTestRecord = updatedPatient.getSmearTestResults().get(0);
        WeightStatisticsRecord weightStatisticsRecord = updatedPatient.getWeightStatistics().get(0);
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_1());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_2());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_2());

        assertEquals(WeightInstance.PreTreatment, weightStatisticsRecord.getWeight_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), weightStatisticsRecord.getMeasuringDate());
        assertEquals(30.00, weightStatisticsRecord.getWeight());
    }

    @After
    public void tearDown() {
        markForDeletion(allTherapies.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
        super.after();
    }
}
