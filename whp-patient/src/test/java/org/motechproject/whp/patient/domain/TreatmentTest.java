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

public class TreatmentTest {

    @Test
    public void shouldCloseTreatment() {
        Therapy therapy = mock(Therapy.class);
        DateTime now = now();

        Treatment treatment = new Treatment();
        treatment.setTherapy(therapy);
        treatment.close(TreatmentOutcome.Cured, now);

        assertEquals(today(), treatment.getEndDate());
        assertEquals(TreatmentOutcome.Cured, treatment.getTreatmentOutcome());
        verify(therapy, times(1)).close(now);
    }

    @Test
    public void shouldPauseTreatment() {
        Therapy therapy = mock(Therapy.class);
        DateTime now = now();

        Treatment treatment = new Treatment();
        treatment.setTherapy(therapy);
        treatment.pause("paws", now);

        assertTrue(treatment.isPaused());
    }

    @Test
    public void shouldResumeTreatment() {
        Therapy therapy = mock(Therapy.class);
        DateTime now = now();

        Treatment treatment = new Treatment();
        treatment.setTherapy(therapy);
        treatment.pause("paws", now);
        treatment.resume("swap", now);

        assertFalse(treatment.isPaused());
    }

    @Test
    public void shouldStoreIdsInLowerCase() {
        Treatment treatment = new Treatment();
        treatment.setProviderId("QWER");
        treatment.setTbId("ASD");
        assertEquals("qwer", treatment.getProviderId());
        assertEquals("asd", treatment.getTbId());

        treatment = new Treatment("QWER","asd", PatientType.New);
        assertEquals("qwer", treatment.getProviderId());
        assertEquals("asd", treatment.getTbId());
    }

    @Test
    public void shouldHandleNullValuesForId() {
        Treatment treatment = new Treatment(null,null, PatientType.New);
        assertEquals(null, treatment.getProviderId());
        assertEquals(null, treatment.getTbId());

        treatment = new Treatment("","",PatientType.New);
        treatment.setProviderId(null);
        treatment.setTbId(null);
        assertEquals(null, treatment.getProviderId());
        assertEquals(null, treatment.getTbId());
    }


}
