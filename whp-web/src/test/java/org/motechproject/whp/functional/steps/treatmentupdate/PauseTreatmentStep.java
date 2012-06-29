package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class PauseTreatmentStep extends Step {

    protected CaseDataService caseDataService;

    public PauseTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public void execute(TestPatient testPatient, String pauseDate, String reason) {
        String pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), pauseDate, testPatient.getTbId(), reason);
        caseDataService.updateCase(pauseTreatmentRequest);
    }

}
