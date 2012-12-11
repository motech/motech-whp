package org.motechproject.whp.providerreminder.domain;

import org.junit.Test;
import org.motechproject.whp.common.event.EventKeys;

import static org.junit.Assert.assertEquals;

public class ProviderReminderTypeTest {
    @Test
    public void shouldGetEventSubjectForGivenJobType() {
        assertEquals(EventKeys.ADHERENCE_WINDOW_APPROACHING_EVENT_NAME, ProviderReminderType.ADHERENCE_WINDOW_APPROACHING.getEventSubject());
    }
}
