package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdherenceUpdateTestPart.class,
        SetNextPhaseTestPart.class,
        PhaseTransitionTestPart.class,
        AdjustPhaseDatesTestPart.class,
        DoseInterruptionsTestPart.class
})
public class TreatmentUpdateOrchestratorTest {
}
