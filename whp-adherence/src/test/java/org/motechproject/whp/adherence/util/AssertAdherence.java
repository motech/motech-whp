package org.motechproject.whp.adherence.util;


import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;

import static org.junit.Assert.assertEquals;

public class AssertAdherence {

    public static void forWeek(Adherence adherence, DayOfWeek... dayOfWeeks) {
        assertEquals(dayOfWeeks.length, adherence.getAdherenceLogs().size());
        for (int i = 0; i < dayOfWeeks.length; i++) {
            assertEquals(dayOfWeeks[i], adherence.getAdherenceLogs().get(i).getPillDay());
        }
    }

    public static void areSame(Adherence expected, Adherence actual) {
        assertEquals(expected.getAdherenceLogs().size(), actual.getAdherenceLogs().size());
        for (int i = 0; i < expected.getAdherenceLogs().size(); i++) {
            assertEquals(expected.getAdherenceLogs().get(i), actual.getAdherenceLogs().get(i));
        }
    }
}
