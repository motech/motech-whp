package org.motechproject.whp.functional.assertions.treatmentcard;

import org.joda.time.LocalDate;
import org.motechproject.whp.functional.page.TreatmentCardPage;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class Adherence {

    public static void is(TreatmentCardPage treatmentCardPage, LocalDate doseDate, String value, String user) {
        assertEquals(value, treatmentCardPage.adherenceStatusOn(doseDate));
        assertEquals(user.toLowerCase(), treatmentCardPage.adherenceOnProvidedBy(doseDate));
    }

    public static void isPaused(TreatmentCardPage treatmentCardPage, LocalDate doseDate) {
        assertTrue(treatmentCardPage.treatmentPausedOn(doseDate));
    }

    public static void isNotPaused(TreatmentCardPage treatmentCardPage, LocalDate doseDate) {
        assertFalse(treatmentCardPage.treatmentPausedOn(doseDate));
    }

    public static void isNotEditable(TreatmentCardPage treatmentCardPage, LocalDate doseDate) {
        assertTrue(treatmentCardPage.nonEditableAdherenceOn(doseDate));
    }

    public static void isNotPresent(TreatmentCardPage treatmentCardPage, LocalDate doseDate) {
        assertTrue(treatmentCardPage.dateNotPresent(doseDate));
    }

}
