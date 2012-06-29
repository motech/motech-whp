package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class RestartTreatmentStep extends Step {

    protected CaseDataService caseDataService;

    public RestartTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public void execute(TestPatient testPatient, String restartDate, String reason) {
        String restartTreatmentRequest = CaseUpdate.RestartTreatmentRequest(testPatient.getCaseId(), restartDate, testPatient.getTbId(), reason);
        caseDataService.updateCase(restartTreatmentRequest);

    }

}
