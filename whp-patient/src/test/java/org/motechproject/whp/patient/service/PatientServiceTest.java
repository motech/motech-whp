package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class PatientServiceTest extends SpringIntegrationTest {
    public static final String CASE_ID = "123456";

    @Autowired
    private AllTreatments allTreatments;
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private UpdateCommandFactory commandFactory;

    PatientService patientService;

    @Before
    public void setUp() {
        super.before();
        patientService = new PatientService(allPatients, allTreatments, commandFactory);
    }

    @After
    public void tearDown() {
        markForDeletion(allTreatments.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
        super.after();
    }

    @Test
    public void shouldUpdatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(CASE_ID)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(66)
                .withTbId("elevenDigit")
                .withCaseId(CASE_ID)
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        Treatment treatment = updatedPatient.latestTreatment();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getDate_modified(), updatedPatient.getLastModifiedDate());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentProvidedTreatment().getPatientAddress());
        assertEquals(updatePatientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(updatePatientRequest.getSmearTestResults(), updatedPatient.getCurrentProvidedTreatment().getSmearTestInstances().latestResult());
        assertEquals(updatePatientRequest.getWeightStatistics(), updatedPatient.getCurrentProvidedTreatment().getWeightInstances().latestResult());
        assertEquals((Integer) 66, treatment.getPatientAge());
    }

    @Test
    public void shouldUpdateOnlyTheSpecifiedFieldsOnPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(CASE_ID)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);
        Patient patient = allPatients.findByPatientId(CASE_ID);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withPatientInfo(CASE_ID, "newFirstName", "newLastName", null, null, "9087654321", null)
                .withTbRegistrationNumber("newTbRegNum")
                .withTbId("elevenDigit")
                .build();

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        Treatment treatment = updatedPatient.latestTreatment();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertNotSame("newFirstName", updatedPatient.getFirstName());
        assertNotSame("newLastName", updatedPatient.getLastName());
        assertEquals(patient.getCurrentProvidedTreatment().getPatientAddress(), updatedPatient.getCurrentProvidedTreatment().getPatientAddress());
        assertEquals(patient.getCurrentProvidedTreatment().getSmearTestInstances().latestResult(), updatedPatient.getCurrentProvidedTreatment().getSmearTestInstances().latestResult());
        assertEquals(patient.getCurrentProvidedTreatment().getWeightInstances().latestResult(), updatedPatient.getCurrentProvidedTreatment().getWeightInstances().latestResult());
    }

    @Test
    public void shouldThrowExceptionWhenSimpleUpdateOnPatientIsTriedWithWrongTbId() {
        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("tbId")
                .withCaseId(CASE_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID).withTbId("wrongTbId").build();

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidSmearTestResults() {
        expectWHPRuntimeException(WHPErrorCode.NULL_VALUE_IN_SMEAR_TEST_RESULTS);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("elevenDigit")
                .withCaseId(CASE_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID)
                .withSmearTestResults(SmearTestSampleInstance.PreTreatment, null, null, null, null)
                .withTbId("elevenDigit")
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidWeightStatistics() {
        expectWHPRuntimeException(WHPErrorCode.NULL_VALUE_IN_WEIGHT_STATISTICS);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("elevenDigit")
                .withCaseId(CASE_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID)
                .withWeightStatistics(null, 100.0, DateUtil.tomorrow())
                .withTbId("elevenDigit")
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientWithGivenCaseIdDoesNotExist() {
        expectWHPRuntimeException(WHPErrorCode.CASE_ID_DOES_NOT_EXIST);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId("invalidCaseId").build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionForTreatmentUpdateWhenPatientWithGivenCaseIdDoesNotExist() {
        expectWHPRuntimeException(WHPErrorCode.CASE_ID_DOES_NOT_EXIST);
        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForOpenNewTreatment().withCaseId("invalidCaseId").build();
        commandFactory.updateFor(UpdateScope.openTreatment).apply(patientRequest);
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseTbIdIsWrong() {
        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(patientRequest);
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseItIsAlreadyClosed() {
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        //first properly closing treatment
        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(patientRequest);

        PatientRequest wrongPatientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build();
        //now trying to close a closed treatment
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(wrongPatientRequest);
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseTbIdIsWrongAndTreatmentIsAlreadyClosed() {
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        //first properly closing treatment
        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(patientRequest);

        PatientRequest wrongPatientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();

        //now trying to close a closed treatment, that too with wrong tbId
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(wrongPatientRequest);
    }

    @Test
    public void shouldThrowExceptionIfNewTreatmentCannotBeOpenedBecauseCurrentTreatmentIsNotClosed() {
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForOpenNewTreatment().withTbId("tbId").build();
        commandFactory.updateFor(UpdateScope.openTreatment).apply(patientRequest);
    }

    @Test
    public void shouldCloseCurrentTreatmentForPatient() {
        String caseId = "caseId";
        DateTime lastModifiedDate = DateUtil.newDateTime(1990, 3, 17, 4, 55, 50);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(lastModifiedDate)
                .withCaseId(caseId)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        PatientRequest closeTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(lastModifiedDate)
                .build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(closeTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentClosed(updatedPatient, lastModifiedDate);
    }

    @Test
    public void shouldOpenNewTreatmentForPatient() {
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        PatientRequest closeTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(closeTreatmentUpdateRequest);

        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .build();
        commandFactory.updateFor(UpdateScope.openTreatment).apply(openNewTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentIsNew(updatedPatient, openNewTreatmentUpdateRequest);
    }

    @Test
    public void shouldPauseAndRestartForPatient() {
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        PatientRequest pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        commandFactory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        Patient pausedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentPaused(pausedPatient, pauseTreatmentRequest);
        assertTrue(pausedPatient.isCurrentTreatmentPaused());

        PatientRequest restartTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        commandFactory.updateFor(UpdateScope.restartTreatment).apply(restartTreatmentRequest);

        Patient restartedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentNotPaused(restartedPatient, restartTreatmentRequest);
    }

    private void assertCurrentTreatmentIsNew(Patient updatedPatient, PatientRequest patientRequest) {
        ProvidedTreatment currentProvidedTreatment = updatedPatient.getCurrentProvidedTreatment();
        assertEquals(patientRequest.getDate_modified().toLocalDate(), currentProvidedTreatment.getStartDate());
        assertEquals(patientRequest.getTb_id(), currentProvidedTreatment.getTbId());
        assertEquals(patientRequest.getTreatment_category(), updatedPatient.latestTreatment().getTreatmentCategory());
        assertEquals(patientRequest.getDisease_class(), updatedPatient.latestTreatment().getDiseaseClass());
        assertEquals(patientRequest.getProvider_id(), currentProvidedTreatment.getProviderId());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentClosed(Patient updatedPatient, DateTime lastModifiedDate) {
        ProvidedTreatment currentProvidedTreatment = updatedPatient.getCurrentProvidedTreatment();
        assertEquals(lastModifiedDate.toLocalDate(), updatedPatient.latestTreatment().getCloseDate());
        assertEquals(lastModifiedDate.toLocalDate(), currentProvidedTreatment.getEndDate());
        assertEquals(TreatmentOutcome.Cured, updatedPatient.getTreatmentOutcome());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentPaused(Patient pausedPatient, PatientRequest pauseTreatmentRequest) {
        assertTrue(pausedPatient.isCurrentTreatmentPaused());
        assertEquals(pauseTreatmentRequest.getDate_modified(), pausedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentNotPaused(Patient pausedPatient, PatientRequest pauseTreatmentRequest) {
        assertFalse(pausedPatient.isCurrentTreatmentPaused());
        assertEquals(pauseTreatmentRequest.getDate_modified(), pausedPatient.getLastModifiedDate());
    }

}
