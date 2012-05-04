package org.motechproject.whp.adherence.mapping;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;

import static org.junit.Assert.*;
import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceRecordMapperTest {

    private DateTime today = DateUtil.now();

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
        assertEquals(today.toLocalDate(), new AdherenceRecordMapper(record).adherenceLog().getPillDate());
    }

    @Test
    public void shouldSetPillDay() {
        AdherenceRecord record = new AdherenceRecord(today, 0, 1, null);
        assertEquals(DayOfWeek.getDayOfWeek(today.toLocalDate()), new AdherenceRecordMapper(record).adherenceLog().getPillDay());
    }

}
