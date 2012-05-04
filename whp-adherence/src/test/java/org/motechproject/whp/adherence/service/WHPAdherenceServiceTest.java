package org.motechproject.whp.adherence.service;

import org.ektorp.CouchDbConnector;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.motechproject.model.DayOfWeek.Monday;
import static org.motechproject.model.DayOfWeek.Tuesday;


@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class WHPAdherenceServiceTest extends SpringIntegrationTest {

    LocalDate currentWednesday = DateUtil.today();

    @Qualifier(value = "whpDbConnector")
    CouchDbConnector couchDbConnector;

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private AllAdherenceLogs allAdherenceLogs;

    @Test
    public void shouldRecordAdherenceForPatient() {
        AdherenceLog logForMonday = new AdherenceLog(Monday, currentWednesday);
        AdherenceLog logForTuesday = new AdherenceLog(Tuesday, currentWednesday);

        Adherence logs = new Adherence(currentWednesday, asList(Monday, Tuesday));
        logs.setAdherenceLogs(asList(logForMonday, logForTuesday));

        adherenceService.recordAdherence("patientId", logs);
        assertArrayEquals(
                new AdherenceLog[]{logForMonday, logForTuesday},
                adherenceService.adherenceAsOf("patientId", currentWednesday).getAdherenceLogs().toArray()
        );
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceLogs.getAll().toArray());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return couchDbConnector;
    }
}
