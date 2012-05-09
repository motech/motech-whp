package org.motechproject.whp.adherence.mapping;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.motechproject.model.DayOfWeek.Monday;

public class AdherenceRequestMapperTest {

    private Patient patient;

    @Before
    public void setup(){
        ProvidedTreatment currentProvidedTreatment = new ProvidedTreatment("ProviderId", "TB_ID");
        patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(currentProvidedTreatment).build();
    }

    @Test
    public void shouldSetExternalIdOnRequest() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        AdherenceRequestMapper mapper = new AdherenceRequestMapper(patient, log);

        assertEquals(patient.getPatientId(), mapper.request().externalId());
    }

    @Test
    public void shouldSetTbIdAsMetaDataOnRequest() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        AdherenceRequestMapper mapper = new AdherenceRequestMapper(patient, log);

        assertNotNull(patient.tbId());
        assertEquals(patient.tbId(), mapper.request().meta().get("tb_id"));
    }

    @Test
    public void shouldMarkDosesTaken() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsTaken(true);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(patient, log);
        assertEquals(1, mapper.request().status());
    }

    @Test
    public void shouldNotMarkDosesTaken() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsTaken(false);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(patient, log);
        assertEquals(0, mapper.request().status());
    }

    @Test
    public void shouldMarkDosesMissed() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsNotTaken(true);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(patient, log);
        assertEquals(2, mapper.request().status());
    }

    @Test
    public void shouldNotMarkDosesMissed() {
        AdherenceLog log = new AdherenceLog(Monday, DateUtil.today());
        log.setIsNotTaken(false);

        AdherenceRequestMapper mapper = new AdherenceRequestMapper(patient, log);
        assertEquals(0, mapper.request().status());
    }
}
