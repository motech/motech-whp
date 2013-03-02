package org.motechproject.whp.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

@Repository
public class AllAdherenceLogs extends MotechBaseRepository<AdherenceLog> {

    @Autowired
    protected AllAdherenceLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceLog.class, db);
    }

    @Override
    public void add(AdherenceLog adherenceLog) {
        AdherenceLog existingLog = findLogBy(adherenceLog.externalId(), adherenceLog.treatmentId(), adherenceLog.doseDate());
        if (existingLog == null) {
            super.add(adherenceLog);
        } else {
            existingLog.status(adherenceLog.status());
            existingLog.providerId(adherenceLog.providerId());
            existingLog.district(adherenceLog.district());
            existingLog.tbId(adherenceLog.tbId());
            update(existingLog);
        }
    }

    @View(name = "by_externaId_treatmentId_andDosageDate", map = "function(doc) {if (doc.type =='AdherenceLog') {emit([doc.externalId, doc.treatmentId, doc.doseDate], doc._id);}}")
    public List<AdherenceLog> findLogsBy(String externalId, String treatmentId, LocalDate asOf) {
        final ComplexKey startKey = ComplexKey.of(externalId, treatmentId, null);
        final ComplexKey endKey = ComplexKey.of(externalId, treatmentId, asOf);
        ViewQuery q = createQuery("by_externaId_treatmentId_andDosageDate").startKey(startKey).endKey(endKey).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, AdherenceLog.class);
    }

    public AdherenceLog findLogBy(String externalId, String treatmentId, LocalDate asOf) {
        final ComplexKey key = ComplexKey.of(externalId, treatmentId, asOf);
        ViewQuery q = createQuery("by_externaId_treatmentId_andDosageDate").key(key).includeDocs(true);
        return singleResult(db.queryView(q, AdherenceLog.class));
    }

    @View(name = "by_dosageDate", map = "function(doc) {if (doc.type =='AdherenceLog' && doc.status !== 0 ) {emit(doc.doseDate, {externalId:doc.externalId, treatmentId:doc.treatmentId, doseDate:doc.doseDate, status:doc.status, meta:doc.meta});}}")
    public String viewForAdherenceLogsByDoseDate(){
        return "by_dosageDate";
    }

    @View(name = "by_dateRangeExternalIdAndTherapy", map = "function(doc) {if (doc.type =='AdherenceLog') {emit([doc.externalId, doc.treatmentId, doc.doseDate], {externalId:doc.externalId, treatmentId:doc.treatmentId, doseDate:doc.doseDate, status:doc.status, meta:doc.meta});}}")
    public List<AdherenceRecord> findLogsInRange(String externalId, String treatmentId, LocalDate startDate, LocalDate endDate) {
        final ComplexKey startKey = ComplexKey.of(externalId, treatmentId, startDate);
        final ComplexKey endKey = ComplexKey.of(externalId, treatmentId, endDate);
        ViewQuery q = createQuery("by_dateRangeExternalIdAndTherapy").startKey(startKey).endKey(endKey).inclusiveEnd(true);
        return db.queryView(q, AdherenceRecord.class);
    }

    @View(name = "by_externalId_dosageDate", map = "function(doc) {if (doc.type =='AdherenceLog') {emit([doc.externalId,doc.doseDate], doc._id);}}")
    public List<AdherenceLog> findAllLogsForExternalIdInDoseDateRange(String externalId, LocalDate startDate, LocalDate endDate) {
        final ComplexKey startKey = ComplexKey.of(externalId, startDate);
        final ComplexKey endKey = ComplexKey.of(externalId, endDate);

        ViewQuery q = createQuery("by_externalId_dosageDate").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        return db.queryView(q, AdherenceLog.class);
    }

    public int countOfDosesTakenBetween(String patientId, String treatmentId, LocalDate from, LocalDate to) {
        int status = 1;
        ComplexKey startKey = ComplexKey.of(patientId, treatmentId, status, from);
        ComplexKey endKey = ComplexKey.of(patientId, treatmentId, status, to);

        ViewQuery q = createQuery("all_taken_logs").startKey(startKey).endKey(endKey).inclusiveEnd(true).reduce(true);
        ViewResult viewResult = db.queryView(q);
        if (viewResult.getRows().size() == 0) {
            return 0;
        }
        return viewResult.getRows().get(0).getValueAsInt();
    }

    @View(name = "all_taken_logs", map = "function(doc) {if (doc.type == 'AdherenceLog') {emit([doc.externalId, doc.treatmentId, doc.status, doc.doseDate], {externalId:doc.externalId, treatmentId:doc.treatmentId, doseDate:doc.doseDate, status:doc.status, meta:doc.meta});}}", reduce = "_count")
    public List<AdherenceRecord> allTakenLogsFrom(String patientId, String treatmentId, LocalDate startDate) {
        int status = 1;
        ComplexKey startKey = ComplexKey.of(patientId, treatmentId, status, startDate);
        ComplexKey endKey = ComplexKey.of(patientId, treatmentId, status, ComplexKey.emptyObject());

        ViewQuery q = createQuery("all_taken_logs").startKey(startKey).endKey(endKey).inclusiveEnd(true).reduce(false);
        return db.queryView(q, AdherenceRecord.class);
    }

    public void addOrUpdateLogsForExternalIdByDoseDate(List<AdherenceLog> adherenceLogs, String externalId) {
        if (null != adherenceLogs && adherenceLogs.isEmpty()) {
            return;
        }
        adherenceLogs = sort(adherenceLogs, on(AdherenceLog.class).doseDate());


        List<AdherenceLog> logsInDb = findAllLogsForExternalIdInDoseDateRange(externalId, adherenceLogs.get(0).doseDate(), adherenceLogs.get(adherenceLogs.size() - 1).doseDate());
        ArrayList<AdherenceLog> tobeStoredLogs = mapLogToDbLogsIfExists(adherenceLogs, logsInDb);
        db.executeAllOrNothing(tobeStoredLogs);
    }

    private ArrayList<AdherenceLog> mapLogToDbLogsIfExists(List<AdherenceLog> logsToMap, List<AdherenceLog> dbLogs) {
        Set<AdherenceLog> tobeStoredLogs = new LinkedHashSet<>();   //using set to remove duplicate logs
        List<LocalDate> doseDatesToBeMappedWith = getDoseDates(dbLogs);
        for (AdherenceLog log : logsToMap) {
            int logPos = doseDatesToBeMappedWith.indexOf(log.doseDate());
            if (logPos < 0) {
                tobeStoredLogs.add(log);
            } else {
                AdherenceLog dbLog = dbLogs.get(logPos);
                dbLog.status(log.status());
                dbLog.providerId(log.providerId());
                dbLog.tbId(log.tbId());
                dbLog.treatmentId(log.treatmentId());
                tobeStoredLogs.add(dbLog);
            }
        }
        return new ArrayList<>(tobeStoredLogs);
    }

    private List<LocalDate> getDoseDates(List<AdherenceLog> logs) {
        List<LocalDate> doseDates = new ArrayList<>();
        for (AdherenceLog log : logs)
            doseDates.add(log.doseDate());
        return doseDates;
    }

    public List<AdherenceRecord> allTakenLogs(String patientId, String treatmentId) {
        int status = 1;
        ComplexKey startKey = ComplexKey.of(patientId, treatmentId, status);
        ComplexKey endKey = ComplexKey.of(patientId, treatmentId, status, ComplexKey.emptyObject());

        ViewQuery q = createQuery("all_taken_logs").startKey(startKey).endKey(endKey).inclusiveEnd(true).reduce(false);
        return db.queryView(q, AdherenceRecord.class);
    }

    public List<AdherenceRecord> allLogs(int pageNumber, int pageSize) {
        ViewQuery q = createQuery(viewForAdherenceLogsByDoseDate()).skip(pageNumber * pageSize).limit(pageSize).inclusiveEnd(true);
        return db.queryView(q, AdherenceRecord.class);
    }
}
