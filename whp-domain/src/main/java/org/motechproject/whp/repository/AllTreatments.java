package org.motechproject.whp.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.domain.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllTreatments extends MotechBaseRepository<Treatment> {

    @Autowired
    public AllTreatments(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Treatment.class, dbCouchDbConnector);
    }

}
