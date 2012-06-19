package org.motechproject.whp.adherence.audit;

import org.junit.After;
import org.junit.Test;
import org.motechproject.common.utils.SpringIntegrationTest;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceBuilder;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AdherenceAuditServiceTest extends SpringIntegrationTest {

    @Autowired
    private AllAuditLogs allAdherenceAuditLogs;

    @Autowired
    private AdherenceAuditService adherenceAuditService;
    private final AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "remarks");

    @Test
    public void shouldLogNumberOfDosesTakenWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals(3, adherenceAuditService.fetchAuditLogs().get(0).numberOfDosesTaken());
    }

    @Test
    public void shouldLogRemarksWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals("remarks", adherenceAuditService.fetchAuditLogs().get(0).remark());
    }

    @Test
    public void shouldLogSourceOfChangeWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals("WEB", adherenceAuditService.fetchAuditLogs().get(0).sourceOfChange());
    }

    @Test
    public void shouldLogPatientIdWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals("patientId", adherenceAuditService.fetchAuditLogs().get(0).patientId());
    }

    @Test
    public void shouldLogTbIdWhenAdherenceCaptured() {
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals("tbId", adherenceAuditService.fetchAuditLogs().get(0).tbId());
    }

    @Test
    public void shouldLogRemarksWhenAdherenceIsCaptured(){
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals("remarks", adherenceAuditService.fetchAuditLogs().get(0).remark());
    }

    @Test
    public void shouldLogUserWhenAdherenceIsCaptured(){
        WeeklyAdherence adherence = new WeeklyAdherenceBuilder().withDefaultLogs().build();
        adherenceAuditService.log(adherence, auditParams);
        assertEquals("user", adherenceAuditService.fetchAuditLogs().get(0).user());
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceAuditLogs.getAll().toArray());
    }
}
