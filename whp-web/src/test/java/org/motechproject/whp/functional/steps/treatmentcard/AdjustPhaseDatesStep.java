package org.motechproject.whp.functional.steps.treatmentcard;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.steps.Step;
import org.motechproject.whp.functional.steps.patient.OpenPatientDashboardStep;
import org.openqa.selenium.WebDriver;

public class AdjustPhaseDatesStep extends Step {

    public PatientDashboardPage patientDashboardPage;

    OpenPatientDashboardStep openPatientDashboardStep;

    public AdjustPhaseDatesStep(WebDriver webDriver) {
        super(webDriver);
        openPatientDashboardStep = new OpenPatientDashboardStep(webDriver);
    }

    public void execute(TestPatient testPatient, String ipStartDate, String eipStartDate, String cpStartDate) {
        openPatientDashboardStep.execute(testPatient);
        patientDashboardPage = openPatientDashboardStep.patientDashboardPage;
        patientDashboardPage.clickOnChangePhaseStartDates();
        patientDashboardPage.editStartDates(ipStartDate, eipStartDate, cpStartDate);
        patientDashboardPage = patientDashboardPage.saveStartDates();
    }

}
