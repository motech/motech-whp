package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.today;

public class ProvidedTreatmentTest {

    @Test
    public void shouldCloseProvidedTreatment() {
        Treatment treatment = mock(Treatment.class);
        ProvidedTreatment providedTreatment = new ProvidedTreatment();
        providedTreatment.setTreatment(treatment);
        providedTreatment.close("Cured");

        assertEquals(today(), providedTreatment.getEndDate());
        verify(treatment, times(1)).close("Cured");
    }
}
