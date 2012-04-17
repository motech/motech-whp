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
                .setPatientInfo("caseId", "Foo", "Bar", "M", PatientType.PHSTransfer.name())
                .setSmearTestResults("Pre-treatment1", DateUtil.today().minusDays(10).toString(), "result1", "Pre-treatment2", DateUtil.today().minusDays(5).toString(), "result2")
                .setRegistrationDetails("registrationNumber", DateUtil.today().toString())
                .setTreatmentData("01", "providerId01seq1", "providerId");

        Patient patient = patientMapper.map(patientRequest);

        assertBasicPatientInfo(patient);
        assertSmearTests(patient);
        assertTreatment(patient);
    }

    private void assertBasicPatientInfo(Patient patient) {
        assertEquals("caseId", patient.getPatientId());
        assertEquals("Foo", patient.getFirstName());
        assertEquals("Bar", patient.getLastName());
        assertEquals(Gender.Male, patient.getGender());
        assertEquals(PatientType.PHSTransfer, patient.getPatientType());
    }

    private void assertSmearTests(Patient patient) {
        assertEquals("Pre-treatment1", patient.getSmearTestResult().getSampleInstance1());
        assertEquals("result1", patient.getSmearTestResult().getResult1());
        assertEquals(DateUtil.today().minusDays(10), patient.getSmearTestResult().getTestDate1());
        assertEquals("Pre-treatment2", patient.getSmearTestResult().getSampleInstance2());
        assertEquals(DateUtil.today().minusDays(5), patient.getSmearTestResult().getTestDate2());
        assertEquals("result2", patient.getSmearTestResult().getResult2());
    }

    private void assertTreatment(Patient patient) {
        assertEquals(1, patient.getTreatments().size());
        Treatment treatment = patient.getTreatments().get(0);
        assertEquals(Category.Category1, treatment.getCategory());
        assertEquals("providerId01seq1", treatment.getCurrentTBId().getTbId());
        assertEquals("providerId", treatment.getCurrentTBId().getProviderId());
        assertEquals(DateUtil.today(), treatment.getCurrentTBId().getStartDate());
        assertEquals("registrationNumber", treatment.getRegistrationNumber());
        assertEquals(DateUtil.today(), treatment.getRegistrationDate());
    }

}
