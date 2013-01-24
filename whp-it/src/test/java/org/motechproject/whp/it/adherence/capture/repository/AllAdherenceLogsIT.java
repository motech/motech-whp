package org.motechproject.whp.it.adherence.capture.repository;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.it.SpringIntegrationTest;
import org.motechproject.whp.user.domain.ProviderIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = "classpath*:/applicationWHPAdherenceContext.xml")
public class AllAdherenceLogsIT extends SpringIntegrationTest {

    @Autowired
    private AllAdherenceLogs allAdherenceLogs;
    private LocalDate today = DateUtil.today();
    private LocalDate yesterday = DateUtil.today().minusDays(1);

    @After
    public void tearDown() {
        markForDeletion(allAdherenceLogs.getAll().toArray());
    }

    @Test
    public void shouldSaveAdherenceLog() {
        AdherenceLog adherenceLog = new AdherenceLog("externalId", "treatmentId", DateUtil.today());
        adherenceLog.providerId("providerId");
        adherenceLog.tbId("tbId");

        allAdherenceLogs.add(adherenceLog);
        Assert.assertEquals(adherenceLog, allAdherenceLogs.get(adherenceLog.getId()));
    }

    @Test
    public void shouldBeIdempotentOnSave() {
        AdherenceLog adherenceLog = new AdherenceLog("externalId", "treatmentId", DateUtil.today());

        allAdherenceLogs.add(adherenceLog);
        allAdherenceLogs.add(adherenceLog);
        Assert.assertEquals(1, allAdherenceLogs.getAll().size());
    }

    @Test
    public void shouldFetchAdherenceLogByKey() {
        LocalDate today = DateUtil.today();

        AdherenceLog toBeFound = new AdherenceLog("externalId", "treatmentId", today);
        AdherenceLog toBeIgnoredByExternalId = new AdherenceLog("otherExternalId", "treatmentId", today);
        AdherenceLog toBeIgnoredByTreatmentId = new AdherenceLog("externalId", "otherTreatmentId", today);
        AdherenceLog toBeIgnoredByDate = new AdherenceLog("externalId", "treatmentId", today.plusDays(1));

        addAll(toBeFound, toBeIgnoredByExternalId, toBeIgnoredByTreatmentId, toBeIgnoredByDate);

        Assert.assertArrayEquals(
                new AdherenceLog[]{toBeFound},
                allAdherenceLogs.findLogsBy("externalId", "treatmentId", today).toArray()
        );
    }

    @Test
    public void shouldReturnTakenLogsWhenFindingLogsAsOfGivenDate() {
        LocalDate today = DateUtil.today();

        AdherenceLog toBeFound = createAdherenceLogWithGivenStatus(today);
        AdherenceLog olderLog = createAdherenceLogWithGivenStatus(today.minusDays(5));
        AdherenceLog newerLog = createAdherenceLogWithGivenStatus(today.plusDays(5));
        toBeFound.status(1);
        addAll(toBeFound, olderLog, newerLog);
        Assert.assertArrayEquals(
                new AdherenceRecord[]{new AdherenceRecord(toBeFound)},
                allAdherenceLogs.findLogsInRange(yesterday, today, 0, 5).toArray()
        );
    }

    private AdherenceLog createAdherenceLogWithGivenStatus(LocalDate today) {
        return new AdherenceLog("externalId", "treatmentId", today).status(1);
    }

    @Test
    public void shouldReturnNotTakenLogsWhenFindingLogsAsOfGivenDate() {
        AdherenceLog toBeFound = new AdherenceLog("externalId", "treatmentId", today);
        toBeFound.status(2);

        addAll(toBeFound);
        Assert.assertArrayEquals(
                new AdherenceRecord[]{new AdherenceRecord(toBeFound)},
                allAdherenceLogs.findLogsInRange(yesterday, today, 0, 1).toArray()
        );
    }

    @Test
    public void shouldNotReturnUnknownLogsWhenFindingLogsAsOfGivenDate() {
        LocalDate today = DateUtil.today();

        AdherenceLog toBeFound = new AdherenceLog("externalId", "treatmentId", today);
        toBeFound.status(0);

        addAll(toBeFound);
        assertTrue(allAdherenceLogs.findLogsInRange(yesterday, today, 0, 1).isEmpty());
    }

