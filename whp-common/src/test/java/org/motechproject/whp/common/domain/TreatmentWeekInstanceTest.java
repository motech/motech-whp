package org.motechproject.whp.common.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.service.AdherencePropertyValues;
import org.motechproject.whp.common.service.AdherenceWindow;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.model.DayOfWeek.*;

public class TreatmentWeekInstanceTest extends BaseUnitTest{

    TreatmentWeekInstance treatmentWeekInstance;

    AdherenceWindow adherenceWindow;

    @Mock
    AdherencePropertyValues adherencePropertyValues;

    @Before
    public void setUp() {
        initMocks(this);
        when(adherencePropertyValues.validAdherenceDays()).thenReturn(asList(Sunday, Monday, Tuesday));
        adherenceWindow = new AdherenceWindow(adherencePropertyValues);
        treatmentWeekInstance = new TreatmentWeekInstance(adherenceWindow);
    }

    @Test
    public void shouldFetchPreviousAdherenceCaptureWeek() {
        LocalDate friday = new LocalDate(2013, 01, 18);
        LocalDate lastSunday = friday.minusDays(5);
        LocalDate previousWeekSunday = friday.minusDays(12);

        mockCurrentDate(friday); //Fri
        assertEquals(lastSunday, treatmentWeekInstance.previousAdherenceWeekEndDate());

        mockCurrentDate(friday.minusDays(2)); //Wed
        assertEquals(lastSunday, treatmentWeekInstance.previousAdherenceWeekEndDate());

        mockCurrentDate(friday.minusDays(3)); //Tue
        assertEquals(previousWeekSunday, treatmentWeekInstance.previousAdherenceWeekEndDate());

        mockCurrentDate(friday.minusDays(4)); //Mon
        assertEquals(previousWeekSunday, treatmentWeekInstance.previousAdherenceWeekEndDate());
    }
}
