package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class CloseTreatmentStep extends Step {

    protected CaseDataService caseDataService;

    public CloseTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public void execute(TestPatient testPatient, String closeDate) {
        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), closeDate, testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);
    }

}
