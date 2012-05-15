package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceMapperTest {

    private LocalDate today = DateUtil.today();

    private AdherenceData createAdherenceData() {
        AdherenceData adherenceData = new AdherenceData(null, null, today);
        adherenceData.addMeta(AdherenceConstants.TB_ID, "tbId");
        adherenceData.addMeta(AdherenceConstants.PROVIDER_ID, "providerId");
        return adherenceData;
    }

    @Test
    public void shouldSetIsTaken() {
        AdherenceData adherenceData = createAdherenceData();
        adherenceData.status(PillStatus.Taken.getStatus());
        assertTrue(new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillStatus() == PillStatus.Taken);
    }

    @Test
    public void shouldNotSetIsTaken() {
        AdherenceData adherenceData = createAdherenceData();
        adherenceData.status(PillStatus.NotTaken.getStatus());
        assertFalse(new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillStatus() == PillStatus.Taken);
    }

    @Test
    public void shouldSetPillDate() {
        AdherenceData adherenceData = createAdherenceData();
        assertEquals(today, new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillDate());
    }

    @Test
    public void shouldSetPillDay() {
        AdherenceData adherenceData = createAdherenceData();
        assertEquals(DayOfWeek.getDayOfWeek(today), new AdherenceMapper().map(Arrays.asList(adherenceData)).get(0).getPillDay());
    }

}
