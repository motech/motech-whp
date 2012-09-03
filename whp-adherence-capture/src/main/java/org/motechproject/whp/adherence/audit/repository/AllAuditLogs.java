package org.motechproject.whp.adherence.audit.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class AllAuditLogs extends MotechBaseRepository<AuditLog> {

    @Autowired
    public AllAuditLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(AuditLog.class, dbCouchDbConnector);
    }

    @View(name = "find_by_audit_tb_ids", map = "function(doc) {if (doc.type ==='AuditLog' && doc.remark != null && doc.remark.trim() != '') {emit(doc.tbId, doc._id);}}")
    public List<AuditLog> findByTbIdsWithRemarks(List<String> tbIds) {
        ViewQuery query=createQuery("find_by_audit_tb_ids").keys(tbIds).includeDocs(true);
        List<AuditLog> auditLogs = db.queryView(query, AuditLog.class);

        sortLogsChronologically(auditLogs);
        return auditLogs;
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
