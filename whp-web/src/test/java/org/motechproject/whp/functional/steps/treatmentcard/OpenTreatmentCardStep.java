package org.motechproject.whp.functional.steps.treatmentcard;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.page.provider.ListPatientsPage;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class OpenTreatmentCardStep extends Step {

    public TreatmentCardPage treatmentCardPage;

    public OpenTreatmentCardStep(WebDriver webDriver) {
        super(webDriver);
    }

    public void execute(TestPatient testPatient) {
        ListAllPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();
        treatmentCardPage = listPatientsPage.clickOnPatientWithStartedTherapy(testPatient.getCaseId());
    }

    private ListAllPatientsPage loginAsCMFAdminAndListAllPatients() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}
