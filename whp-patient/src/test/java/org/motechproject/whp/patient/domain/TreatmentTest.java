package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.refdata.domain.TreatmentStatus;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;

public class TreatmentTest {

    @Test
    public void shouldCloseTreatment() {
        Treatment treatment = new Treatment();
        treatment.close("Cured");
        assertEquals(TreatmentOutcome.Cured, treatment.getTreatmentOutcome());
        assertEquals(TreatmentStatus.Closed, treatment.getStatus());
        assertEquals(today(), treatment.getEndDate());
    }

}
