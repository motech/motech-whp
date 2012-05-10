package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;

import static org.junit.Assert.*;
import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceMapperTest {

    private LocalDate today = DateUtil.today();

    @Test
    public void shouldSetIsTaken() {
        AdherenceRecord record = new AdherenceRecord(today, 1, null);
        assertTrue(new AdherenceMapper(record).map().getPillStatus() == PillStatus.Taken);
    }

    @Test
    public void shouldNotSetIsTaken() {
        AdherenceRecord record = new AdherenceRecord(today, 0, null);
        assertFalse(new AdherenceMapper(record).map().getPillStatus() == PillStatus.Taken);
    }

    @Test
    public void shouldSetPillDate() {
        AdherenceRecord record = new AdherenceRecord(today, 0, null);
        assertEquals(today, new AdherenceMapper(record).map().getPillDate());
    }

    @Test
    public void shouldSetPillDay() {
        AdherenceRecord record = new AdherenceRecord(today, 0, null);
        assertEquals(DayOfWeek.getDayOfWeek(today), new AdherenceMapper(record).map().getPillDay());
    }

}
