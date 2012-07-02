package org.motechproject.whp.functional.assertions.treatmentcard;

import org.motechproject.whp.functional.page.admin.PatientDashboardPage;

import static org.junit.Assert.assertEquals;

public class PhaseDates {

    public static void is(PatientDashboardPage patientDashboardPage, String ipStartDate, String eipStartDate, String cpStartDate) {
        patientDashboardPage.clickOnChangePhaseStartDates();
        assertEquals(ipStartDate, patientDashboardPage.getIpStartDate());
        assertEquals(eipStartDate, patientDashboardPage.getEIpStartDate());
        assertEquals(cpStartDate, patientDashboardPage.getCpStartDate());
        patientDashboardPage.saveStartDates();
    }

}
