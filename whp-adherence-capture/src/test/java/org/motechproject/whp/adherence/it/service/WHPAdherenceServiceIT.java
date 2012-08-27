package org.motechproject.whp.adherence.it.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuditAdherenceTestPart.class,
        RecordAdherenceTestPart.class,
        StartPatientOnTreatmentTestPart.class
})
public class WHPAdherenceServiceIT {
}
