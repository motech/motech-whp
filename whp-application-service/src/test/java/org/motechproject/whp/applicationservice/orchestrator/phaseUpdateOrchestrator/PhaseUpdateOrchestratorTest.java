package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdherenceUpdateTestPart.class,
        PillTakenCountTestPart.class,
        SetNextPhaseTestPart.class,
        PhaseTransitionTestPart.class,
        AdjustPhaseDatesTestPart.class,
        DoseInterruptionsTestPart.class
})
public class PhaseUpdateOrchestratorTest {
}
