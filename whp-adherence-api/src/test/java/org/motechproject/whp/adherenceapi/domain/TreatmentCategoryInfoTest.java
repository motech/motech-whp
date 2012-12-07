package org.motechproject.whp.adherenceapi.domain;

import org.junit.Test;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.junit.Assert.assertEquals;

public class TreatmentCategoryInfoTest {

    @Test
    public void shouldReturnTreatmentCategoryInfoForGovernmentTreatmentCategory() {
        Integer dosesPerWeek = 3;
        TreatmentCategory govtCategory = new TreatmentCategory();
        govtCategory.setDosesPerWeek(dosesPerWeek);

        TreatmentCategoryInfo treatmentCategoryInfo = new TreatmentCategoryInfo(govtCategory);

        assertEquals(TreatmentCategoryType.GOVERNMENT, treatmentCategoryInfo.getTreatmentCategoryType());
        assertEquals("0", treatmentCategoryInfo.getValidRangeFrom());
        assertEquals(dosesPerWeek.toString(), treatmentCategoryInfo.getValidRangeTo());
    }

    @Test
    public void shouldReturnTreatmentCategoryInfoForPrivateTreatmentCategory() {
        Integer dosesPerWeek = 7;
        TreatmentCategory privateCategory = new TreatmentCategory();
        privateCategory.setDosesPerWeek(dosesPerWeek);

        TreatmentCategoryInfo treatmentCategoryInfo = new TreatmentCategoryInfo(privateCategory);

        assertEquals(TreatmentCategoryType.PRIVATE, treatmentCategoryInfo.getTreatmentCategoryType());
        assertEquals("0", treatmentCategoryInfo.getValidRangeFrom());
        assertEquals(dosesPerWeek.toString(), treatmentCategoryInfo.getValidRangeTo());
    }
}
