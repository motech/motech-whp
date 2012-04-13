package org.motechproject.whp.adherence.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.DosageLog;
import org.motechproject.whp.adherence.repository.AllDosageLogs;
import org.motechproject.whp.adherence.testutils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationAdherenceContext.xml"})
public class AdherenceServiceIT extends SpringIntegrationTest {

    @Autowired
    AllDosageLogs allDosageLogs;

    AdherenceService adherenceService;

    @Before
    public void setUp() {
        adherenceService = new AdherenceService(allDosageLogs);
    }

    @After
    public void tearDown() {
        markForDeletion(allDosageLogs.getAll().toArray());
    }

    @Test
    public void shouldRecordAdherence() {
        DosageLog dosageLog = adherenceService.recordAdherence("patientId", "treatmentCourseId", DateUtil.today().minusDays(2), DateUtil.today(), 2, 10, null);
        assertNotNull(dosageLog.getId());
    }

}
