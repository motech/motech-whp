package org.motechproject.whp.patient.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class PatientServiceIT extends SpringIntegrationTest{

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
        Patient createPatient = allPatients.findByPatientId(CASE_ID);

        PatientRequest updatePatientRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId(CASE_ID).build();
        patientService.simpleUpdate(updatePatientRequest);
        Patient updatedPatient = allPatients.findByPatientId(CASE_ID);
        Treatment treatment = updatedPatient.getCurrentProvidedTreatment().getTreatment();

        assertEquals(updatePatientRequest.getMobileNumber(), updatedPatient.getPhoneNumber());
        assertEquals(updatePatientRequest.getTbRegistrationNumber(), treatment.getTbRegistrationNumber());
        assertEquals(updatePatientRequest.getSmearTestResults(), treatment.latestSmearTestResult());
        assertEquals(updatePatientRequest.getWeightStatistics(), treatment.latestWeightStatistics());
        assertEquals(updatePatientRequest.getAddress(), updatedPatient.getCurrentProvidedTreatment().getPatientAddress());
    }

}
