package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllTherapyRemarks extends MotechBaseRepository<TherapyRemark> {


    @Autowired
    public AllTherapyRemarks(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(TherapyRemark.class, dbCouchDbConnector);
    }

}
