package org.motechproject.whp.functional.steps.treatmentcard;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.ListPatientsPage;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.TreatmentCardPage;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class OpenTreatmentCardStep extends Step {

    public TreatmentCardPage treatmentCardPage;

    public OpenTreatmentCardStep(WebDriver webDriver) {
        super(webDriver);
    }

    public void execute(TestPatient testPatient) {
        ListPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();
        treatmentCardPage = listPatientsPage.clickOnPatientWithStartedTherapy(testPatient.getCaseId());
    }

    private ListPatientsPage loginAsCMFAdminAndListAllPatients() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}
