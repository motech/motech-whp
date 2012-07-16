package org.motechproject.whp.adherence.audit.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.adherence.audit.domain.DailyAdherenceAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllDailyAdherenceAuditLogs extends MotechBaseRepository<DailyAdherenceAuditLog> {

    @Autowired
    public AllDailyAdherenceAuditLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(DailyAdherenceAuditLog.class, dbCouchDbConnector);
    }
}
