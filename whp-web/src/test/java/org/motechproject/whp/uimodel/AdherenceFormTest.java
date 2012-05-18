package org.motechproject.whp.uimodel;

import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class AdherenceFormTest {

    @Test
    public void shouldMarkAsUpdated_WhenThereIsChangeInPillStatus() {
        AdherenceForm adherenceForm = new AdherenceForm(DayOfWeek.Monday, DateUtil.today(), PillStatus.Taken, null);
        adherenceForm.setIsNotTaken(true);
        assertTrue("Form was modified, still not flagged as updated!", adherenceForm.updated());
    }

    @Test
    public void shouldNotMarkAsUpdated_WhenThereIsNoChangeInPillStatus() {
        AdherenceForm adherenceForm = new AdherenceForm(DayOfWeek.Monday, DateUtil.today(), PillStatus.Taken, null);
        adherenceForm.setIsTaken(true);
        assertFalse("Form was not modified, still flagged as updated!", adherenceForm.updated());
    }
}
