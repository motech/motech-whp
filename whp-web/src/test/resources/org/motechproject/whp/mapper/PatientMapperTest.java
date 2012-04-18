package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

import static org.junit.Assert.assertEquals;

public class PatientMapperTest {
    PatientMapper patientMapper;

    @Before
    public void setUp() {
        patientMapper = new PatientMapper();
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequest()
                .setPatientInfo("caseId", "Foo", "Bar", "M", PatientType.PHSTransfer.name(), "12345667890")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .setSmearTestResults("Pre-treatment1", DateUtil.today().minusDays(10).toString(), "result1", "Pre-treatment2", DateUtil.today().minusDays(5).toString(), "result2")
                .setRegistrationDetails("registrationNumber", DateUtil.today().toString())
                .setTreatmentData("01", "providerId01seq1", "providerId");

        Treatment treatment = new Treatment();

        Patient patient = patientMapper.map(patientRequest, treatment);

        assertBasicPatientInfo(patient);
//        assertSmearTests(patient);
        assertProvidedTreatment(patient);
    }

    private void assertBasicPatientInfo(Patient patient) {
        assertEquals("caseId", patient.getPatientId());
        assertEquals("Foo", patient.getFirstName());
        assertEquals("Bar", patient.getLastName());
        assertEquals(Gender.Male, patient.getGender());
        assertEquals(PatientType.PHSTransfer, patient.getPatientType());
        assertEquals("12345667890", patient.getPhoneNumber());
    }

    private void assertAddress(Address address) {
        assertEquals(new Address("house number", "landmark", "block", "village", "district", "state"), address);
    }

    /*private void assertSmearTests(Patient patient) {
        assertEquals("Pre-treatment1", patient.getLatestProvidedTreatment().getTreatment().getSmearTestResult().getSampleInstance1());
        assertEquals("result1", patient.getLatestProvidedTreatment().getTreatment().getSmearTestResult().getResult1());
        assertEquals(DateUtil.today().minusDays(10), patient.getLatestProvidedTreatment().getTreatment().getSmearTestResult().getTestDate1());
        assertEquals("Pre-treatment2", patient.getLatestProvidedTreatment().getTreatment().getSmearTestResult().getSampleInstance2());
        assertEquals(DateUtil.today().minusDays(5), patient.getLatestProvidedTreatment().getTreatment().getSmearTestResult().getTestDate2());
        assertEquals("result2", patient.getLatestProvidedTreatment().getTreatment().getSmearTestResult().getResult2());
    }*/

    private void assertProvidedTreatment(Patient patient) {
        ProvidedTreatment providedTreatment = patient.getLatestProvidedTreatment();
        assertEquals("providerId01seq1", providedTreatment.getTbId());
        assertEquals("providerId", providedTreatment.getProviderId());
        assertEquals(DateUtil.today(), providedTreatment.getStartDate());

        assertAddress(providedTreatment.getPatientAddress());
    }

}
