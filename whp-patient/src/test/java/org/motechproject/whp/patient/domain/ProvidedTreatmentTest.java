package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
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
        providedTreatment.close(TreatmentOutcome.Cured, now);

        assertEquals(today(), providedTreatment.getEndDate());
        assertEquals(TreatmentOutcome.Cured, providedTreatment.getTreatmentOutcome());
        verify(treatment, times(1)).close(now);
    }

    @Test
    public void shouldPauseProvidedTreatment() {
        Treatment treatment = mock(Treatment.class);
        DateTime now = now();

        ProvidedTreatment providedTreatment = new ProvidedTreatment();
        providedTreatment.setTreatment(treatment);
        providedTreatment.pause("paws", now);

        assertTrue(providedTreatment.isPaused());
    }

    @Test
    public void shouldResumeProvidedTreatment() {
        Treatment treatment = mock(Treatment.class);
        DateTime now = now();

        ProvidedTreatment providedTreatment = new ProvidedTreatment();
        providedTreatment.setTreatment(treatment);
        providedTreatment.pause("paws", now);
        providedTreatment.resume("swap", now);

        assertFalse(providedTreatment.isPaused());
    }

    @Test
    public void shouldStoreIdsInLowerCase() {
        ProvidedTreatment providedTreatment = new ProvidedTreatment();
        providedTreatment.setProviderId("QWER");
        providedTreatment.setTbId("ASD");
        assertEquals("qwer", providedTreatment.getProviderId());
        assertEquals("asd", providedTreatment.getTbId());

        providedTreatment = new ProvidedTreatment("QWER","asd", PatientType.New);
        assertEquals("qwer", providedTreatment.getProviderId());
        assertEquals("asd", providedTreatment.getTbId());
    }

    @Test
    public void shouldHandleNullValuesForId() {
        ProvidedTreatment providedTreatment = new ProvidedTreatment(null,null, PatientType.New);
        assertEquals(null,providedTreatment.getProviderId());
        assertEquals(null,providedTreatment.getTbId());

        providedTreatment = new ProvidedTreatment("","",PatientType.New);
        providedTreatment.setProviderId(null);
        providedTreatment.setTbId(null);
        assertEquals(null, providedTreatment.getProviderId());
        assertEquals(null,providedTreatment.getTbId());
    }


}
