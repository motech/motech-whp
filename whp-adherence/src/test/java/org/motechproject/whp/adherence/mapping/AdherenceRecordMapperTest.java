package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;

import static org.junit.Assert.*;
import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceRecordMapperTest {

    private LocalDate today = DateUtil.today();

    @Test
    public void shouldSetIsTaken() {
        AdherenceRecord record = new AdherenceRecord(today, 1, 1, null);
        assertTrue(new AdherenceRecordMapper(record).adherenceLog().getIsTaken());
    }

    @Test
    public void shouldNotSetIsTaken() {
        AdherenceRecord record = new AdherenceRecord(today, 0, 1, null);
        assertFalse(new AdherenceRecordMapper(record).adherenceLog().getIsTaken());
    }

    @Test
    public void shouldSetPillDate() {
        AdherenceRecord record = new AdherenceRecord(today, 0, 1, null);
        assertEquals(today, new AdherenceRecordMapper(record).adherenceLog().getPillDate());
    }

    @Test
    public void shouldSetPillDay() {
        AdherenceRecord record = new AdherenceRecord(today, 0, 1, null);
        assertEquals(DayOfWeek.getDayOfWeek(today), new AdherenceRecordMapper(record).adherenceLog().getPillDay());
    }

}
