package org.motechproject.whp.refdata.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TreatmentCategoryTest {
    @Test
    public void shouldReturnApplicableNumberOfDosesForGivenPhase() {
        TreatmentCategory category = new TreatmentCategory();
        category.setNumberOfDosesInIP(24);
        category.setNumberOfDosesInEIP(12);
        category.setNumberOfDosesInCP(28);

        assertEquals(Integer.valueOf(24), category.numberOfDosesForPhase(PhaseName.IP));
        assertEquals(Integer.valueOf(12), category.numberOfDosesForPhase(PhaseName.EIP));
        assertEquals(Integer.valueOf(28), category.numberOfDosesForPhase(PhaseName.CP));
    }
}
