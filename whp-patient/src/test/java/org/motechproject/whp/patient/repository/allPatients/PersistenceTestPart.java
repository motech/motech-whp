package org.motechproject.whp.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PersistenceTestPart extends AllPatientsTestPart {

    @Test
    public void shouldSavePatientInfo() {
        createPatient("cha01100001", "providerId");

        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        assertNotNull(savedPatient);
        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());

        SmearTestRecord smearTestRecord = savedPatient.getCurrentTreatment().getSmearTestResults().latestResult();
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.today(), smearTestRecord.getSmear_test_date_1());
    }

    @Test
    public void shouldSaveProfileFields() {
        createPatient("cha01100001", "providerId");
        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        assertEquals("Raju", savedPatient.getFirstName());
        assertEquals("Singh", savedPatient.getLastName());
        assertEquals(Gender.M, savedPatient.getGender());
    }

    @Test
    public void shouldSaveSmearTestResults() {
        createPatient("cha01100001", "providerId");
        Patient savedPatient = allPatients.findByPatientId("cha01100001");

        SmearTestRecord smearTestRecord = savedPatient.getCurrentTreatment().getSmearTestResults().latestResult();
        assertEquals(SmearTestSampleInstance.PreTreatment, smearTestRecord.getSmear_sample_instance());
        assertEquals(SmearTestResult.Positive, smearTestRecord.getSmear_test_result_1());
        assertEquals(DateUtil.today(), smearTestRecord.getSmear_test_date_1());
    }
}
