package org.motechproject.whp.functional.steps.treatmentcard;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class OpenTreatmentCardStep extends Step {


    private TestPatient testPatient;

    public OpenTreatmentCardStep(WebDriver webDriver) {
        super(webDriver);
    }

    public OpenTreatmentCardStep withPatient(TestPatient testPatient) {
        this.testPatient = testPatient;
        return this;
    }

    @Override
    public TreatmentCardPage execute() {
        ListAllPatientsPage listPatientsPage = loginAsCMFAdminAndListAllPatients();
        return listPatientsPage.clickOnPatientWithTherapyStarted(testPatient.getCaseId());
    }

    private ListAllPatientsPage loginAsCMFAdminAndListAllPatients() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().navigateToShowAllPatients();
    }
}
