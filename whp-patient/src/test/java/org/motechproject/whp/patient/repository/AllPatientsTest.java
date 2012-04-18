package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllPatientsTest extends SpringIntegrationTest {

    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
    }

    @Test
    public void shouldSavePatientInfo() {
        Treatment treatment = new Treatment(Category.Category1, DateUtil.today());
        allTreatments.add(treatment);

        Patient patient = new Patient("cha01100001", "Raju", "Singh", Gender.Male, PatientType.PHSTransfer, "1234567890");
        ProvidedTreatment providedTreatment = new ProvidedTreatment("providerId", "tdId", DateUtil.today());
        providedTreatment.setTreatment(treatment);
        patient.addProvidedTreatment(providedTreatment);
        allPatients.add(patient);

        Patient patientReturned = allPatients.findByPatientId("cha01100001");
        assertNotNull(patientReturned);
        assertEquals("Raju", patientReturned.getFirstName());
        assertEquals("Singh", patientReturned.getLastName());
        assertEquals(Gender.Male, patientReturned.getGender());
        assertEquals(PatientType.PHSTransfer, patientReturned.getPatientType());
    }

}
