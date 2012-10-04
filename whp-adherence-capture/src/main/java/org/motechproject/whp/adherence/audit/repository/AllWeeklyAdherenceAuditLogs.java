package org.motechproject.whp.adherence.audit.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class AllWeeklyAdherenceAuditLogs extends MotechBaseRepository<AuditLog> {

    @Autowired
    public AllWeeklyAdherenceAuditLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(AuditLog.class, dbCouchDbConnector);
    }

    @View(name = "find_by_audit_tb_ids", map = "function(doc) {if (doc.type ==='AuditLog' && doc.remark != null && doc.remark != '') {emit(doc.tbId, doc._id);}}")
    public List<AuditLog> findByTbIdsWithRemarks(List<String> tbIds) {
        ViewQuery query=createQuery("find_by_audit_tb_ids").keys(tbIds).includeDocs(true);
        List<AuditLog> auditLogs = db.queryView(query, AuditLog.class);

        sortLogsChronologically(auditLogs);
        return auditLogs;
    }

    @View(name = "by_creationTime", map = "function(doc) {if (doc.type =='AuditLog') {emit(doc.creationTime, doc._id);}}")
    public List<AuditLog> findLogsAsOf(DateTime asOf, int pageNumber, int pageSize) {
        ViewQuery q = createQuery("by_creationTime").endKey(asOf).skip(pageNumber * pageSize).limit(pageSize).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, AuditLog.class);
    }

    private void sortLogsChronologically(List<AuditLog> auditLogs) {
        Collections.sort(auditLogs, new Comparator<AuditLog>() {
            @Override
            public int compare(AuditLog auditLog1, AuditLog auditLog2) {
                return auditLog2.getCreationTime().compareTo(auditLog1.getCreationTime());
            }
        });
    }
}
