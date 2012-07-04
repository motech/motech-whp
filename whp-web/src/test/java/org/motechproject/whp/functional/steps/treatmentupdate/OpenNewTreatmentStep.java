package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class OpenNewTreatmentStep extends Step {

    private CaseDataService caseDataService;
    private TestProvider testProvider;
    private TestPatient testPatient;

    public OpenNewTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public OpenNewTreatmentStep withPatient(TestPatient testPatient) {
        this.testPatient = testPatient;
        return this;
    }

    public OpenNewTreatmentStep withProvider(TestProvider testProvider) {
        this.testProvider = testProvider;
        return this;
    }

    @Override
    public Page execute() {
        String closeTreatmentRequest = CaseUpdate.OpenNewTreatmentRequest(testPatient.getCaseId(), testProvider.getProviderId());
        caseDataService.updateCase(closeTreatmentRequest);
        return null;
    }
}
