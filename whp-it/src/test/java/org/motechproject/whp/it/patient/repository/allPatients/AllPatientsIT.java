package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.whp.common.util.SpringIntegrationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PatientRepositoryTestPart.class,
        SearchByPatientIdTestPart.class,
        SearchByProviderIdTestPart.class,
        SearchByActivePatientsTestPart.class,
        PatientFilterTestPart.class
})
public class AllPatientsIT extends SpringIntegrationTest {
}
