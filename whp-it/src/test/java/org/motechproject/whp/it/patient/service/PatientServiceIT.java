package org.motechproject.whp.it.patient.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.SmearTestResult.Positive;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientForRequests;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class PatientServiceIT extends SpringIntegrationTest {

    public static final String PROVIDER_DISTRICT = "district";
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private UpdateCommandFactory commandFactory;
    @Autowired
    PatientService patientService;
    @Autowired
    ProviderService providerService;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllTherapyRemarks allTherapyRemarks;
    @Autowired
    AllDistricts allDistricts;
    District district;

    @Before
    public void setUp() {
        super.before();
        allTherapyRemarks.removeAll();
        allPatients.removeAll();
        allProviders.add(new ProviderBuilder()
                .withDefaults()
                .withProviderId(PatientBuilder.PROVIDER_ID)
                .withDistrict(PROVIDER_DISTRICT)
                .build());
        district = new District("district");
        allDistricts.add(district);
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTherapyRemarks.getAll().toArray());
        allProviders.removeAll();
        allDistricts.remove(district);
        super.after();
    }

    @Test
    public void shouldUpdatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(PATIENT_ID)
                .withLastModifiedDate("17/03/1990 04:55:50")
                .withPatientAge(50)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);
        District new_district = new District("new_district");
        allDistricts.add(new_district);

        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields()
                .withLastModifiedDate("17/03/1990 04:55:50")
                .withPatientAge(66)
                .withTbId("elevenDigit")
                .withCaseId(PATIENT_ID)
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);

        allDistricts.remove(new_district);
        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);
        Therapy therapy = updatedPatient.getCurrentTherapy();

        //treatmentCreationDate does not change
        assertEquals(patientRequest.getDate_modified(), updatedPatient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT));
        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(WHPDateTime.date(updatePatientRequest.getDate_modified()).dateTime(), updatedPatient.getLastModifiedDate());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentTreatment().getPatientAddress());
        assertEquals(updatePatientRequest.getTb_registration_number(), updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        verifySmearTestResultsUpdate(updatePatientRequest, updatedPatient);
        verifyWeightStatisticsUpdate(updatePatientRequest, updatedPatient);
        assertEquals((Integer) 66, therapy.getPatientAge());
    }

    @Test
    public void shouldUpdateOnlyTheSpecifiedFieldsOnPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate("17/03/1990 04:55:50")
                .withCaseId(PATIENT_ID)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);
        Patient patient = allPatients.findByPatientId(PATIENT_ID);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withPatientInfo(PATIENT_ID, "newFirstName", "newLastName", null, null, "9087654321", null)
                .withTbRegistrationNumber("newTbRegNum")
                .withAddressDistrict("district")
                .withTbId("elevenDigit")
                .build();

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        //treatmentCreationDate does not change
        assertEquals(patientRequest.getDate_modified(), updatedPatient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT));
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
        expectWHPRuntimeException(WHPErrorCode.NO_SUCH_TREATMENT_EXISTS);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate("17/03/1990 04:55:50")
                .withTbId("tbId")
                .withCaseId(PATIENT_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(PATIENT_ID).withTbId("wrongTbId").build();

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithOnlyOneSmearTestResults() {
        expectWHPRuntimeException(WHPErrorCode.SPUTUM_LAB_RESULT_IS_INCOMPLETE);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate("17/03/1990 04:55:50")
                .withTbId("elevenDigit")
                .withCaseId(PATIENT_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(PATIENT_ID)
                .withSmearTestResults(SputumTrackingInstance.PreTreatment, today(), Positive, null, null)
                .withTbId("elevenDigit")
                .withAddressDistrict("district")
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionForUpdateWhenPatientWithGivenCaseIdDoesNotExist() {
        expectWHPRuntimeException(WHPErrorCode.INVALID_PATIENT_CASE_ID);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId("invalidCaseId").build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionForTreatmentUpdateWhenPatientWithGivenCaseIdDoesNotExist() {
        expectWHPRuntimeException(WHPErrorCode.INVALID_PATIENT_CASE_ID);
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
                .withProviderId(PatientBuilder.PROVIDER_ID)
                .withLastModifiedDate("17/03/1990 04:55:50")
                .withCaseId(caseId)
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(caseId);
        assertTrue(patient.isOnActiveTreatment());
        assertThat(patient.getCurrentTreatment().getProviderDistrict(), is(PROVIDER_DISTRICT));
        assertThat(patient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT), is(patientRequest.getDate_modified()));
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
        String lastModifiedDate = "17/03/1990 04:55:50";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        PatientRequest closeTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(lastModifiedDate)
                .build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(closeTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentClosed(updatedPatient, WHPDateTime.date(lastModifiedDate).dateTime());
    }

    @Test
    public void shouldOpenNewTreatmentForPatient() {
        DateTime lastModifiedDate = new DateTime(1990, 3, 17, 4, 55, 50);

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        PatientRequest closeTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(lastModifiedDate.toString(WHPDateTime.DATE_TIME_FORMAT))
                .build();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(closeTreatmentUpdateRequest);

        String newProviderId = "new-provider-id";
        String newDistrict = "newDistrict";

        allProviders.add(new ProviderBuilder()
                .withDefaults()
                .withProviderId(newProviderId)
                .withDistrict(newDistrict)
                .build());

        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withProviderId(newProviderId)
                .withDateModified(lastModifiedDate.plusDays(1).toString(WHPDateTime.DATE_TIME_FORMAT))
                .withTbId("newTbId")
                .build();
        commandFactory.updateFor(UpdateScope.openTreatment).apply(openNewTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentIsNew(updatedPatient, openNewTreatmentUpdateRequest);
        assertThat(updatedPatient.getCurrentTreatment().getProviderDistrict(), is(newDistrict));
        assertThat(updatedPatient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT), is(openNewTreatmentUpdateRequest.getDate_modified()));
    }

    @Test
    public void shouldPauseAndRestartForPatient() {
        DateTime lastModifiedDate = new DateTime(1990, 3, 17, 4, 55, 50);
        String originalDateModified = lastModifiedDate.toString(WHPDateTime.DATE_TIME_FORMAT);

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withDateModified(originalDateModified).build();
        patientService.createPatient(patientRequest);

        PatientRequest pauseTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForPauseTreatment()
                .withDateModified(lastModifiedDate.plusDays(1).toString(WHPDateTime.DATE_TIME_FORMAT))
                .build();
        commandFactory.updateFor(UpdateScope.pauseTreatment).apply(pauseTreatmentRequest);

        Patient pausedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentPaused(pausedPatient, pauseTreatmentRequest);
        assertEquals(originalDateModified, pausedPatient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT));

        PatientRequest restartTreatmentRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForRestartTreatment()
                .withDateModified(lastModifiedDate.plusDays(2).toString(WHPDateTime.DATE_TIME_FORMAT))
                .build();
        commandFactory.updateFor(UpdateScope.restartTreatment).apply(restartTreatmentRequest);

        Patient restartedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertCurrentTreatmentNotPaused(restartedPatient, restartTreatmentRequest);
        assertEquals(originalDateModified, restartedPatient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT));
    }

    @Test
    public void shouldSetStartDateAndIPStartDateOnTherapyUponCreatingNewPatient() {
        LocalDate today = today();
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        String patientId = createPatientRequest.getCase_id();

        Patient patient = allPatients.findByPatientId(patientId);
        patient.startTherapy(today);
        allPatients.update(patient);

        assertEquals(today, patient.getCurrentTherapy().getStartDate());
        assertEquals(today, patient.getCurrentTherapy().getPhases().getIPStartDate());
    }

    @Test
    public void shouldUpdatePillTakenCountForGivenPhase() {
        LocalDate today = today();
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        String patientId = createPatientRequest.getCase_id();

        Patient patient = allPatients.findByPatientId(patientId);
        patient.startTherapy(today);
        allPatients.update(patient);

        patient.setNumberOfDosesTaken(Phase.IP, 2, currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday));

        assertEquals(2, patient.getCurrentTherapy().getPhases().getNumberOfDosesTaken(Phase.IP));
        assertEquals(2, patient.getCurrentTherapy().getPhases().getNumberOfDosesTakenAsOfLastSunday(Phase.IP, today()));
    }

    @Test
    public void shouldSearchPatientsByProviderIdWhenBothProviderDistrictAndProviderIdArePresent() {
        String districtName1 = "Vaishali";
        String districtName2 = "Begusarai";
        District district1 = new District(districtName1);
        District district2 = new District(districtName2);
        allDistricts.add(district1);
        allDistricts.add(district2);

        String searchProviderId = "provider2";
        createProvider("provider1", districtName1);
        createProvider(searchProviderId, districtName1);
        createProvider("provider3", districtName2);
        Provider providerToBeUsedForSearch = providerService.findByProviderId(searchProviderId);

        PatientRequest createPatientRequest1 = new PatientRequestBuilder().withDefaults().withCaseId("1").withProviderId("provider1").withPatientAddress("", "", "", "", "district", "").build();
        patientService.createPatient(createPatientRequest1);
        PatientRequest createPatientRequest2 = new PatientRequestBuilder().withDefaults().withCaseId("2").withProviderId(searchProviderId).withPatientAddress("", "", "", "", "district", "").build();
        patientService.createPatient(createPatientRequest2);
        PatientRequest createPatientRequest3 = new PatientRequestBuilder().withDefaults().withCaseId("3").withProviderId("provider3").withPatientAddress("", "", "", "", "district", "").build();
        patientService.createPatient(createPatientRequest3);

        List<Patient> patientList = patientService.getAllWithActiveTreatmentForProvider(providerToBeUsedForSearch.getProviderId());
        allDistricts.remove(district1);
        allDistricts.remove(district2);

        assertPatientForRequests(asList(createPatientRequest2), patientList);
    }

    @Test
    public void shouldAddRemarkToTherapy() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);
        Patient patient = patientService.findByPatientId(PATIENT_ID);

        patientService.addRemark(patient, "remark", "username");

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

    @Test
    public void shouldPerformSimpleUpdate_onAnyTreatmentAcrossTherapiesForPatient() {

        DateTime lastModifiedDate = new DateTime(1990, 3, 17, 4, 55, 50);
        String originalDateModified = lastModifiedDate.toString(WHPDateTime.DATE_TIME_FORMAT);

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        PatientRequest closeTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(originalDateModified)
                .build();
        String oldTbId = closeTreatmentUpdateRequest.getTb_id();
        commandFactory.updateFor(UpdateScope.closeTreatment).apply(closeTreatmentUpdateRequest);

        String newProviderId = "new-provider-id";
        String newDistrict = "newDistrict";

        allProviders.add(new ProviderBuilder()
                .withDefaults()
                .withProviderId(newProviderId)
                .withDistrict(newDistrict)
                .build());

        String newTreatmentDateModified = lastModifiedDate.plusDays(1).toString(WHPDateTime.DATE_TIME_FORMAT);
        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withProviderId(newProviderId)
                .withDateModified(newTreatmentDateModified)
                .withTbId("newTbId")
                .build();
        commandFactory.updateFor(UpdateScope.openTreatment).apply(openNewTreatmentUpdateRequest);

        LocalDate today = today();
        SmearTestResult testResult = SmearTestResult.Positive;
        SputumTrackingInstance sampleInstance = SputumTrackingInstance.ExtendedIP;
        PatientRequest simpleUpdateRequest = new PatientRequestBuilder()
                .withSimpleUpdateFields()
                .withDateModified(originalDateModified)
                .withSmearTestResults(sampleInstance, today, testResult, today, testResult)
                .withAddressDistrict("district")
                .withTbId(oldTbId)
                .build();
        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(simpleUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        assertEquals(newTreatmentDateModified, updatedPatient.getCurrentTreatment().getCreationDate().toString(WHPDateTime.DATE_TIME_FORMAT));
        SmearTestRecord smearTestRecord = updatedPatient.getTreatmentBy(oldTbId).getSmearTestResults().resultForInstance(sampleInstance);
        assertEquals(sampleInstance, smearTestRecord.getSmear_sample_instance());
        assertEquals(testResult, smearTestRecord.getSmear_test_result_1());
        assertEquals(testResult, smearTestRecord.getSmear_test_result_2());
    }

    @Test
    public void shouldPerformSimpleUpdate_ForEmptySmearTestResults() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        String new_district = "new_district";
        District newDistrict = new District(new_district);
        allDistricts.add(newDistrict);
        PatientRequest simpleUpdateRequest = new PatientRequestBuilder()
                .withDateModified("17/03/1990 04:55:50")
                .withSmearTestResults(SputumTrackingInstance.PreTreatment, null, null, null, null)
                .withTbId(patientRequest.getTb_id())
                .withPatientAddress("new_house number", "new_landmark", "new_block", "new_village", new_district, "new_state")
                .withWeightStatistics(SputumTrackingInstance.EndTreatment, 99.7, DateUtil.newDate(2010, 9, 20))
                .build();
        simpleUpdateRequest.setPatientInfo(PATIENT_ID, null, null, null, null, "9087654321", null)
                .setTreatmentData(null, patientRequest.getTb_id(), null, null, 50, "newRegistrationNumber", "20/09/2010 04:55:50");

        commandFactory.updateFor(UpdateScope.simpleUpdate).apply(simpleUpdateRequest);

        allDistricts.remove(newDistrict);
        Patient updatedPatient = allPatients.findByPatientId(PATIENT_ID);

        SmearTestRecord smearTestRecord = updatedPatient.getTreatmentBy(patientRequest.getTb_id()).getSmearTestResults().latestResult();
        assertEquals(SputumTrackingInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertNull(smearTestRecord.getSmear_test_result_1());
        assertNull(smearTestRecord.getSmear_test_result_2());
        assertNull(smearTestRecord.getSmear_test_date_1());
        assertNull(smearTestRecord.getSmear_test_date_2());
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
        assertEquals(WHPDate.date(openNewPatientRequest.getTb_registration_date()).date(), currentTreatment.getStartDate());
        assertEquals(openNewPatientRequest.getTb_id().toLowerCase(), currentTreatment.getTbId());
        assertEquals(openNewPatientRequest.getTreatment_category(), updatedPatient.getCurrentTherapy().getTreatmentCategory());
        assertEquals(openNewPatientRequest.getDisease_class(), updatedPatient.getCurrentTherapy().getDiseaseClass());
        assertEquals(openNewPatientRequest.getProvider_id().toLowerCase(), currentTreatment.getProviderId());
        assertEquals(openNewPatientRequest.getTb_registration_number(), currentTreatment.getTbRegistrationNumber());
        assertEquals(openNewPatientRequest.getDate_modified(), updatedPatient.getLastModifiedDate().toString(WHPDateTime.DATE_TIME_FORMAT));
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
        assertEquals(WHPDateTime.date(pauseTreatmentRequest.getDate_modified()).dateTime(), pausedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentNotPaused(Patient pausedPatient, PatientRequest pauseTreatmentRequest) {
        assertFalse(pausedPatient.isCurrentTreatmentPaused());
        assertEquals(WHPDateTime.date(pauseTreatmentRequest.getDate_modified()).dateTime(), pausedPatient.getLastModifiedDate());
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
