package org.motechproject.whp.adherence.request;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DailyAdherenceRequestsTest {

    @Test
    public void shouldFindMaxDoseDate() {
        DailyAdherenceRequest dailyAdherenceRequestWithMaxDoseDate = new DailyAdherenceRequest(17, 5, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest2 = new DailyAdherenceRequest(9, 5, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest3 = new DailyAdherenceRequest(12, 5, 2012, 1);

        List<DailyAdherenceRequest> dailyAdherenceRequests = asList(dailyAdherenceRequestWithMaxDoseDate, dailyAdherenceRequest2, dailyAdherenceRequest3);

        assertEquals(dailyAdherenceRequestWithMaxDoseDate.getDoseDate(), new DailyAdherenceRequests(dailyAdherenceRequests).maxDoseDate());
    }

    @Test
    public void shouldCheckIfEmpty() {
        assertTrue(new DailyAdherenceRequests(new ArrayList<DailyAdherenceRequest>()).isEmpty());
        assertFalse(new DailyAdherenceRequests(asList(new DailyAdherenceRequest(12, 5, 2012, 1))).isEmpty());
    }

    @Test
    public void shouldIterateThroughDailyAdherenceRequests() {
        DailyAdherenceRequest dailyAdherenceRequest1 = new DailyAdherenceRequest(17, 5, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest2 = new DailyAdherenceRequest(9, 5, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest3 = new DailyAdherenceRequest(12, 5, 2012, 1);

        List<DailyAdherenceRequest> expectedList = asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3);

        DailyAdherenceRequests dailyAdherenceRequests = new DailyAdherenceRequests(expectedList);

        List<DailyAdherenceRequest> list = new ArrayList<>();

        for(DailyAdherenceRequest dailyAdherenceRequest : dailyAdherenceRequests){
            list.add(dailyAdherenceRequest);
        }

        assertEquals(expectedList, list);
    }
}

