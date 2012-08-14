package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientForRequests;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;
import static org.motechproject.whp.refdata.domain.SmearTestResult.Positive;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class PatientServiceIT extends SpringIntegrationTest {

    @Autowired
    private AllPatients allPatients;
    @Autowired
    private UpdateCommandFactory commandFactory;
    @Autowired
    PatientService patientService;
    @Autowired
    ProviderService providerService;
    @Autowired
    AllTherapyRemarks allTherapyRemarks;

    @Before
    public void setUp() {
        super.before();
        allTherapyRemarks.removeAll();
        allPatients.removeAll();
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTherapyRemarks.getAll().toArray());
        super.after();
    }

    @Test
    public void shouldUpdatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(PATIENT_ID)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(66)
                .withTbId("elevenDigit")
                .withCaseId(PATIENT_ID)
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);
        Therapy therapy = updatedPatient.getCurrentTherapy();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getDate_modified(), updatedPatient.getLastModifiedDate());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentTreatment().getPatientAddress());
        assertEquals(updatePatientRequest.getTb_registration_number(), updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        verifySmearTestResultsUpdate(updatePatientRequest, updatedPatient);
        verifyWeightStatisticsUpdate(updatePatientRequest, updatedPatient);
        assertEquals((Integer) 66, therapy.getPatientAge());
    }

    @Test
    public void shouldUpdateOnlyTheSpecifiedFieldsOnPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(PATIENT_ID)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);
        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withPatientInfo(PATIENT_ID, "newFirstName", "newLastName", null, null, "9087654321", null)
                .withTbRegistrationNumber("newTbRegNum")
                .withTbId("elevenDigit")
                .build();

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

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
                .withCaseId(PATIENT_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(PATIENT_ID).withTbId("wrongTbId").build();

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithOnlyOneSmearTestResults() {
        expectWHPRuntimeException(WHPErrorCode.NULL_VALUE_IN_SMEAR_TEST_RESULTS);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("elevenDigit")
                .withCaseId(PATIENT_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(PATIENT_ID)
                .withSmearTestResults(SampleInstance.PreTreatment, today(), Positive, null, null)
                .withTbId("elevenDigit")
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithoutAnySmearTestResults() {
        expectWHPRuntimeException(WHPErrorCode.NULL_VALUE_IN_SMEAR_TEST_RESULTS);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("elevenDigit")
                .withCaseId(PATIENT_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(PATIENT_ID)
                .withSmearTestResults(SampleInstance.PreTreatment, null, null, null, null)
                .withTbId("elevenDigit")
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionForUpdateWhenPatientWithGivenCaseIdDoesNotExist() {
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
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(patientRequest);
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseItIsAlreadyClosed() {
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        //first properly closing treatment
        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(patientRequest);

        PatientRequest wrongPatientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build();
        //now trying to close a closed treatment
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(wrongPatientRequest);
    }

    @Test
    public void shouldCreatePatientWithActiveTreatment() {
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
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
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
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        patientRequest = new PatientRequestBuilder().withMandatoryFieldsForOpenNewTreatment().withTbId("tbId").build();
        commandFactory.updateFor(UpdateScope.openTreatment).apply(patientRequest);
    }

    @Test
    public void shouldCloseCurrentTreatmentForPatient() {
        DateTime lastModifiedDate = DateUtil.newDateTime(1990, 3, 17, 4, 55, 50);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        PatientRequest closeTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(lastModifiedDate)
                .build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(closeTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentClosed(updatedPatient, lastModifiedDate);
    }

    @Test
    public void shouldOpenNewTreatmentForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
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

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentIsNew(updatedPatient, openNewTreatmentUpdateRequest);
    }

    @Test
    public void shouldPauseAndRestartForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        PatientRequest pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        commandFactory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        Patient pausedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentPaused(pausedPatient, pauseTreatmentRequest);

        PatientRequest restartTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        commandFactory.updateFor(UpdateScope.restartTreatment).apply(restartTreatmentRequest);

        Patient restartedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentNotPaused(restartedPatient, restartTreatmentRequest);
    }

    @Test
    public void patientCanBeTransferredToAnotherProvider_WhenHeWasTransferredOutForPreviousTreatment() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        PatientRequest closeRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTreatmentOutcome(TreatmentOutcome.TransferredOut).build();
        patientService.update(closeRequest);

        assertTrue(patientService.canBeTransferred(createPatientRequest.getCase_id()));
    }

    @Test
    public void patientCannotBeTransferredToAnotherProvider_WhenHeWasNotTransferredOutForPreviousTreatment() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        PatientRequest closeRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTreatmentOutcome(TreatmentOutcome.Died).build();
        patientService.update(closeRequest);

        assertFalse(patientService.canBeTransferred(createPatientRequest.getCase_id()));
    }

    @Test
    public void shouldSetStartDateAndIPStartDateOnTherapyUponCreatingNewPatient() {
        LocalDate today = today();
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        String patientId = createPatientRequest.getCase_id();

        patientService.startTherapy(patientId, today);

        assertEquals(today, allPatients.findByPatientId(patientId).getCurrentTherapy().getStartDate());
        assertEquals(today, allPatients.findByPatientId(patientId).getCurrentTherapy().getPhases().getIPStartDate());
    }

    @Test
    public void shouldUpdatePillTakenCountForGivenPhase() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        patientService.startTherapy(createPatientRequest.getCase_id(), today());
        String patientId = createPatientRequest.getCase_id();

        patientService.updatePillTakenCount(allPatients.findByPatientId(patientId), Phase.IP, 2, currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday));

        assertEquals(2, allPatients.findByPatientId(patientId).getCurrentTherapy().getPhases().getNumberOfDosesTaken(Phase.IP));
        assertEquals(2, allPatients.findByPatientId(patientId).getCurrentTherapy().getPhases().getNumberOfDosesTakenAsOfLastSunday(Phase.IP, today()));
    }

    @Test
    public void shouldSearchPatientsByProviderDistrict() {
        createProvider("provider1", "Vaishali");
        createProvider("provider2", "Vaishali");
        createProvider("provider3", "Begusarai");

        PatientRequest createPatientRequest1 = new PatientRequestBuilder().withDefaults().withCaseId("1").withProviderId("provider1").withPatientAddress("", "", "", "", "", "").build();
        patientService.createPatient(createPatientRequest1);
        PatientRequest createPatientRequest2 = new PatientRequestBuilder().withDefaults().withCaseId("2").withProviderId("provider2").withPatientAddress("", "", "", "", "", "").build();
        patientService.createPatient(createPatientRequest2);
        PatientRequest createPatientRequest3 = new PatientRequestBuilder().withDefaults().withCaseId("3").withProviderId("provider3").withPatientAddress("", "", "", "", "", "").build();
        patientService.createPatient(createPatientRequest3);

        List<Patient> patientList = patientService.searchBy("Vaishali");
        assertPatientForRequests(asList(createPatientRequest1, createPatientRequest2), patientList);
    }

    @Test
    public void shouldSearchPatientsByProviderIdWhenBothProviderDistrictAndProviderIdArePresent() {
        String searchProviderId = "provider2";
        createProvider("provider1", "Vaishali");
        createProvider(searchProviderId, "Vaishali");
        createProvider("provider3", "Begusarai");
        Provider providerToBeUsedForSearch = providerService.findByProviderId(searchProviderId);

        PatientRequest createPatientRequest1 = new PatientRequestBuilder().withDefaults().withCaseId("1").withProviderId("provider1").withPatientAddress("", "", "", "", "", "").build();
        patientService.createPatient(createPatientRequest1);
        PatientRequest createPatientRequest2 = new PatientRequestBuilder().withDefaults().withCaseId("2").withProviderId(searchProviderId).withPatientAddress("", "", "", "", "", "").build();
        patientService.createPatient(createPatientRequest2);
        PatientRequest createPatientRequest3 = new PatientRequestBuilder().withDefaults().withCaseId("3").withProviderId("provider3").withPatientAddress("", "", "", "", "", "").build();
        patientService.createPatient(createPatientRequest3);

        List<Patient> patientList = patientService.getAllWithActiveTreatmentForProvider(providerToBeUsedForSearch.getProviderId());
        assertPatientForRequests(asList(createPatientRequest2), patientList);
    }

    @Test
    public void shouldAddRemarkToTherapy() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);

        patientService.addRemark(PATIENT_ID, "remark", "username");

        assertEquals(1, allTherapyRemarks.getAll().size());

        TherapyRemark therapyRemark = allTherapyRemarks.getAll().get(0);
        assertEquals(PATIENT_ID, therapyRemark.getPatientId());
        assertEquals(patient.getCurrentTherapy().getUid(), therapyRemark.getTherapyUid());
        assertEquals("remark", therapyRemark.getRemark());
        assertEquals("username", therapyRemark.getUser());

    }

    @Test
    public void shouldClearDoseInterruptionsForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);
        patient.dosesMissedSince(new LocalDate());
        patient.dosesResumedOnAfterBeingInterrupted(new LocalDate().plusDays(2));
        patient.clearDoseInterruptionsForUpdate();

        assertEquals(0, patient.getCurrentTherapy().getDoseInterruptions().size());
    }

    @Test
    public void shouldAddDoseInterruptionForPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);

        LocalDate today = today();
        patient.dosesMissedSince(today);
        assertEquals(0, patient.getCurrentTherapy().getDoseInterruptions().get(0).compareTo(new DoseInterruption(today)));
        assertTrue(patient.getCurrentTherapy().getDoseInterruptions().get(0).isOngoing());
    }

    @Test
    public void shouldCloseCurrentDoseInterruptionForPatient_WithEndDateAsOneDayBeforeDoseDate() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);

        LocalDate today = today();
        patient.dosesMissedSince(today);
        patient.dosesResumedOnAfterBeingInterrupted(today.plusDays(2));

        assertEquals(today.plusDays(1), patient.getCurrentTherapy().getDoseInterruptions().get(0).endDate());
        assertFalse(patient.getCurrentTherapy().getDoseInterruptions().get(0).isOngoing());
    }

    @Test
    public void shouldNotAddDoseInterruptionForPatient_IfHeAlreadyHasAnOngoingInterruption() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);

        LocalDate today = today();
        patient.dosesMissedSince(today);
        patient.dosesMissedSince(today.plusDays(2));

        assertEquals(0, patient.getCurrentTherapy().getDoseInterruptions().get(0).compareTo(new DoseInterruption(today)));
    }

    @Test
    public void shouldNotCloseDoseInterruptionForPatient_IfHeDoseNotHaveAnOngoingInterruption() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);

        LocalDate today = today();
        patient.dosesMissedSince(today);
        patient.dosesResumedOnAfterBeingInterrupted(today.plusDays(2));
        patient.dosesResumedOnAfterBeingInterrupted(today.plusDays(3));

        assertEquals(today.plusDays(1), patient.getCurrentTherapy().getDoseInterruptions().get(0).endDate());
    }


    private void createProvider(String providerId, String district) {
        String primaryMobile = "1234567890";
        String secondaryMobile = "0987654321";
        String tertiaryMobile = "1111111111";
        DateTime now = now();

        ProviderRequest providerRequest = new ProviderRequest(providerId, district, primaryMobile, now);
        providerRequest.setSecondaryMobile(secondaryMobile);
        providerRequest.setTertiaryMobile(tertiaryMobile);

        providerService.registerProvider(providerRequest);

    }

    private void assertCurrentTreatmentIsNew(Patient updatedPatient, PatientRequest openNewPatientRequest) {
        Treatment currentTreatment = updatedPatient.getCurrentTreatment();
        assertEquals(openNewPatientRequest.getDate_modified().toLocalDate(), currentTreatment.getStartDate());
        assertEquals(openNewPatientRequest.getTb_id().toLowerCase(), currentTreatment.getTbId());
        assertEquals(openNewPatientRequest.getTreatment_category(), updatedPatient.getCurrentTherapy().getTreatmentCategory());
        assertEquals(openNewPatientRequest.getDisease_class(), updatedPatient.getCurrentTherapy().getDiseaseClass());
        assertEquals(openNewPatientRequest.getProvider_id().toLowerCase(), currentTreatment.getProviderId());
        assertEquals(openNewPatientRequest.getTb_registration_number(), currentTreatment.getTbRegistrationNumber());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentClosed(Patient updatedPatient, DateTime lastModifiedDate) {
        Treatment currentTreatment = updatedPatient.getCurrentTreatment();
        assertEquals(lastModifiedDate.toLocalDate(), updatedPatient.getCurrentTherapy().getCloseDate());
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
}
