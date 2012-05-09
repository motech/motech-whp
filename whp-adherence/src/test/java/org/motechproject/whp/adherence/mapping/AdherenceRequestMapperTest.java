package org.motechproject.whp.adherence.mapping;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;

import static org.junit.Assert.assertEquals;
import static org.motechproject.model.DayOfWeek.Monday;

public class AdherenceRequestMapperTest {

    private final static String PATIENT_ID = "patientId";

    @Test
    public void shouldSetExternalIdOnRequest() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        AdherenceRequestMapper mapper = new AdherenceRequestMapper(PATIENT_ID, log);

        assertEquals(PATIENT_ID, mapper.map().externalId());
    }

    @Test
    public void shouldMarkDosesTaken() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsTaken(true);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(PATIENT_ID, log);
        assertEquals(1, mapper.map().status());
    }

    @Test
    public void shouldNotMarkDosesTaken() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsTaken(false);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(PATIENT_ID, log);
        assertEquals(0, mapper.map().status());
    }

    @Test
    public void shouldMarkDosesMissed() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsNotTaken(true);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(PATIENT_ID, log);
        assertEquals(2, mapper.map().status());
    }

    @Test
    public void shouldNotMarkDosesMissed() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsNotTaken(false);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(PATIENT_ID, log);
        assertEquals(0, mapper.map().status());
    }
}
