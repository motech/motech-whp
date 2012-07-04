package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part.AdjustPhaseDatesTestPart;
import org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part.PhaseTransitionTestPart;
import org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part.PillTakenCountTestPart;
import org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part.SetNextPhaseTestPart;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PillTakenCountTestPart.class,
        SetNextPhaseTestPart.class,
        PhaseTransitionTestPart.class,
        AdjustPhaseDatesTestPart.class
})
public class PhaseUpdateOrchestratorTest {
}
