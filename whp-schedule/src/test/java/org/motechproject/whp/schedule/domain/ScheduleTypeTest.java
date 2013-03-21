package org.motechproject.whp.schedule.domain;

import org.junit.Test;
import org.motechproject.whp.common.event.EventKeys;

import static org.junit.Assert.assertEquals;

public class ScheduleTypeTest {
    @Test
    public void shouldGetEventSubjectForGivenJobType() {
        assertEquals(EventKeys.ADHERENCE_WINDOW_COMMENCED_EVENT_NAME, ScheduleType.ADHERENCE_WINDOW_COMMENCED.getEventSubject());
    }
}
