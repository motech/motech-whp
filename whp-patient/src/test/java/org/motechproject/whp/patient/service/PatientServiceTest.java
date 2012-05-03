package org.motechproject.whp.patient.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.SmearTestSampleInstance;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class PatientServiceTest extends SpringIntegrationTest {

    public static final String CASE_ID = "123456";
    @Autowired
    private AllTreatments allTreatments;
    @Autowired
    private AllPatients allPatients;

    PatientService patientService;

    @Before
    public void setUp() {
        patientService = new PatientService(allPatients, allTreatments);
    }

    @After
    public void tearDown() {
        markForDeletion(allTreatments.getAll().toArray());
        markForDeletion(allPatients.getAll().toArray());
    }

    @Test
    public void shouldUpdatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withCaseId(CASE_ID).build();
        patientService.add(patientRequest);

        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId(CASE_ID).build();
        patientService.simpleUpdate(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        Treatment treatment = updatedPatient.getCurrentProvidedTreatment().getTreatment();

        assertEquals(updatePatientRequest.getMobile_number(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getDate_modified(), updatedPatient.getLastModifiedDate());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentProvidedTreatment().getPatientAddress());
        assertEquals(updatePatientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(updatePatientRequest.getAge(), treatment.getPatientAge());
        assertEquals(updatePatientRequest.getSmearTestResults(), treatment.latestSmearTestResult());
        assertEquals(updatePatientRequest.getWeightStatistics(), treatment.latestWeightStatistics());
    }

    @Test
    public void shouldUpdateOnlyTheSpecifiedFieldsOnPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withCaseId(CASE_ID).build();
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

    @Test(expected = WHPDomainException.class)
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidSmearTestResults() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withCaseId(CASE_ID).build();
        patientService.add(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID).withSmearTestResults(SmearTestSampleInstance.PreTreatment, null, null, null, null).build();

        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test(expected = WHPDomainException.class)
    public void shouldThrowExceptionWhenPatientIsUpdatedWithInvalidWeightStatistics() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withCaseId(CASE_ID).build();
        patientService.add(patientRequest);
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withCaseId(CASE_ID).withWeightStatistics(null, 100.0, DateUtil.tomorrow()).build();

        patientService.simpleUpdate(updatePatientRequest);
    }

    @Test(expected = WHPDomainException.class)
    public void shouldThrowExceptionWhenPatientWithGivenCaseIdDoesNotExist() {
        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId("invalidCaseId").build();
        patientService.simpleUpdate(updatePatientRequest);
    }

}
