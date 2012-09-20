package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AdherenceMapperTest {

    private LocalDate today = DateUtil.today();

    private AdherenceRecord createAdherenceData() {
        AdherenceRecord adherenceData = new AdherenceRecord(null, null, today);
        adherenceData.tbId("tbId");
        adherenceData.providerId("providerId");
        return adherenceData;
    }

    @Test
    public void shouldSetIsTaken() {
        AdherenceRecord adherenceData = createAdherenceData();
        adherenceData.status(PillStatus.Taken.getStatus());
        assertTrue(new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillStatus() == PillStatus.Taken);
    }

    @Test
    public void shouldNotSetIsTaken() {
        AdherenceRecord adherenceData = createAdherenceData();
        adherenceData.status(PillStatus.NotTaken.getStatus());
        assertFalse(new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillStatus() == PillStatus.Taken);
    }

    @Test
    public void shouldSetPillDate() {
        AdherenceRecord adherenceData = createAdherenceData();
        assertEquals(today, new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillDate());
    }

    @Test
    public void shouldSetPillDay() {
        AdherenceRecord adherenceData = createAdherenceData();
        assertEquals(DayOfWeek.getDayOfWeek(today), new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillDay());
    }

}
