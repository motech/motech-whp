package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.util.DateUtil;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;

public class ProvidedTreatmentTest {

    @Test
    public void shouldCloseProvidedTreatment() {
        Treatment treatment = mock(Treatment.class);
        DateTime now = now();

        ProvidedTreatment providedTreatment = new ProvidedTreatment();
        providedTreatment.setTreatment(treatment);
        providedTreatment.close("Cured", now);

        assertEquals(today(), providedTreatment.getEndDate());
        verify(treatment, times(1)).close("Cured", now);
    }
}
