package org.motechproject.whp.it.container.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommonTestPart.class,
        InTreatmentTestPart.class,
        PreTreatmentTestPart.class
})
public class AllContainerTrackingRecordsIT {
}