    @Test
    public void shouldPageAdherenceRecordsReturnedWhenFindingLogsByDate() {
        LocalDate today = DateUtil.today();

        AdherenceLog logToBeFoundForPatient1 = new AdherenceLog("externalId1", "treatmentId1", today);
        AdherenceLog logToBeFoundForPatient2 = new AdherenceLog("externalId2", "treatmentId2", today);
        logToBeFoundForPatient1.status(1);
        logToBeFoundForPatient2.status(1);

        addAll(logToBeFoundForPatient1, logToBeFoundForPatient2);
        Assert.assertArrayEquals(
                new AdherenceRecord[]{new AdherenceRecord(logToBeFoundForPatient1)},
                allAdherenceLogs.findLogsInRange(yesterday, today, 0, 1).toArray()
        );
        Assert.assertArrayEquals(
                new AdherenceRecord[]{new AdherenceRecord(logToBeFoundForPatient2)},
                allAdherenceLogs.findLogsInRange(yesterday, today, 1, 1).toArray()
        );
    }

    @Test
    public void shouldReturnLogsForAllPatientsWhenFindingLogsAsOfGivenDate() {
        LocalDate today = DateUtil.today();

        AdherenceLog logToBeFoundForPatient1 = new AdherenceLog("externalId1", "treatmentId1", today);
        AdherenceLog logToBeFoundForPatient2 = new AdherenceLog("externalId2", "treatmentId2", today);
        logToBeFoundForPatient1.status(1);
        logToBeFoundForPatient2.status(1);

        addAll(logToBeFoundForPatient1, logToBeFoundForPatient2);
        Assert.assertArrayEquals(
                new AdherenceRecord[]{new AdherenceRecord(logToBeFoundForPatient1), new AdherenceRecord(logToBeFoundForPatient2)},
                allAdherenceLogs.findLogsInRange(yesterday, today, 0, 2).toArray()
        );
    }

    @Test
    public void shouldFetchAllAdherenceLogsByKeyTillKeyDate() {
        LocalDate today = DateUtil.today();

        AdherenceLog toBeFound = new AdherenceLog("externalId", "treatmentId", today);
        AdherenceLog anotherToBeFound = new AdherenceLog("externalId", "treatmentId", today.plusDays(1));
        AdherenceLog toBeIgnoredByExternalId = new AdherenceLog("otherExternalId", "treatmentId", today);
        AdherenceLog toBeIgnoredByTreatmentId = new AdherenceLog("externalId", "otherTreatmentId", today);

        addAll(toBeFound, anotherToBeFound, toBeIgnoredByExternalId, toBeIgnoredByTreatmentId);

        LocalDate asOf = today.plusDays(5);
        Assert.assertEquals(2, allAdherenceLogs.findLogsBy("externalId", "treatmentId", asOf).size());
    }

    @Test
    public void shouldNotFetchAllAdherenceLogsByKeyBeyondKeyDate() {
        LocalDate today = DateUtil.today();

        AdherenceLog hasDateWithinKeyDate = new AdherenceLog("externalId", "treatmentId", today);
        AdherenceLog hasDateBeyondKeyDate = new AdherenceLog("externalId", "treatmentId", today.plusDays(1));

        addAll(hasDateWithinKeyDate, hasDateBeyondKeyDate);

        Assert.assertArrayEquals(
                new AdherenceLog[]{hasDateWithinKeyDate},
                allAdherenceLogs.findLogsBy("externalId", "treatmentId", today).toArray()
        );
    }

    @Test
    public void shouldFetchAllLogsBetweenGivenDates() {
        AdherenceLog hasDateBeforeRange = new AdherenceLog("externalId", "treatmentId", new LocalDate(2012, 1, 1));
        AdherenceLog hasDateWithinRange = new AdherenceLog("externalId", "treatmentId", new LocalDate(2012, 5, 5));
        AdherenceLog alsoHasDateWithinRange = new AdherenceLog("otherExternalId", "treatmentId", new LocalDate(2012, 5, 5));
        AdherenceLog hasDateBeyondRange = new AdherenceLog("externalId", "treatmentId", new LocalDate(2012, 12, 1));

        addAll(hasDateBeforeRange, hasDateWithinRange, alsoHasDateWithinRange, hasDateBeyondRange);

        AdherenceRecord expectedAdherenceRecord = new AdherenceRecord(hasDateWithinRange.externalId(),
                hasDateWithinRange.treatmentId(),
                hasDateWithinRange.doseDate());

        List<AdherenceRecord> actualResult = allAdherenceLogs.findLogsInRange("externalId", "treatmentId", new LocalDate(2012, 5, 4), new LocalDate(2012, 5, 6));
        assertEquals(1, actualResult.size());

        AdherenceRecord actualAdherenceRecord = actualResult.get(0);
        Assert.assertEquals(expectedAdherenceRecord.doseDate(), actualAdherenceRecord.doseDate());
        Assert.assertEquals(expectedAdherenceRecord.status(), actualAdherenceRecord.status());
        Assert.assertEquals(expectedAdherenceRecord.externalId(), actualAdherenceRecord.externalId());
        Assert.assertEquals(expectedAdherenceRecord.treatmentId(), actualAdherenceRecord.treatmentId());
    }

