package org.motechproject.whp.it.adherence.capture.service;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.mapping.AdherenceLogMapper;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.it.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AdherenceLogServiceIT extends SpringIntegrationTest {

    @Autowired
    AllAdherenceLogs allAdherenceLogs;

    @Autowired
    AdherenceLogService adherenceLogService;

    @After
    public void tearDown() {
        markForDeletion(allAdherenceLogs.getAll().toArray());
    }

    @Test
    public void shouldFetchAllAdherenceLogsWithinGivenDateAndWithinThreeMonths() {
        LocalDate today = DateUtil.today();
        AdherenceRecord patientOneWithinDateLimit = new AdherenceRecord("externalId1", "treatmentId", today);
        patientOneWithinDateLimit = patientOneWithinDateLimit.status(1);

        AdherenceRecord patientOneOutsideLimit = new AdherenceRecord("externalId1", "treatmentId", today.plusDays(1));
        patientOneOutsideLimit = patientOneOutsideLimit.status(1);

        AdherenceRecord patientOneOutsideLimit_BeforeThreeMonths = new AdherenceRecord("externalId1", "treatmentId", today.minusMonths(3).minusDays(2));
        patientOneOutsideLimit_BeforeThreeMonths = patientOneOutsideLimit.status(1);

        AdherenceRecord patientTwoWithinDateLimit = new AdherenceRecord("externalId2", "treatmentId", today.minusDays(1));
        patientTwoWithinDateLimit = patientTwoWithinDateLimit.status(1);

        adherenceLogService.saveOrUpdateAdherence(asList(patientOneOutsideLimit, patientOneWithinDateLimit, patientTwoWithinDateLimit, patientOneOutsideLimit_BeforeThreeMonths));
        Assert.assertEquals(1, adherenceLogService.adherence(today, 0, 1).size());
        Assert.assertEquals(1, adherenceLogService.adherence(today, 1, 1).size());
    }

    @Test
    public void shouldFetchAdherenceRecordsBetweenTwoDates() {
        LocalDate today = DateUtil.today();

        AdherenceRecord forYesterday = new AdherenceRecord("externalId", "treatmentId", today.minusDays(1));
        forYesterday = forYesterday.status(1);
        AdherenceRecord forToday = new AdherenceRecord("externalId", "treatmentId", today);
        forToday = forToday.status(1);

        adherenceLogService.saveOrUpdateAdherence(asList(forYesterday, forToday));
        Assert.assertEquals(2, adherenceLogService.adherence("externalId", "treatmentId", today.minusDays(1), today).size());
    }

    @Test
    public void shouldSaveAdherenceLogs() {
        LocalDate now = LocalDate.now();
        AdherenceRecord datum1 = new AdherenceRecord("externalId1", "treatmentId1", now);
        datum1.district("district1");
        AdherenceRecord datum2 = new AdherenceRecord("externalId1", "treatmentId2", now);
        datum2.district("district2");

        AdherenceLogMapper mapper = new AdherenceLogMapper();
        adherenceLogService.addOrUpdateLogsByDoseDate(asList(datum1, datum2), "externalId1");

        List<AdherenceLog> logsInDb = allAdherenceLogs.getAll();
        assertEquals(2, logsInDb.size());

        Assert.assertEquals(mapper.map(datum1), logsInDb.get(0));
        Assert.assertEquals(mapper.map(datum2), logsInDb.get(1));
    }
}
