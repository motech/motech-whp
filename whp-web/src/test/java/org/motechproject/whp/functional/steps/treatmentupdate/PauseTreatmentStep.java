package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class PauseTreatmentStep extends Step {

    protected CaseDataService caseDataService;
    private TestPatient testPatient;
    private String pauseDate;
    private String reason;

    public PauseTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public PauseTreatmentStep withPatient(TestPatient testPatient) {
        this.testPatient = testPatient;
        return this;
    }

    public PauseTreatmentStep withPauseDate(String pauseDate) {
        this.pauseDate = pauseDate;
        return this;
    }

    public PauseTreatmentStep withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public Page execute() {
        String pauseTreatmentRequest = CaseUpdate.PauseTreatmentRequest(testPatient.getCaseId(), pauseDate, testPatient.getTbId(), reason);
        caseDataService.updateCase(pauseTreatmentRequest);
        return null;
    }
}
