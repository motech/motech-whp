package org.motechproject.whp.functional.test.therapy;

import org.junit.Test;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.test.BasePatientTest;

import static org.junit.Assert.assertEquals;

public class AdjustPhaseDatesTest extends BasePatientTest {

    private PatientDashboardPage patientDashboardPage;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        setupPatientForProvider();
        patientDashboardPage = showPatient();
    }

    @Test
    public void shouldAdjustPhaseStartDates() {
        String ipStartDate = "25/10/2011";
        String eipStartDate = "26/10/2011";
        String cpStartDate = "27/10/2011";

        adjustPhaseStartDates(ipStartDate, eipStartDate, cpStartDate);
        verifyPhaseStartDates(ipStartDate, eipStartDate, cpStartDate);
    }

    private void verifyPhaseStartDates(String ipStartDate, String eipStartDate, String cpStartDate) {
        patientDashboardPage.clickOnChangePhaseStartDates();
        assertEquals(ipStartDate, patientDashboardPage.getIpStartDate());
        assertEquals(eipStartDate, patientDashboardPage.getEIpStartDate());
        assertEquals(cpStartDate, patientDashboardPage.getCpStartDate());
        patientDashboardPage = patientDashboardPage.saveStartDates();
    }

    private void adjustPhaseStartDates(String ipStartDate, String eipStartDate, String cpStartDate) {
        patientDashboardPage.clickOnChangePhaseStartDates();
        patientDashboardPage.editStartDates(ipStartDate, eipStartDate, cpStartDate);
        patientDashboardPage = patientDashboardPage.saveStartDates();
    }

    private PatientDashboardPage showPatient() {
        ListAllPatientsPage listPatientsPage = loginAsCMFAdmin();
        return listPatientsPage.clickOnPatientWithTherapyNotYetStarted(testPatient.getCaseId());
    }
}
