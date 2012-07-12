package org.motechproject.whp.migration.v1.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllPatientsV1 extends MotechBaseRepository<Patient> {

    @Autowired
    public AllPatientsV1(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Patient.class, dbCouchDbConnector);
    }

}
