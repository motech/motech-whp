package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class TransferInTreatmentStep extends Step {

    protected CaseDataService caseDataService;

    public TransferInTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public void execute(TestProvider newProvider, TestPatient testPatient, String transferDate) {
        String transferInPatientRequest = CaseUpdate.TransferInPatientRequest(testPatient.getCaseId(), transferDate, testPatient.getTbId(), "01", newProvider.getProviderId());
        caseDataService.updateCase(transferInPatientRequest);
    }

}
