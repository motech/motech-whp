package org.motechproject.whp.adherenceapi.domain;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.builder.DosageBuilder;

import static org.junit.Assert.*;

public class DosageTest {

    @Test
    public void shouldReturnTreatmentCategoryInfoForGovernmentTreatmentCategory() {
        Dosage dosage = new DosageBuilder(3).dosage();
        assertEquals(TreatmentProvider.GOVERNMENT, dosage.getTreatmentProvider());
        assertEquals("0", dosage.getValidRangeFrom());
        assertEquals("3", dosage.getValidRangeTo());
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoForPrivateTreatmentCategory() {
        Dosage dosage = new DosageBuilder(7).dosage();
        assertEquals(TreatmentProvider.PRIVATE, dosage.getTreatmentProvider());
        assertEquals("0", dosage.getValidRangeFrom());
        assertEquals("7", dosage.getValidRangeTo());
    }

    @Test
    public void shouldBeValidInputBasedOnAllowedDosesForPatient() {
        Dosage dosage = new DosageBuilder(3).dosage();
        assertTrue(dosage.isValidInput(3));
        assertTrue(dosage.isValidInput(0));
    }

    @Test
    public void shouldBeNotValidInputBasedOnAllowedDosesForPatient() {
        Dosage dosage = new DosageBuilder(3).dosage();
        assertFalse(dosage.isValidInput(4));
        assertFalse(dosage.isValidInput(-1));
    }

}
