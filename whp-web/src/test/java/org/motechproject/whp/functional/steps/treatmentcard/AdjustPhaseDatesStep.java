package org.motechproject.whp.functional.steps.treatmentcard;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.steps.Step;
import org.motechproject.whp.functional.steps.patient.OpenPatientDashboardStep;
import org.openqa.selenium.WebDriver;

public class AdjustPhaseDatesStep extends Step {

    private OpenPatientDashboardStep openPatientDashboardStep;
    private TestPatient patient;
    private String ipStartDate;
    private String eipStartDate;
    private String cpStartDate;

    public AdjustPhaseDatesStep(WebDriver webDriver) {
        super(webDriver);
        openPatientDashboardStep = new OpenPatientDashboardStep(webDriver);
    }

    public AdjustPhaseDatesStep withPatient(TestPatient patient) {
        this.patient = patient;
        return this;
    }

    public AdjustPhaseDatesStep withIpStartDate(String ipStartDate) {
        this.ipStartDate = ipStartDate;
        return this;
    }

    public AdjustPhaseDatesStep withEipStartDate(String eipStartDate) {
        this.eipStartDate = eipStartDate;
        return this;
    }

    public AdjustPhaseDatesStep withCpStartDate(String cpStartDate) {
        this.cpStartDate = cpStartDate;
        return this;
    }

    @Override
    public PatientDashboardPage execute() {
        PatientDashboardPage patientDashboardPage = openPatientDashboardStep.withPatient(patient).execute();
        patientDashboardPage.clickOnChangePhaseStartDates();
        patientDashboardPage.editStartDates(ipStartDate, eipStartDate, cpStartDate);
        return patientDashboardPage.saveStartDates();
    }
}
