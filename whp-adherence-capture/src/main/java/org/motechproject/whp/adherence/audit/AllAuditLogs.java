package org.motechproject.whp.adherence.audit;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllAuditLogs extends MotechBaseRepository<AuditLog> {

    @Autowired
    public AllAuditLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(AuditLog.class, dbCouchDbConnector);
    }
}
