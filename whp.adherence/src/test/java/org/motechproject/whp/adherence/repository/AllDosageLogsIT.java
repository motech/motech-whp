package org.motechproject.whp.adherence.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.DosageLog;
import org.motechproject.whp.adherence.testutils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:applicationAdherenceContext.xml"})
public class AllDosageLogsIT extends SpringIntegrationTest {

    @Autowired
    private AllDosageLogs allDosageLogs;

    @After
    public void tearDown() {
        markForDeletion(allDosageLogs.getAll().toArray());
    }

    @Test
    public void shouldAddDosageLogs_WhenLogDoesNotExistForGivenDateRangeAndPatientId() {
        DosageLog dosageLog = new DosageLog("patientId", DateUtil.today(), DateUtil.today(), 3, 3, null);
        allDosageLogs.add(dosageLog);
        assertNotNull(allDosageLogs.get(dosageLog.getId()));
    }

    @Test
    public void shouldReplaceDosageLogs_WhenLogAlreadyExistsForGivenDateRangeAndPatientId() {
        Map<String, String> metaData = new HashMap<String, String>() {{
            this.put("key1", "value1");
            this.put("key2", "value2");
        }};
        DosageLog existingDosageLog = new DosageLog("patientId", DateUtil.today(), DateUtil.today(), 3, 3, metaData);
        allDosageLogs.add(existingDosageLog);

        Map<String, String> updatedMetaData = new HashMap<String, String>() {{
            this.put("key2", "newValue2");
        }};
        DosageLog dosageLog = new DosageLog("patientId", DateUtil.today(), DateUtil.today(), 5, 10, updatedMetaData);
        allDosageLogs.add(dosageLog);

        assertEquals(existingDosageLog.getId(), dosageLog.getId());

        DosageLog updatedDosageLog = allDosageLogs.get(existingDosageLog.getId());
        assertEquals(5, updatedDosageLog.getDoseTakenCount());
        assertEquals(10, updatedDosageLog.getIdealDoseCount());
        assertEquals(2, updatedDosageLog.getMetaData().size());
        assertEquals("value1", updatedDosageLog.getMetaData().get("key1"));
        assertEquals("newValue2", updatedDosageLog.getMetaData().get("key2"));
    }

}
