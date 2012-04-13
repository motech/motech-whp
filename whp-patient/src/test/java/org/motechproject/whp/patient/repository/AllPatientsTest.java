package org.motechproject.whp.patient.repository;

import junit.framework.Assert;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

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
    public void shouldSavePatientInfo(){
        Patient patient = new Patient("cha01100001", "cha011", "01", "cha01102001", "Raju", "Singh", "M");
        allPatients.add(patient);

        List<Patient> patients = allPatients.getAll();
        Assert.assertTrue(patients.size() > 0);
    }

}