    @Test
    public void shouldAddBulkObjects() {
        AdherenceLog log1 = new AdherenceLog("externalId", "treatmentId", new LocalDate(2012, 1, 1));
        AdherenceLog log2 = new AdherenceLog("externalId", "treatmentId", new LocalDate(2012, 5, 5));

        allAdherenceLogs.addOrUpdateLogsForExternalIdByDoseDate(asList(log1, log2), "externalId");

        Assert.assertEquals(2, allAdherenceLogs.getAll().size());
    }

    @Test
    public void shouldUpdateBulkObjectsIfAlreadyExists() {
        AdherenceLog log1 = createAdherenceLog();
        log1.status(1);
        AdherenceLog log2 = new AdherenceLog("externalId", "treatmentId2", new LocalDate(2012, 1, 1));
        log2.status(2);
        AdherenceLog log3 = new AdherenceLog("externalId", "treatmentId2", new LocalDate(2012, 5, 5));

        allAdherenceLogs.add(log1);
        allAdherenceLogs.addOrUpdateLogsForExternalIdByDoseDate(asList(log2, log3), "externalId");

        Assert.assertEquals(2, allAdherenceLogs.getAll().size());
        Assert.assertEquals(2, allAdherenceLogs.findLogBy("externalId", "treatmentId2", new LocalDate(2012, 1, 1)).status());

    }

    @Test
    public void shouldHandleDuplicateNewLogs() {
        AdherenceLog log1 = createAdherenceLog();
        AdherenceLog log2 = createAdherenceLog();
        allAdherenceLogs.addOrUpdateLogsForExternalIdByDoseDate(asList(log1, log2), "externalId");
        Assert.assertEquals(1, allAdherenceLogs.getAll().size());
    }

