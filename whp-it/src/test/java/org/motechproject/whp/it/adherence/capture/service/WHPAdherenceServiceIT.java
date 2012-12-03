package org.motechproject.whp.it.adherence.capture.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuditAdherenceTestPart.class,
        RecordAdherenceTestPart.class,
        StartPatientOnTreatmentTestPart.class,
        PatientsWithAdherenceTestPart.class
})
public class WHPAdherenceServiceIT {
}
