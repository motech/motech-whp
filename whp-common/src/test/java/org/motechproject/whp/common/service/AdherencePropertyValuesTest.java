package org.motechproject.whp.common.service;


import org.junit.Test;
import org.motechproject.whp.common.service.AdherencePropertyValues;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.*;

public class AdherencePropertyValuesTest {

    @Test
    public void shouldReturnValidAdherenceDayList() {
        List<String> adherenceDays = asList("7", "1", "2");

        AdherencePropertyValues adherenceProperties = new AdherencePropertyValues(adherenceDays);

        assertEquals(asList(Sunday, Monday, Tuesday), adherenceProperties.validAdherenceDays());
    }
}
