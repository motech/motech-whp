package org.motechproject.whp.adherence.audit;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AdherenceAuditServiceTest extends SpringIntegrationTest {

    @Autowired
    private AllAuditLogs allAdherenceAuditLogs;

    @Autowired
    private AdherenceAuditService adherenceAuditService;

    @Test
    public void shouldLogNumberOfDosesTakenWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log("sourceOfChange", adherence);
        assertEquals(3, adherenceAuditService.fetchAuditLogs().get(0).numberOfDosesTaken());
    }

    @Test
    public void shouldLogRemarksWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log("sourceOfChange", adherence);
        assertEquals("remark", adherenceAuditService.fetchAuditLogs().get(0).remark());
    }

    @Test
    public void shouldLogSourceOfChangeWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log("sourceOfChange", adherence);
        assertEquals("sourceOfChange", adherenceAuditService.fetchAuditLogs().get(0).sourceOfChange());
    }

    @Test
    public void shouldLogPatientIdWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log("sourceOfChange", adherence);
        assertEquals("patientId", adherenceAuditService.fetchAuditLogs().get(0).patientId());
    }

    @Test
    public void shouldLogTbIdWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log("sourceOfChange", adherence);
        assertEquals("tbId", adherenceAuditService.fetchAuditLogs().get(0).tbId());
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceAuditLogs.getAll().toArray());
    }
}
