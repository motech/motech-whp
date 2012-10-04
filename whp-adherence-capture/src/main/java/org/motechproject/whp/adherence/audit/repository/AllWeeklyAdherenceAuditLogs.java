package org.motechproject.whp.adherence.audit.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.audit.domain.WeeklyAdherenceAuditLog;
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
public class AllWeeklyAdherenceAuditLogs extends MotechBaseRepository<WeeklyAdherenceAuditLog> {

    @Autowired
    public AllWeeklyAdherenceAuditLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(WeeklyAdherenceAuditLog.class, dbCouchDbConnector);
    }

    @View(name = "find_by_audit_tb_ids", map = "function(doc) {if (doc.type ==='AuditLog' && doc.remark != null && doc.remark != '') {emit(doc.tbId, doc._id);}}")
    public List<WeeklyAdherenceAuditLog> findByTbIdsWithRemarks(List<String> tbIds) {
        ViewQuery query=createQuery("find_by_audit_tb_ids").keys(tbIds).includeDocs(true);
        List<WeeklyAdherenceAuditLog> weeklyAdherenceAuditLogs = db.queryView(query, WeeklyAdherenceAuditLog.class);

        sortLogsChronologically(weeklyAdherenceAuditLogs);
        return weeklyAdherenceAuditLogs;
    }

    private void sortLogsChronologically(List<WeeklyAdherenceAuditLog> weeklyAdherenceAuditLogs) {
        Collections.sort(weeklyAdherenceAuditLogs, new Comparator<WeeklyAdherenceAuditLog>() {
            @Override
            public int compare(WeeklyAdherenceAuditLog weeklyAdherenceAuditLog1, WeeklyAdherenceAuditLog weeklyAdherenceAuditLog2) {
                return weeklyAdherenceAuditLog2.getCreationTime().compareTo(weeklyAdherenceAuditLog1.getCreationTime());
            }
        });
    }
}
