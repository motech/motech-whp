package org.motechproject.whp.functional.test.therapy;

import org.junit.Test;
import org.motechproject.whp.functional.assertions.treatmentcard.PhaseDates;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.steps.treatmentcard.AdjustPhaseDatesStep;
import org.motechproject.whp.functional.test.BasePatientTest;

public class AdjustPhaseDatesTest extends BasePatientTest {

    AdjustPhaseDatesStep adjustPhaseDatesStep;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        setupPatientForProvider();
        adjustPhaseDatesStep = new AdjustPhaseDatesStep(webDriver);
    }

    @Test
    public void shouldAdjustPhaseStartDates() {
        String ipStartDate = "25/10/2011";
        String eipStartDate = "26/10/2011";
        String cpStartDate = "27/10/2011";

        PatientDashboardPage dashboardPage = adjustPhaseDatesStep
                .withPatient(testPatient)
                .withIpStartDate(ipStartDate)
                .withEipStartDate(eipStartDate)
                .withCpStartDate(cpStartDate)
                .execute();
        PhaseDates.is(dashboardPage, ipStartDate, eipStartDate, cpStartDate);
        dashboardPage.logout();
    }

}
