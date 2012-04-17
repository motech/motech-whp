package org.motechproject.whp.patient.repository;

import org.junit.Test;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/12/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllPatientsTest extends SpringIntegrationTest {
    @Autowired
    AllPatients allPatients;

    @Test
    public void shouldSavePatientInfo() {
        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.Male);
        allPatients.add(patient);

        Patient patientReturned = allPatients.findByPatientId("cha01100001");
        assertNotNull(patientReturned);
        assertEquals("Raju", patientReturned.getFirstName());
        assertEquals("Singh", patientReturned.getLastName());
        assertEquals(Gender.Male, patientReturned.getGender());
    }

}
