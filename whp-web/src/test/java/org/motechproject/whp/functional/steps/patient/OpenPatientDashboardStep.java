package org.motechproject.whp.functional.steps.patient;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.steps.LoginAsCmfAdminStep;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class OpenPatientDashboardStep extends Step {

    public PatientDashboardPage patientDashboardPage;

    LoginAsCmfAdminStep loginAsCmfAdminStep;

    public OpenPatientDashboardStep(WebDriver webDriver) {
        super(webDriver);
        loginAsCmfAdminStep = new LoginAsCmfAdminStep(this.webDriver);
    }

    public void execute(TestPatient testPatient) {
        loginAsCmfAdminStep.execute();
        patientDashboardPage = loginAsCmfAdminStep.listAllPatientsPage.clickOnPatientWithTherapyNotYetStarted(testPatient.getCaseId());
    }

}
