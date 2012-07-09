package org.motechproject.whp.v0.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.v0.domain.TherapyV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllTherapiesV0 extends MotechBaseRepository<TherapyV0> {

    @Autowired
    public AllTherapiesV0(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(TherapyV0.class, dbCouchDbConnector);
    }

}