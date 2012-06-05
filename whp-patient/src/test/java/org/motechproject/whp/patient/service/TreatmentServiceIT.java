package org.motechproject.whp.patient.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
    public void shouldMarkPatientAsNotHavingActiveTreatmentOnPause() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.pauseTreatment, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertFalse(updatedPatient.isOnActiveTreatment());
    }

    @Test
    public void shouldMarkPatientAsHavingActiveTreatmentOnRestart() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.pauseTreatment, updatePatientRequest);

        updatePatientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withCaseId(CASE_ID)
                .withTbId(TB_ID)
                .build();

        patientService.update(UpdateScope.restartTreatment, updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        assertTrue(updatedPatient.isOnActiveTreatment());
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

    @After
    public void tearDown() {
        markForDeletion(allTherapies.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
        super.after();
    }
}
