package org.motechproject.whp.adherence.domain;

import org.junit.Test;
import org.motechproject.util.DateUtil;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.Monday;
import static org.motechproject.model.DayOfWeek.Tuesday;

public class AdherenceTest {

    @Test
    public void shouldHaveOneAdherenceLogForEveryDay() {
        Adherence adherence = new Adherence(DateUtil.today(), asList(Monday, Tuesday));
        assertEquals(2, adherence.getAdherenceLogs().size());
    }

}
