package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.today;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class PatientServiceIT extends SpringIntegrationTest {

    public static final String CASE_ID = "123456";
    @Autowired
    private AllTherapies allTherapies;
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private UpdateCommandFactory commandFactory;

    PatientService patientService;

    @Before
    public void setUp() {
        super.before();
        patientService = new PatientService(allPatients, allTherapies, commandFactory);
    }

    @After
    public void tearDown() {
        markForDeletion(allTherapies.getAll().toArray());
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
        Therapy therapy = updatedPatient.latestTherapy();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getDate_modified(), updatedPatient.getLastModifiedDate());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentTreatment().getPatientAddress());
        assertEquals(updatePatientRequest.getTb_registration_number(), updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        verifySmearTestResultsUpdate(updatePatientRequest, updatedPatient);
        verifyWeightStatisticsUpdate(updatePatientRequest, updatedPatient);
        assertEquals((Integer) 66, therapy.getPatientAge());
    }

    private void verifyWeightStatisticsUpdate(PatientRequest updatePatientRequest, Patient updatedPatient) {
        WeightStatistics updateRequests = updatePatientRequest.getWeightStatistics();
        WeightStatistics weightStatistics = updatedPatient.getCurrentTreatment().getWeightStatistics();
        assertTrue(weightStatistics.getAll().containsAll(updateRequests.getAll()));
    }

    private void verifySmearTestResultsUpdate(PatientRequest updatePatientRequest, Patient updatedPatient) {
        SmearTestResults updateRequests = updatePatientRequest.getSmearTestResults();
        SmearTestResults smearTestResults = updatedPatient.getCurrentTreatment().getSmearTestResults();
        assertTrue(smearTestResults.getAll().containsAll(updateRequests.getAll()));
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
        Therapy therapy = updatedPatient.latestTherapy();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getTb_registration_number(), updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        assertNotSame("newFirstName", updatedPatient.getFirstName());
        assertNotSame("newLastName", updatedPatient.getLastName());
        assertEquals(patient.getCurrentTreatment().getPatientAddress(), updatedPatient.getCurrentTreatment().getPatientAddress());
        assertEquals(patient.getCurrentTreatment().getSmearTestResults().latestResult(), updatedPatient.getCurrentTreatment().getSmearTestResults().latestResult());
        assertEquals(patient.getCurrentTreatment().getWeightStatistics().latestResult(), updatedPatient.getCurrentTreatment().getWeightStatistics().latestResult());
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
    public void shouldMarkPatientAsHavingActiveTreatment() {
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(caseId);
        assertTrue(patient.isOnActiveTreatment());
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseTbIdIsWrongAndTreatmentIsAlreadyClosed() {
        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
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

    @Test
    public void patientCanBeTransferredToAnotherProvider_WhenHeWasTransferredOutForPreviousTreatment() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        PatientRequest closeRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTreatmentOutcome(TreatmentOutcome.TransferredOut).build();
        patientService.update(UpdateScope.closeTreatment, closeRequest);

        assertTrue(patientService.canBeTransferred(createPatientRequest.getCase_id()));
    }

    @Test
    public void patientCannotBeTransferredToAnotherProvider_WhenHeWasNotTransferredOutForPreviousTreatment() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        PatientRequest closeRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTreatmentOutcome(TreatmentOutcome.Died).build();
        patientService.update(UpdateScope.closeTreatment, closeRequest);

        assertFalse(patientService.canBeTransferred(createPatientRequest.getCase_id()));
    }

    @Test
    public void shouldSetStartDateAndIPStartDateOnTherapy() {
        LocalDate today = today();
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        String patientId = createPatientRequest.getCase_id();

        patientService.startTherapy(patientId, today);

        assertEquals(today, allPatients.findByPatientId(patientId).latestTherapy().getStartDate());
    }

    private void assertCurrentTreatmentIsNew(Patient updatedPatient, PatientRequest openNewPatientRequest) {
        Treatment currentTreatment = updatedPatient.getCurrentTreatment();
        assertEquals(openNewPatientRequest.getDate_modified().toLocalDate(), currentTreatment.getStartDate());
        assertEquals(openNewPatientRequest.getTb_id().toLowerCase(), currentTreatment.getTbId());
        assertEquals(openNewPatientRequest.getTreatment_category(), updatedPatient.latestTherapy().getTreatmentCategory());
        assertEquals(openNewPatientRequest.getDisease_class(), updatedPatient.latestTherapy().getDiseaseClass());
        assertEquals(openNewPatientRequest.getProvider_id().toLowerCase(), currentTreatment.getProviderId());
        assertEquals(openNewPatientRequest.getTb_registration_number(), currentTreatment.getTbRegistrationNumber());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentClosed(Patient updatedPatient, DateTime lastModifiedDate) {
        Treatment currentTreatment = updatedPatient.getCurrentTreatment();
        assertEquals(lastModifiedDate.toLocalDate(), updatedPatient.latestTherapy().getCloseDate());
        assertEquals(lastModifiedDate.toLocalDate(), currentTreatment.getEndDate());
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
