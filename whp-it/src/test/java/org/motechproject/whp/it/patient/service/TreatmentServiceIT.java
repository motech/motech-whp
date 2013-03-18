package org.motechproject.whp.it.patient.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.DiseaseClass;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.patient.domain.WeightStatisticsRecord;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.webservice.service.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class TreatmentServiceIT extends SpringIntegrationTest {

    private static final String CASE_ID = "TestCaseId";
    private static final String TB_ID = "tb_id";

    @Autowired
    private AllPatients allPatients;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientWebService patientWebService;
    @Autowired
    AllProviders allProviders;
    @Autowired
    private AllDistricts allDistricts;

    private String providerId = "provider-id";
    private District district;

    @Before
    public void setup() {
        district = new District("district");
        allDistricts.add(district);
        createProvider(providerId, "district");
        createTestPatient();
    }

    private void createProvider(String providerId, String district) {
        allProviders.add(new ProviderBuilder()
                .withDefaults()
                .withProviderId(providerId)
                .withDistrict(district)
                .build());
    }

    private void createTestPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withLastModifiedDate("17/03/1990 04:55:50")
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
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientWebService.update(updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientWebService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertTrue(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldMarkPatientAsNotHavingActiveTreatmentOnClose() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientWebService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertFalse(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldMarkPatientAsHavingActiveTreatmentOnTransferIn() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientWebService.update(updatePatientRequest);

        String newProviderId = "new-provider-id";
        createProvider(newProviderId, "newDistrict");

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withProviderId(newProviderId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientWebService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertTrue(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldUpdateDiseaseClassOnTransferIn() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientWebService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertEquals(DiseaseClass.P, updatedPatient.getCurrentTherapy().getDiseaseClass());

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withProviderId(providerId)
                .withDiseaseClass(DiseaseClass.E)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientWebService.update(updatePatientRequest);

        updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertEquals(DiseaseClass.E, updatedPatient.getCurrentTherapy().getDiseaseClass());
    }

    @Test
    public void shouldCaptureNewSmearTestResultsAndWeightStatisticsIfSent() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withProviderId(providerId)
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientWebService.update(updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withProviderId(providerId)
                .withSmearTestResults(SputumTrackingInstance.PreTreatment, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive)
                .withWeightStatistics(SputumTrackingInstance.PreTreatment, 30.00, DateUtil.newDate(2012, 5, 19))
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();
        patientWebService.update(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        SmearTestRecord smearTestRecord = updatedPatient.getSmearTestResults().get(0);
        WeightStatisticsRecord weightStatisticsRecord = updatedPatient.getWeightStatistics().get(0);
        assertEquals(SputumTrackingInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_1());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.newDate(2012, 5, 19), smearTestRecord.getSmear_test_date_2());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_2());

        assertEquals(SputumTrackingInstance.PreTreatment, weightStatisticsRecord.getWeight_instance());
        assertEquals(DateUtil.newDate(2012, 5, 19), weightStatisticsRecord.getMeasuringDate());
        assertEquals(30.00, weightStatisticsRecord.getWeight());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        allProviders.removeAll();
        allDistricts.remove(district);
        super.after();
    }
}
