package org.motechproject.whp.patient.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.ReasonForClosure;
import org.motechproject.whp.refdata.domain.TreatmentComplete;
import org.motechproject.whp.refdata.domain.TreatmentStatus;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;

public class TreatmentTest {

    @Test
    public void shouldCloseTreatment() {
        Treatment treatment = new Treatment();
        treatment.close("Cured", "Yes");
        assertEquals(ReasonForClosure.Cured, treatment.getReasonForClosure());
        assertEquals(TreatmentComplete.Yes, treatment.getTreatmentComplete());
        assertEquals(TreatmentStatus.Closed, treatment.getStatus());
        assertEquals(today(), treatment.getEndDate());
    }

}
