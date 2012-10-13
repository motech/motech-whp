package org.motechproject.whp.refdata.domain;

import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class TreatmentCategoryTest {
    @Test
    public void shouldReturnApplicableNumberOfDosesForGivenPhase() {
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        category.setNumberOfDosesInEIP(12);
        category.setNumberOfDosesInCP(28);

        assertEquals(Integer.valueOf(24), category.numberOfDosesForPhase(Phase.IP));
        assertEquals(Integer.valueOf(12), category.numberOfDosesForPhase(Phase.EIP));
        assertEquals(Integer.valueOf(28), category.numberOfDosesForPhase(Phase.CP));
    }

    @Test
    public void shouldReturnTrueIfGovernmentCategory() {
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek);
        assertTrue(treatmentCategory.isGovernmentCategory());
    }

    @Test
    public void shouldReturnFalseIfCommercialCategory() {
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, asList(DayOfWeek.values()));

        assertTrue(treatmentCategory.isGovernmentCategory());
    }
}
