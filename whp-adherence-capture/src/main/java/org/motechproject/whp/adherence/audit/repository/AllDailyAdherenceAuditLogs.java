package org.motechproject.whp.adherence.audit.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllDailyAdherenceAuditLogs extends MotechBaseRepository<DailyAdherenceAuditLog> {

    @Autowired
    public AllDailyAdherenceAuditLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(DailyAdherenceAuditLog.class, dbCouchDbConnector);
    }

    @View(name = "by_patientId", map = "function(doc) {if (doc.type =='DailyAdherenceAuditLog') {emit([doc.patientId], doc._id);}}")
    public List<DailyAdherenceAuditLog> findByPatientId(String patientId){
        final ComplexKey startKey = ComplexKey.of(patientId.toLowerCase());
        final ComplexKey endKey =  startKey;
        ViewQuery q = createQuery("by_patientId").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        return db.queryView(q, DailyAdherenceAuditLog.class);
    }

    public void remove(List<DailyAdherenceAuditLog> dailyAdherenceAuditLogs){
        for (DailyAdherenceAuditLog dailyAdherenceAuditLog : dailyAdherenceAuditLogs){
            remove(dailyAdherenceAuditLog);
        }
    }
}
