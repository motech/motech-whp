package org.motechproject.whp.functional.steps.treatmentupdate;

import org.motechproject.whp.functional.data.CaseUpdate;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.service.CaseDataService;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class TransferInTreatmentStep extends Step {

    protected CaseDataService caseDataService;
    private TestProvider newProvider;
    private TestPatient testPatient;
    private String transferDate;

    public TransferInTreatmentStep(WebDriver webDriver) {
        super(webDriver);
        caseDataService = new CaseDataService(webDriver);
    }

    public TransferInTreatmentStep withProvider(TestProvider newProvider) {
        this.newProvider = newProvider;
        return this;
    }

    public TransferInTreatmentStep withPatient(TestPatient testPatient) {
        this.testPatient = testPatient;
        return this;
    }

    public TransferInTreatmentStep withTransferDate(String transferDate) {
        this.transferDate = transferDate;
        return this;
    }

    @Override
    public Page execute() {
        String transferInPatientRequest = CaseUpdate.TransferInPatientRequest(testPatient.getCaseId(), transferDate, testPatient.getTbId(), "01", newProvider.getProviderId());
        caseDataService.updateCase(transferInPatientRequest);
        return null;
    }

}
