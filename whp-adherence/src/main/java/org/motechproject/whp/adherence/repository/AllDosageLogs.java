package org.motechproject.whp.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.domain.DosageLog;
import org.motechproject.whp.adherence.domain.DosageSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllDosageLogs extends MotechBaseRepository<DosageLog> {

    @Autowired
    protected AllDosageLogs(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(DosageLog.class, db);
    }

    @Override
    public void add(DosageLog dosageLog) {
        DosageLog existingLog = getByPatientIdAndDateRange(dosageLog.getPatientId(), dosageLog.getFromDate(), dosageLog.getToDate());
        if (existingLog == null) {
            super.add(dosageLog);
        } else {
            dosageLog.setId(existingLog.getId());
            dosageLog.setRevision(existingLog.getRevision());
            existingLog.addMetaData(dosageLog.getMetaData());
            dosageLog.setMetaData(existingLog.getMetaData());
            update(dosageLog);
        }
    }

    @View(name = "byPatientIdAndDateRange", map = "function(doc) {if (doc.type =='DosageLog') {emit([doc.patientId, doc.fromDate, doc.toDate], doc._id);}}")
    private DosageLog getByPatientIdAndDateRange(String patientId, LocalDate fromDate, LocalDate toDate) {
        final ComplexKey key = ComplexKey.of(patientId, fromDate, toDate);
        ViewQuery q = createQuery("byPatientIdAndDateRange").key(key).includeDocs(true);
        return singleResult(db.queryView(q, DosageLog.class));
    }

    public List<DosageLog> getAllByPatientIdAndDateRange(String patientId, LocalDate fromDate, LocalDate toDate) {
        final ComplexKey startKey = ComplexKey.of(patientId, fromDate);
        final ComplexKey endKey = ComplexKey.of(patientId, toDate);
        ViewQuery q = createQuery("byPatientIdAndDateRange").startKey(startKey).endKey(endKey).includeDocs(true);
        return db.queryView(q, DosageLog.class);
    }

    @View(name = "byDateRange", map = "function(doc) {if (doc.type =='DosageLog') {emit([doc.fromDate, doc.toDate], doc._id);}}")
    public List<DosageLog> getAllInDateRange(LocalDate fromDate, LocalDate toDate) {
        final ComplexKey startKey = ComplexKey.of(fromDate);
        final ComplexKey endKey = ComplexKey.of(toDate);
        ViewQuery q = createQuery("byDateRange").startKey(startKey).endKey(endKey).includeDocs(true);
        return db.queryView(q, DosageLog.class);
    }

    @View(name = "patientSummaryByDateRange", file = "patientSummaryByDateRange.json")
    public DosageSummary getPatientDosageSummary(String patientId, LocalDate fromDate, LocalDate toDate) {
        final ComplexKey startKey = ComplexKey.of(patientId, fromDate);
        final ComplexKey endKey = ComplexKey.of(patientId, toDate);
        ViewQuery q = createQuery("patientSummaryByDateRange").startKey(startKey).endKey(endKey).reduce(true).groupLevel(1);
        List<DosageSummary> resultSet = db.queryView(q, DosageSummary.class);
        return (resultSet == null || resultSet.isEmpty()) ? null : resultSet.get(0);
    }
}
