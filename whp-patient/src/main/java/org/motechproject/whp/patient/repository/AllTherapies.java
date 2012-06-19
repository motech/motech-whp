package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Therapy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllTherapies extends MotechBaseRepository<Therapy> {

    @Autowired
    public AllTherapies(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Therapy.class, dbCouchDbConnector);
    }

}
