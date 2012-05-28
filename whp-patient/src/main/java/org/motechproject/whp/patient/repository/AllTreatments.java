package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class AllTreatments extends MotechBaseRepository<Treatment> {

    @Autowired
    public AllTreatments(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Treatment.class, dbCouchDbConnector);
    }
}
