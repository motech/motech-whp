package org.motechproject.whp.patient.service;

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
import org.motechproject.whp.patient.domain.SmearTestSampleInstance;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.criteria.UpdateTreatmentCriteria;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static org.motechproject.util.DateUtil.today;

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
    private UpdateTreatmentCriteria openNewTreatmentCriteria;

    PatientService patientService;

    @Before
    public void setUp() {
        patientService = new PatientService(allPatients, allTreatments, openNewTreatmentCriteria);
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
                                                                   .build();
        patientService.add(patientRequest);

        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields()
                                                                         .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                         .withPatientAge(66)
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
                                                                   .build();
        patientService.add(patientRequest);
        Patient patient = allPatients.findByPatientId(CASE_ID);
        PatientRequest updatePatientRequest = new PatientRequestBuilder()
                .withPatientInfo(CASE_ID, "newFirstName", "newLastName", null, null, "9087654321", null)
                .withTbRegistrationNumber("newTbRegNum").build();

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
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidSmearTestResults() {
        expectException("invalid treatment data.[Invalid smear test results : null value]");
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .withCaseId(CASE_ID)
                                                                   .build();
        patientService.add(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID).withSmearTestResults(SmearTestSampleInstance.PreTreatment, null, null, null, null).build();

        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidWeightStatistics() {
        expectException("invalid treatment data.[Invalid weight statistics : null value]");
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .withCaseId(CASE_ID)
                                                                   .build();
        patientService.add(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID).withWeightStatistics(null, 100.0, DateUtil.tomorrow()).build();

        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionWhenPatientWithGivenCaseIdDoesNotExist() {
        expectException("Invalid case-id. No such patient.");
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId("invalidCaseId").build();
        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test
    public void shouldThrowExceptionForTreatmentUpdateWhenPatientWithGivenCaseIdDoesNotExist() {
        expectException("Invalid case-id. No such patient.");
        patientService.performTreatmentUpdate(TreatmentUpdateRequestBuilder.startRecording().withCaseId("invalidCaseId").build());
    }

    @Test
    public void shouldThrowExceptionIfCurrentTreatmentCannotBeClosed() {
        expectException("Cannot close current treatment for case: either wrong tb id, there is no current treatment or it is already closed.");
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .withCaseId(caseId)
                                                                   .withTbId("wrongTbId")
                                                                   .build();
        patientService.add(patientRequest);

        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().build();
        patientService.performTreatmentUpdate(treatmentUpdateRequest);
    }

    @Test
    public void shouldCloseCurrentTreatmentForPatient() {
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .withCaseId(caseId)
                                                                   .withTbId("tbId")
                                                                   .build();
        patientService.add(patientRequest);

        TreatmentUpdateRequest closeTreatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording()
                                                                                     .withMandatoryFieldsForCloseTreatment()
                                                                                     .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                                     .build();
        patientService.performTreatmentUpdate(closeTreatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(caseId);

        assertCurrentTreatmentClosed(updatedPatient);
    }

    @Test
    public void shouldOpenNewTreatmentForPatient() {
        String caseId = "caseId";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                                                                   .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                                                                   .withCaseId(caseId)
                                                                   .withTbId("tbId")
                                                                   .build();
        patientService.add(patientRequest);

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

    private void assertCurrentTreatmentIsNew(Patient updatedPatient, TreatmentUpdateRequest treatmentUpdateRequest) {
        ProvidedTreatment currentProvidedTreatment = updatedPatient.getCurrentProvidedTreatment();
        assertEquals(treatmentUpdateRequest.getDate_modified().toLocalDate(), currentProvidedTreatment.getStartDate());
        assertEquals(treatmentUpdateRequest.getTb_id(), currentProvidedTreatment.getTbId());
        assertEquals(treatmentUpdateRequest.getTreatment_category(), currentProvidedTreatment.getTreatment().getTreatmentCategory());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void assertCurrentTreatmentClosed(Patient updatedPatient) {
        ProvidedTreatment currentProvidedTreatment = updatedPatient.getCurrentProvidedTreatment();
        assertEquals(today(), currentProvidedTreatment.getTreatment().getEndDate());
        assertEquals(today(), currentProvidedTreatment.getEndDate());
        assertEquals("Cured", currentProvidedTreatment.getTreatment().getReasonForClosure());
        assertEquals("Yes", currentProvidedTreatment.getTreatment().getTreatmentComplete());
        assertEquals(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50), updatedPatient.getLastModifiedDate());
    }

    private void expectException(String message) {
        exceptionThrown.expect(WHPDomainException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }
}
