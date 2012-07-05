package org.motechproject.whp.functional.assertions.treatmentcard;

import org.joda.time.LocalDate;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class Adherence {

    public static void is(TreatmentCardPage treatmentCardPage, LocalDate doseDate, String value, String user, String section) {
        assertEquals(value, treatmentCardPage.adherenceStatusOn(doseDate, section));
        assertEquals(user.toLowerCase(), treatmentCardPage.adherenceOnProvidedBy(doseDate, section));
    }

    public static void isPaused(TreatmentCardPage treatmentCardPage, LocalDate doseDate, String section) {
        assertTrue(treatmentCardPage.treatmentPausedOn(doseDate, section));
    }

    public static void isNotPaused(TreatmentCardPage treatmentCardPage, LocalDate doseDate, String section) {
        assertFalse(treatmentCardPage.treatmentPausedOn(doseDate, section));
    }

    public static void isNotEditable(TreatmentCardPage treatmentCardPage, LocalDate doseDate, String section) {
        assertTrue(treatmentCardPage.nonEditableAdherenceOn(doseDate, section));
    }

    public static void isNotPresent(TreatmentCardPage treatmentCardPage, LocalDate doseDate, String section) {
        assertTrue(treatmentCardPage.dateNotPresent(doseDate, section));
    }

}
