package org.motechproject.whp.adherence.audit.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.audit.domain.AdherenceAuditLog;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllAdherenceAuditLogs extends MotechBaseRepository<AuditLog> {

    @Autowired
    protected AllAdherenceAuditLogs(CouchDbConnector whpDbConnector) {
        super(AuditLog.class, whpDbConnector);
    }

    @View(name = "all_adherence_audit_logs", map = "function(doc) {if (doc.type == 'AuditLog') {emit(doc.creationTime, {patientId:doc.patientId, providerId:doc.providerId, tbId:doc.tbId, creationTime:doc.creationTime, doseDate:null, userId:doc.user, numberOfDosesTaken:doc.numberOfDosesTaken, pillStatus:null, sourceOfChange: doc.sourceOfChange});} " +
            "  if(doc.type == 'DailyAdherenceAuditLog') {emit(doc.creationTime, {patientId:doc.patientId, providerId:doc.providerId, tbId:doc.tbId, creationTime:doc.creationTime, doseDate:doc.pillDate, userId:doc.user, numberOfDosesTaken:null, pillStatus:doc.pillStatus, sourceOfChange: doc.sourceOfChange});} }")
    public List<AdherenceAuditLog> allLogs(int pageNo, int pageSize) {
        ViewQuery q = createQuery("all_adherence_audit_logs").skip(pageNo * pageSize).limit(pageSize).inclusiveEnd(true);
        return db.queryView(q, AdherenceAuditLog.class);
    }

    @View(name = "by_patientId", map = "function(doc) {if (doc.type =='AuditLog') {emit([doc.patientId], doc._id);}}")
    public List<AuditLog> findByPatientId(String patientId){
        final ComplexKey startKey = ComplexKey.of(patientId.toLowerCase());
        final ComplexKey endKey =  startKey;
        ViewQuery q = createQuery("by_patientId").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        return db.queryView(q, AuditLog.class);
    }

    public void remove(List<AuditLog> auditLogs){
        for (AuditLog auditLog : auditLogs){
            remove(auditLog);
        }
    }
}
