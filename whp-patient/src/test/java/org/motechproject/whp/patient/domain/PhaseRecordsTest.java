package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.common.domain.Phase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.motechproject.util.DateUtil.today;

public class PhaseRecordsTest {

    private LocalDate today = today();

    @Test
    public void shouldRecordStartDateOfPhaseUponTransitionToPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today);
        assertEquals(today, records.get(Phase.IP).getStartDate());
    }

    @Test
    public void shouldRecordEndDateOfPhaseUponTransitionFromPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today.minusDays(1));
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.CP, null), today);

        assertEquals(today.minusDays(1), records.get(Phase.IP).getEndDate());
    }

    @Test
    public void shouldAdjustEndDateOfPhaseUponTransitionToDifferentPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today.minusDays(2));
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.CP, null), today);
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP), today.minusDays(1));

        assertEquals(today.minusDays(2), records.get(Phase.IP).getEndDate());
    }

    @Test
    public void shouldAdjustEndDateOnNewPhaseInsertedBetweenToPhases() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today.minusDays(2));
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.CP, null), today);
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP), today.minusDays(1));

        assertEquals(today.minusDays(1), records.get(Phase.EIP).getEndDate());
    }

    @Test
    public void shouldAdjustEndDateUponChangeInTheStartingPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.EIP, null), today);
        records.recordTransition(new PhaseTransition(null, Phase.IP, Phase.EIP), today.minusDays(2));

        assertEquals(today.minusDays(1), records.get(Phase.IP).getEndDate());
    }

    @Test
    public void shouldRemoveTransitionToPhase() {
        PhaseRecords records = new PhaseRecords();
        records.removeTransition(new PhaseTransition(null, Phase.IP, null));
        assertNull(records.get(Phase.IP).getEndDate());
    }

    @Test
    public void shouldAdjustEndDateUponRemovalOfTransitionFromPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today.minusDays(1));
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.CP, null), today);

        records.removeTransition(new PhaseTransition(Phase.IP, Phase.CP, null));
        assertNull(records.get(Phase.IP).getEndDate());
    }

    @Test
    public void shouldProlongPreviousPhaseUponRemovalOfTransitionToPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today.minusDays(2));
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.CP, null), today);
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP), today.minusDays(1));

        records.removeTransition(new PhaseTransition(Phase.IP, Phase.EIP, Phase.CP));
        assertEquals(today.minusDays(1), records.get(Phase.IP).getEndDate());
    }

    @Test
    public void shouldProlongSecondPhaseUponRemovalOfTransitionToFirstPhase() {
        PhaseRecords records = new PhaseRecords();
        records.recordTransition(new PhaseTransition(null, Phase.IP, null), today.minusDays(2));
        records.recordTransition(new PhaseTransition(Phase.IP, Phase.CP, null), today);

        records.removeTransition(new PhaseTransition(null, Phase.IP, Phase.CP));
        assertEquals(today.minusDays(2), records.get(Phase.CP).getStartDate());
    }
}
