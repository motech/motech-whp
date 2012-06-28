package org.motechproject.whp.patient.repository.allPatients;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.whp.common.utils.SpringIntegrationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PersistenceTestPart.class,
        SearchByPatientIdTestPart.class,
        SearchByProviderIdTestPart.class
})
public class AllPatientsIT extends SpringIntegrationTest {
}
