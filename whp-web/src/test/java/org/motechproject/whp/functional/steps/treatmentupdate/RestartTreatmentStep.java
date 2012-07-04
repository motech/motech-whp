package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class RestartTreatmentStep extends Step {

    protected CaseDataService caseDataService;
    private TestPatient testPatient;
    private String restartDate;
    private String reason;

    public RestartTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public RestartTreatmentStep withPatient(TestPatient testPatient) {
        this.testPatient = testPatient;
        return this;
    }

    public RestartTreatmentStep withRestartDate(String restartDate) {
        this.restartDate = restartDate;
        return this;
    }

    public RestartTreatmentStep withReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Page execute() {
        String restartTreatmentRequest = CaseUpdate.RestartTreatmentRequest(testPatient.getCaseId(), restartDate, testPatient.getTbId(), reason);
        caseDataService.updateCase(restartTreatmentRequest);
        return null;
    }
}
