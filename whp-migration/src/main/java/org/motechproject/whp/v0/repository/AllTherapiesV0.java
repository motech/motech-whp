package org.motechproject.whp.v0.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.v0.domain.TherapyV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllTherapiesV0 extends CouchDbRepositorySupport<TherapyV0> {

    @Autowired
    public AllTherapiesV0(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(TherapyV0.class, dbCouchDbConnector, Therapy.class.getSimpleName());
        initStandardDesignDocument();
    }

    @Override
    @GenerateView
    public List<TherapyV0> getAll() {
        return super.getAll();
    }


}