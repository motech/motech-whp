package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientFlag;
import org.motechproject.whp.patient.domain.SmearTestRecord;

import static junit.framework.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PatientRepositoryTestPart extends AllPatientsTestPart {

    @Test
    public void shouldSavePatientInfo() {
        createPatient("cha01100001", "providerId", PROVIDER_DISTRICT);

        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        assertNotNull(savedPatient);
        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());

        SmearTestRecord smearTestRecord = savedPatient.getCurrentTreatment().getSmearTestResults().latestResult();
        assertEquals(SputumTrackingInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.today(), smearTestRecord.getSmear_test_date_1());
    }

    @Test
    public void shouldSaveProfileFields() {
        createPatient("cha01100001", "providerId", PROVIDER_DISTRICT);
        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());
    }

    @Test
    public void shouldSaveSmearTestResults() {
        createPatient("cha01100001", "providerId", PROVIDER_DISTRICT);
        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        SmearTestRecord smearTestRecord = savedPatient.getCurrentTreatment().getSmearTestResults().latestResult();
        assertEquals(SputumTrackingInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.today(), smearTestRecord.getSmear_test_date_1());
    }

    @Test
    public void shouldSaveAndRetrieveUpdateFlag(){
        Patient patient = createPatient("cha01100001", "providerId", PROVIDER_DISTRICT);
        PatientFlag patientFlag = new PatientFlag();
        patientFlag.setFlagValue(true);
        patient.setPatientFlag(patientFlag);
        allPatients.update(patient);

        Patient savedPatient = allPatients.findByPatientId(patient.getPatientId());

        assertTrue(savedPatient.getPatientFlag().isFlagSet());
    }

    @Test
    public void shouldGetAllPatientsForGivenPageNumberAndPageSize() {
        createPatient("patient1", "provider", "district");
        createPatient("patient2", "provider", "district");
        createPatient("patient3", "provider", "district");

        assertThat(allPatients.getAll(0, 3).size(), is(3));
        assertThat(allPatients.getAll(1, 2).size(), is(1));
        assertThat(allPatients.getAll(2, 1).size(), is(1));
        assertThat(allPatients.getAll(3, 2).size(), is(0));
    }
}