    @Test
    public void shouldCountTakenLogsForTherapyBetweenGivenDates() {
        AdherenceLog log1 = createAdherenceLog();
        log1.status(1);
        AdherenceLog log2 = new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 3));
        log2.status(1);
        AdherenceLog log3 = new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 5));
        log3.status(2);

        allAdherenceLogs.add(log1);
        allAdherenceLogs.add(log2);
        allAdherenceLogs.add(log3);

        int count = allAdherenceLogs.countOfDosesTakenBetween("externalId", "treatmentId1", new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 10));

        assertEquals(2, count);
    }

    @Test
    public void shouldReturnAllTakenLogsSortedInAscendingOrderOfDoseDate() {
        AdherenceLog log1 = new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 3));
        log1.status(1);
        AdherenceLog log2 = createAdherenceLog();
        log2.status(1);
        AdherenceLog log3 = new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 5));
        log3.status(2);

        allAdherenceLogs.add(log1);
        allAdherenceLogs.add(log2);
        allAdherenceLogs.add(log3);

        List<AdherenceRecord> adherenceRecords = allAdherenceLogs.allTakenLogsFrom("externalId", "treatmentId1", new LocalDate(2012, 1, 1));

        assertEquals(2, adherenceRecords.size());
        Assert.assertEquals(new LocalDate(2012, 1, 1), adherenceRecords.get(0).doseDate());
        Assert.assertEquals(new LocalDate(2012, 1, 3), adherenceRecords.get(1).doseDate());
    }

    @Test
    public void shouldFetchLogsForExternalIdByDateRange() {
        LocalDate yesterday = new LocalDate(2012, 2, 1);
        LocalDate dateInBetweenRange = new LocalDate(2012, 5, 5);
        LocalDate endDate = new LocalDate(2012, 5, 10);

        AdherenceLog logBeforeRange = new AdherenceLog("externalId1", "treatmentId1", yesterday.minusDays(1));
        AdherenceLog logInRange1 = new AdherenceLog("externalId1", "treatmentId3", yesterday);
        AdherenceLog logInRange2 = new AdherenceLog("externalId1", "treatmentId2", dateInBetweenRange);
        AdherenceLog logInRange3 = new AdherenceLog("externalId1", "treatmentId3", endDate);
        AdherenceLog logAfterRange = new AdherenceLog("externalId1", "treatmentId3", endDate.plusDays(1));
        AdherenceLog logInRangeButDiffExternalId = new AdherenceLog("externalId2", "treatmentId3", dateInBetweenRange);
        allAdherenceLogs.add(logBeforeRange);
        allAdherenceLogs.add(logInRange1);
        allAdherenceLogs.add(logInRange2);
        allAdherenceLogs.add(logInRange3);
        allAdherenceLogs.add(logInRangeButDiffExternalId);
        allAdherenceLogs.add(logAfterRange);

        List<AdherenceLog> result = allAdherenceLogs.findAllLogsForExternalIdInDoseDateRange("externalId1", yesterday, endDate);
        assertEquals(3, result.size());
        Assert.assertEquals(yesterday, result.get(0).doseDate());
        Assert.assertEquals(dateInBetweenRange, result.get(1).doseDate());
        Assert.assertEquals(endDate, result.get(2).doseDate());

    }

    @Test
    public void shouldReturnTakenLogs() {
        AdherenceLog log1 = createAdherenceLog();
        log1.status(1);
        AdherenceLog log2 = new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 3));
        log2.status(1);
        AdherenceLog log3 = new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 5));
        log3.status(2);

        allAdherenceLogs.add(log1);
        allAdherenceLogs.add(log2);
        allAdherenceLogs.add(log3);

        List<AdherenceRecord> adherenceRecords = allAdherenceLogs.allTakenLogs("externalId", "treatmentId1");

        assertEquals(2, adherenceRecords.size());
    }

    @Test
    public void shouldReturnProvidersWithAdherenceReportedInADistrict() {
        ProviderIds providersWithAdherence = new ProviderIds(asList("providerId1", "providerId2", "providerId3"));
        String district = "district";

        List<AdherenceLog> adherenceLogs = asList(createAdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 1), 1),
                createAdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 3), 1),
                createAdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 5), 1));

        adherenceLogs.get(0).providerId("providerId1");
        adherenceLogs.get(0).district(district);
        adherenceLogs.get(1).providerId("providerId2");
        adherenceLogs.get(1).district(district);
        adherenceLogs.get(2).providerId("providerId3");
        adherenceLogs.get(2).district(district);
        addAll(adherenceLogs);

        assertEquals(providersWithAdherence, allAdherenceLogs.withKnownAdherenceReportedByProviders(district, new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 5)));
    }

    @Test
    public void shouldReturnProvidersWithAdherenceReported() {
        ProviderIds providersWithAdherence = new ProviderIds(asList("providerId1", "providerId2", "providerId3"));

        List<AdherenceLog> adherenceLogs = asList(createAdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 1), 1).providerId("providerId1"),
                createAdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 3), 1).providerId("providerId2"),
                createAdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 5), 1).providerId("providerId3"));

        addAll(adherenceLogs);

        assertEquals(providersWithAdherence, allAdherenceLogs.findProvidersWithAdherence(new LocalDate(2012, 1, 1), new LocalDate(2012, 1, 5)));
    }

    private AdherenceLog createAdherenceLog() {
        return new AdherenceLog("externalId", "treatmentId1", new LocalDate(2012, 1, 1));
    }

    private AdherenceLog createAdherenceLog(String externalId, String treatmentId, LocalDate doseDate, int status) {
        AdherenceLog log = new AdherenceLog(externalId, treatmentId, doseDate);
        log.status(status);
        return log;
    }

    private void addAll(AdherenceLog... adherenceLogs) {
        for (AdherenceLog adherenceLog : adherenceLogs) {
            allAdherenceLogs.add(adherenceLog);
        }
    }

    private void addAll(List<AdherenceLog> adherenceLogs) {
        for (AdherenceLog adherenceLog : adherenceLogs) {
            allAdherenceLogs.add(adherenceLog);
        }
    }
}
