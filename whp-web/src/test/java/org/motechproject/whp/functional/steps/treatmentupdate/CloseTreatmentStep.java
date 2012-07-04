package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class CloseTreatmentStep extends Step {

    protected CaseDataService caseDataService;
    private TestPatient testPatient;
    private String closeDate;

    public CloseTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public CloseTreatmentStep withPatient(TestPatient testPatient) {
        this.testPatient = testPatient;
        return this;
    }

    public CloseTreatmentStep withCloseDate(String closeDate) {
        this.closeDate = closeDate;
        return this;
    }

    @Override
    public Page execute() {
        String closeTreatmentRequest = CaseUpdate.CloseTreatmentRequest(testPatient.getCaseId(), closeDate, testPatient.getTbId());
        caseDataService.updateCase(closeTreatmentRequest);
        return null;
    }
}
