package org.motechproject.whp.adherence.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationAdherenceContext.xml"})
public class AllAdherenceLogsIT {

    @Autowired
    private AllAdherenceLogs allAdherenceLogs;

    @Test
    public void shouldGetAllAdherenceLogs() {
        AdherenceLog adherenceLog = new AdherenceLog().setDoseTaken(10).setDoseToBeTaken(20);
        allAdherenceLogs.add(adherenceLog);

        List<AdherenceLog> adherenceLogs = allAdherenceLogs.getAll();
        assertEquals(1, adherenceLogs.size());
        assertEquals(10, adherenceLogs.get(0).getDoseTaken());
        assertEquals(20, adherenceLogs.get(0).getDoseToBeTaken());
        assertEquals(DateUtil.today(), adherenceLogs.get(0).getFromDate());
        assertEquals(DateUtil.today(), adherenceLogs.get(0).getToDate());

        allAdherenceLogs.remove(adherenceLog);
    }
}
