package org.motechproject.whp.patient.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
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
import org.motechproject.whp.refdata.domain.SampleInstance;
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

        patientService.update(updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(updatePatientRequest);

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

        patientService.update(updatePatientRequest);

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

        patientService.update(updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(updatePatientRequest);

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

        patientService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertEquals(DiseaseClass.P, updatedPatient.currentTherapy().getDiseaseClass());

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDiseaseClass(DiseaseClass.E)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(updatePatientRequest);

        updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertEquals(DiseaseClass.E, updatedPatient.currentTherapy().getDiseaseClass());
    }

    @Test
    public void shouldCaptureNewSmearTestResultsAndWeightStatisticsIfSent() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withSmearTestResults(SampleInstance.PreTreatment, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive)
                .withWeightStatistics(SampleInstance.PreTreatment, 30.00, DateUtil.newDate(2012, 5, 19))
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        SmearTestRecord smearTestRecord = updatedPatient.getSmearTestResults().get(0);
        WeightStatisticsRecord weightStatisticsRecord = updatedPatient.getWeightStatistics().get(0);
        assertEquals(SampleInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_1());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_2());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_2());

        assertEquals(SampleInstance.PreTreatment, weightStatisticsRecord.getWeight_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), weightStatisticsRecord.getMeasuringDate());
        assertEquals(30.00, weightStatisticsRecord.getWeight());
    }
//
//    @Test
//    public void shouldTransferInPatient_WhenMinimumRequiredFieldsAreSent() {
//
//        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
//        patientService.createPatient(patientRequest);
//
//        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
//
//        String newProviderId = "newProviderId";
//        String tbId = "newTbId";
//        DateTime now = now();
//
//        treatmentService.transferInPatient(newProviderId, patient, tbId, "" , now, new SmearTestResults(), new WeightStatistics());
//
//        Patient updatedPatient = allPatients.findByPatientId(patient.getPatientId());
//
//        assertEquals(PatientType.TransferredIn, updatedPatient.getCurrentTreatment().getPatientType());
//
//        assertEquals(newProviderId.toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
//        assertEquals(tbId.toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
//        assertEquals("", updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
//        assertEquals(now.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
//        assertEquals(now, updatedPatient.getLastModifiedDate());
//
//        assertTrue(patient.getCurrentTreatment().getSmearTestResults().isEmpty());
//        assertTrue(patient.getCurrentTreatment().getWeightStatistics().isEmpty());
//    }
//
//    @Test
//    public void shouldTransferInPatient_WithNewlySent_SmearTestResultsAndWeightStatistics() {
//
//        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
//        patientService.createPatient(patientRequest);
//
//        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
//
//        String newProviderId = "newProviderId";
//        String tbId = "newTbId";
//        String newTbRegistrationNumber = "newTbRegistrationNumber";
//        DateTime now = now();
//
//        treatmentService.transferInPatient(newProviderId, patient, tbId, newTbRegistrationNumber, now, patientRequest.getSmearTestResults(), patientRequest.getWeightStatistics());
//
//        Patient updatedPatient = allPatients.findByPatientId(patient.getPatientId());
//
//        assertEquals(newProviderId.toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
//        assertEquals(tbId.toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
//        assertEquals(newTbRegistrationNumber, updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
//        assertEquals(now.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
//        assertEquals(now, updatedPatient.getLastModifiedDate());
//        assertEquals(PatientType.TransferredIn, updatedPatient.getCurrentTreatment().getPatientType());
//
//        assertEquals(1, patient.getCurrentTreatment().getSmearTestResults().size());
//        assertEquals(DateUtil.newDate(2010, 5, 19), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_1());
//        assertEquals(DateUtil.newDate(2010, 5, 21), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_2());
//        assertEquals(SmearTestResult.Positive, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_1());
//        assertEquals(SmearTestResult.Positive, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_2());
//        assertEquals(DateUtil.newDate(2010, 5, 19), patient.getCurrentTreatment().getWeightStatistics().latestResult().getMeasuringDate());
//        assertEquals(Double.valueOf(99.7), patient.getCurrentTreatment().getWeightStatistics().latestResult().getWeight());
//    }

    @After
    public void tearDown() {
        markForDeletion(allTherapies.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
        super.after();
    }
}
