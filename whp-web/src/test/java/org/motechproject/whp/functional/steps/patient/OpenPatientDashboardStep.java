package org.motechproject.whp.functional.steps.patient;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.steps.LoginAsCmfAdminStep;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

public class OpenPatientDashboardStep extends Step {

    private LoginAsCmfAdminStep loginAsCmfAdminStep;
    private TestPatient patient;

    public OpenPatientDashboardStep(WebDriver webDriver) {
        super(webDriver);
        loginAsCmfAdminStep = new LoginAsCmfAdminStep(this.webDriver);
    }

    public OpenPatientDashboardStep withPatient(TestPatient patient) {
        this.patient = patient;
        return this;
    }

    @Override
    public PatientDashboardPage execute() {
        ListAllPatientsPage listAllPatientsPage = loginAsCmfAdminStep.execute();
        return listAllPatientsPage.clickOnPatientWithTherapyNotYetStarted(patient.getCaseId());
    }
}
