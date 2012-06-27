package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.TherapyStatus;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class TherapyTest {

    @Test
    public void shouldCloseTherapy() {
        Therapy therapy = new Therapy();
        therapy.close(now());
        assertEquals(today(), therapy.getCloseDate());
        assertEquals(TherapyStatus.Closed, therapy.getStatus());
    }

    @Test
    public void shouldStartTherapy() {
        Therapy therapy = new Therapy();
        LocalDate today = today();

        therapy.start(today);

        assertEquals(today, therapy.getStartDate());
        assertEquals(today, therapy.getPhases().getByPhaseName(PhaseName.IP).getStartDate());
        assertEquals(TherapyStatus.Ongoing, therapy.getStatus());
    }

    @Test
    public void shouldFetchPhaseByName() {
        Therapy therapy = new Therapy();
        assertEquals(new Phase(PhaseName.IP), therapy.getPhase(PhaseName.IP));
    }
}
