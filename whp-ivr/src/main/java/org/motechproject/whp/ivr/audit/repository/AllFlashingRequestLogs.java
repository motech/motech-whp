package org.motechproject.whp.ivr.audit.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.ivr.audit.domain.FlashingRequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllFlashingRequestLogs extends MotechBaseRepository<FlashingRequestLog> {
    @Autowired
    public AllFlashingRequestLogs(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(FlashingRequestLog.class, dbCouchDbConnector);
    }
}
