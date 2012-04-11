package org.motechproject.whp.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.domain.DosageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllDosageLogs extends MotechBaseRepository<DosageLog> {

    @Autowired
    protected AllDosageLogs(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(DosageLog.class, db);
    }

    @Override
    public void add(DosageLog dosageLog) {
        DosageLog existingLog = findByPatientIdAndDateRange(dosageLog.getPatientId(), dosageLog.getFromDate(), dosageLog.getToDate());
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

    @View(name = "findByPatientIdAndDateRange", map = "function(doc) {if (doc.type =='DosageLog') {emit([doc.patientId, doc.fromDate, doc.toDate], doc._id);}}")
    private DosageLog findByPatientIdAndDateRange(String patientId, LocalDate fromDate, LocalDate toDate) {
        final ComplexKey key = ComplexKey.of(patientId, fromDate, toDate);
        ViewQuery q = createQuery("findByPatientIdAndDateRange").key(key).includeDocs(true);
        return singleResult(db.queryView(q, DosageLog.class));
    }
}
