package org.motechproject.whp.functional.steps.provideradherence;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.page.provider.UpdateAdherencePage;
import org.motechproject.whp.functional.steps.LoginAsProviderStep;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class SubmitAdherenceStep extends Step {

    LoginAsProviderStep loginAsProviderStep;

    public SubmitAdherenceStep(WebDriver webDriver) {
        super(webDriver);
        loginAsProviderStep = new LoginAsProviderStep(this.webDriver);
    }

    public void execute(TestProvider testProvider, TestPatient testPatient, int dosesTaken) {
        UpdateAdherencePage updateAdherencePage = loginAsProviderStep.execute(testProvider).clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(dosesTaken);
        ProviderPage providerPage = updateAdherencePage.submit();
        providerPage.logout();
    }


}
