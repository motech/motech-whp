package org.motechproject.whp.functional.steps.provideradherence;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.page.provider.UpdateAdherencePage;
import org.motechproject.whp.functional.steps.LoginAsProviderStep;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class SubmitAdherenceStep extends Step {

    private LoginAsProviderStep loginAsProviderStep;
    private TestProvider testProvider;
    private TestPatient patient;
    private int dosesTaken;

    public SubmitAdherenceStep(WebDriver webDriver) {
        super(webDriver);
        loginAsProviderStep = new LoginAsProviderStep(this.webDriver);
    }

    public SubmitAdherenceStep withProvider(TestProvider testProvider) {
        this.testProvider = testProvider;
        return this;
    }

    public SubmitAdherenceStep withPatient(TestPatient patient) {
        this.patient = patient;
        return this;
    }

    public SubmitAdherenceStep withDosesTaken(int dosesTaken) {
        this.dosesTaken = dosesTaken;
        return this;
    }

    @Override
    public Page execute() {
        UpdateAdherencePage updateAdherencePage = loginAsProviderStep.withProvider(testProvider).execute().clickEditAdherenceLink(patient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(dosesTaken);
        ProviderPage providerPage = updateAdherencePage.submit();
        providerPage.logout();
        return null;
    }
}
