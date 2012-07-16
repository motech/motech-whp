package org.motechproject.whp.adherence.util;


import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;

import static org.junit.Assert.assertEquals;

public class AssertAdherence {

    public static void areSame(WeeklyAdherenceSummary expected, WeeklyAdherenceSummary actual) {
        assertEquals(expected.getWeek(), actual.getWeek());
        assertEquals(expected.getPatientId(), actual.getPatientId());
        assertEquals(expected.getDosesTaken(), actual.getDosesTaken());
    }

}
