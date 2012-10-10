package org.motechproject.adherence.it.service;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.AdherenceLogMapper;
import org.motechproject.adherence.it.common.SpringIntegrationTest;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.adherence.service.AdherenceLogService;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceLogServiceIT extends SpringIntegrationTest {

    @Autowired
    AllAdherenceLogs allAdherenceLogs;

    AdherenceLogService adherenceLogService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceLogService = new AdherenceLogService(allAdherenceLogs);
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceLogs.getAll().toArray());
    }

    @Test
    public void shouldFetchAllAdherenceLogsWithinGivenDate() {
        LocalDate today = DateUtil.today();
        AdherenceRecord patientOneWithinDateLimit = new AdherenceRecord("externalId1", "treatmentId", today);
        patientOneWithinDateLimit = patientOneWithinDateLimit.status(1);

        AdherenceRecord patientOneOutsideLimit = new AdherenceRecord("externalId1", "treatmentId", today.plusDays(1));
        patientOneOutsideLimit = patientOneOutsideLimit.status(1);

        AdherenceRecord patientTwoWithinDateLimit = new AdherenceRecord("externalId2", "treatmentId", today.minusDays(1));
        patientTwoWithinDateLimit = patientTwoWithinDateLimit.status(1);

        adherenceLogService.saveOrUpdateAdherence(asList(patientOneOutsideLimit, patientOneWithinDateLimit, patientTwoWithinDateLimit));
        assertEquals(1, adherenceLogService.adherence(today, 0, 1).size());
        assertEquals(1, adherenceLogService.adherence(today, 1, 1).size());
    }

    @Test
    public void shouldFetchAdherenceRecordsBetweenTwoDates() {
        LocalDate today = DateUtil.today();

        AdherenceRecord forYesterday = new AdherenceRecord("externalId", "treatmentId", today.minusDays(1));
        forYesterday = forYesterday.status(1);
        AdherenceRecord forToday = new AdherenceRecord("externalId", "treatmentId", today);
        forToday = forToday.status(1);

        adherenceLogService.saveOrUpdateAdherence(asList(forYesterday, forToday));
        assertEquals(2, adherenceLogService.adherence("externalId", "treatmentId", today.minusDays(1), today).size());
    }

    @Test
    public void shouldSaveAdherenceLogs() {
        LocalDate now = LocalDate.now();
        AdherenceRecord datum1 = new AdherenceRecord("externalId1", "treatmentId1", now);
        AdherenceRecord datum2 = new AdherenceRecord("externalId1", "treatmentId2", now);

        AdherenceLogMapper mapper = new AdherenceLogMapper();
        adherenceLogService.addOrUpdateLogsByDoseDate(asList(datum1, datum2), "externalId1");

        List<AdherenceLog> logsInDb = allAdherenceLogs.getAll();
        assertEquals(2, logsInDb.size());

        assertEquals(mapper.map(datum1), logsInDb.get(0));
        assertEquals(mapper.map(datum2), logsInDb.get(1));
    }
}