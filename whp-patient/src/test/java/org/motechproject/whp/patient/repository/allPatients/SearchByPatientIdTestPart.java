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

public class SearchByPatientIdTestPart extends AllPatientsTestPart {

    @Test
    public void patientIdShouldBeCaseInsensitive() {
        createPatient("Cha01100002", "providerId");

        Patient savedPatient = allPatients.findByPatientId("chA01100002");
        assertNotNull(savedPatient);
    }

    @Test
    public void shouldSaveIdsInLowerCase() {
        createPatient("Cha01100002", "providerId");
        Patient savedPatient = allPatients.findByPatientId("chA01100002");

        assertEquals("tbid", savedPatient.getCurrentTreatment().getTbId());
        assertEquals("providerid", savedPatient.getCurrentTreatment().getProviderId());
    }

    @Test
    public void findByPatientIdShouldReturnNullIfKeywordIsNull() {
        assertEquals(null, allPatients.findByPatientId(null));
    }
}
