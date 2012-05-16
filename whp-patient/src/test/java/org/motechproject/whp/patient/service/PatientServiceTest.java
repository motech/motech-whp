package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.service.treatmentupdate.TreatmentUpdateFactory;
import org.motechproject.whp.refdata.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class PatientServiceTest extends SpringIntegrationTest {

    public static final String CASE_ID = "123456";

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();
    @Autowired
    private AllTreatments allTreatments;
    @Autowired
    private AllPatients allPatients;
    @Autowired
    private TreatmentUpdateFactory factory;

    PatientService patientService;

    @Before
    public void setUp() {
        patientService = new PatientService(allPatients, allTreatments, factory);
    }

    @After
    public void tearDown() {
        markForDeletion(allTreatments.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
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
        patientService.simpleUpdate(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        Treatment treatment = updatedPatient.getCurrentProvidedTreatment().getTreatment();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getDate_modified(), updatedPatient.getLastModifiedDate());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentProvidedTreatment().getPatientAddress());
        assertEquals(updatePatientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(updatePatientRequest.getSmearTestResults(), treatment.latestSmearTestResult());
        assertEquals(updatePatientRequest.getWeightStatistics(), treatment.latestWeightStatistics());
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

        patientService.simpleUpdate(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        Treatment treatment = updatedPatient.getCurrentProvidedTreatment().getTreatment();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertNotSame("newFirstName", updatedPatient.getFirstName());
        assertNotSame("newLastName", updatedPatient.getLastName());
        assertEquals(patient.getCurrentProvidedTreatment().getPatientAddress(), updatedPatient.getCurrentProvidedTreatment().getPatientAddress());
        assertEquals(patient.getCurrentProvidedTreatment().getTreatment().latestSmearTestResult(), treatment.latestSmearTestResult());
        assertEquals(patient.getCurrentProvidedTreatment().getTreatment().latestWeightStatistics(), treatment.latestWeightStatistics());
    }

    @Test
    public void shouldThrowExceptionWhenSimpleUpdateOnPatientIsTriedWithWrongTbId() {
        expectWHPDomainException("Cannot update details for this case: [No such tb id for current treatment]");
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("tbId")
                .withCaseId(CASE_ID)
                .build();
        patientService.createPatient(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID).withTbId("wrongTbId").build();

        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidSmearTestResults() {
        expectWHPDomainException("Invalid treatment data.[Invalid smear test results : null value]");
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
        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidWeightStatistics() {
        expectWHPDomainException("nvalid treatment data.[Invalid weight statistics : null value]");
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
        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientWithGivenCaseIdDoesNotExist() {
        expectWHPDomainException("Cannot update details for this case: [Invalid case-id. No such patient.]");
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId("invalidCaseId").build();
        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionForTreatmentUpdateWhenPatientWithGivenCaseIdDoesNotExist() {
        expectWHPDomainException("Cannot open new treatment for this case: [Invalid case-id. No such patient.]");
        patientService.performTreatmentUpdate(TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForOpenNewTreatment().withCaseId("invalidCaseId").build());
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseTbIdIsWrong() {
        expectWHPDomainException("Cannot close current treatment for case: [No such tb id for current treatment]");
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();
        patientService.performTreatmentUpdate(treatmentUpdateRequest);
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseItIsAlreadyClosed() {
        expectWHPDomainException("Cannot close current treatment for case: [Current treatment is already closed]");
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        //first properly closing treatment
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().build();
        patientService.performTreatmentUpdate(treatmentUpdateRequest);

        TreatmentUpdateRequest wrongTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().build();
        //now trying to close a closed treatment
        patientService.performTreatmentUpdate(wrongTreatmentUpdateRequest);
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosedBecauseTbIdIsWrongAndTreatmentIsAlreadyClosed() {
        expectWHPDomainException("Cannot close current treatment for case: [No such tb id for current treatment, Current treatment is already closed]");
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        //first properly closing treatment
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().build();
        patientService.performTreatmentUpdate(treatmentUpdateRequest);

        TreatmentUpdateRequest wrongTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();

        //now trying to close a closed treatment, that too with wrong tbId
        patientService.performTreatmentUpdate(wrongTreatmentUpdateRequest);
    }

    @Test
    public void shouldThrowExceptionIfNewTreatmentCannotBeOpenedBecauseCurrentTreatmentIsNotClosed() {
        expectWHPDomainException("Cannot open new treatment for this case: [Current treatment is not closed]");
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForOpenNewTreatment().withTbId("tbId").build();
        patientService.performTreatmentUpdate(treatmentUpdateRequest);
    }

    @Test
    public void shouldCloseCurrentTreatmentForPatient() {
        String caseId = "caseId";
        DateTime lastModifiedDate = DateUtil.newDateTime(1990, 3, 17, 4, 55, 50);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(lastModifiedDate)
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        TreatmentUpdateRequest closeTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(lastModifiedDate)
                .build();
        patientService.performTreatmentUpdate(closeTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentClosed(updatedPatient, lastModifiedDate);
    }

    @Test
    public void shouldOpenNewTreatmentForPatient() {
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withCaseId(caseId)
                .withTbId("tbId")
                .build();
        patientService.createPatient(patientRequest);

        TreatmentUpdateRequest closeTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                .withMandatoryFieldsForCloseTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        patientService.performTreatmentUpdate(closeTreatmentUpdateRequest);

        TreatmentUpdateRequest openNewTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                .withMandatoryFieldsForOpenNewTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .build();
        patientService.performTreatmentUpdate(openNewTreatmentUpdateRequest);

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

        TreatmentUpdateRequest pauseTreatmentRequest = TreatmentUpdateRequestBuilder.startRecording()
                .withMandatoryFieldsForPauseTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        patientService.performTreatmentUpdate(pauseTreatmentRequest);

        Patient pausedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentPaused(pausedPatient, pauseTreatmentRequest);
        assertTrue(pausedPatient.isCurrentTreatmentPaused());

        TreatmentUpdateRequest restartTreatmentRequest = TreatmentUpdateRequestBuilder.startRecording()
                .withMandatoryFieldsForRestartTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        patientService.performTreatmentUpdate(restartTreatmentRequest);

        Patient restartedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentNotPaused(restartedPatient, restartTreatmentRequest);
    }

    private void assertCurrentTreatmentIsNew(Patient updatedPatient, TreatmentUpdateRequest treatmentUpdateRequest) {
        ProvidedTreatment currentProvidedTreatment = updatedPatient.getCurrentProvidedTreatment();
        assertEquals(treatmentUpdateRequest.getDate_modified().toLocalDate(), currentProvidedTreatment.getStartDate());
        assertEquals(treatmentUpdateRequest.getTb_id(), currentProvidedTreatment.getTbId());
        assertEquals(treatmentUpdateRequest.getTreatment_category(), currentProvidedTreatment.getTreatment().getTreatmentCategory());
        assertEquals(treatmentUpdateRequest.getDisease_class(), currentProvidedTreatment.getTreatment().getDiseaseClass());
        assertEquals(treatmentUpdateRequest.getProvider_id(), currentProvidedTreatment.getProviderId());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentClosed(Patient updatedPatient, DateTime lastModifiedDate) {
        ProvidedTreatment currentProvidedTreatment = updatedPatient.getCurrentProvidedTreatment();
        assertEquals(lastModifiedDate.toLocalDate(), currentProvidedTreatment.getTreatment().getCloseDate());
        assertEquals(lastModifiedDate.toLocalDate(), currentProvidedTreatment.getEndDate());
        assertEquals(TreatmentOutcome.Cured, currentProvidedTreatment.getTreatment().getTreatmentOutcome());
        assertEquals(TreatmentStatus.Closed, currentProvidedTreatment.getTreatment().getStatus());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentPaused(Patient pausedPatient, TreatmentUpdateRequest pauseTreatmentRequest) {
        assertTrue(pausedPatient.isCurrentTreatmentPaused());
        assertEquals(pauseTreatmentRequest.getDate_modified(), pausedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentNotPaused(Patient pausedPatient, TreatmentUpdateRequest pauseTreatmentRequest) {
        assertFalse(pausedPatient.isCurrentTreatmentPaused());
        assertEquals(pauseTreatmentRequest.getDate_modified(), pausedPatient.getLastModifiedDate());
    }

    private void expectWHPDomainException(String message) {
        exceptionThrown.expect(WHPDomainException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }
}
