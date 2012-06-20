package org.motechproject.whp.refdata.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.common.exception.WHPRuntimeException;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.refdata.domain.CmfLocation;
import org.motechproject.whp.refdata.domain.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllDistricts extends MotechBaseRepository<District> {

    @Autowired
    public AllDistricts(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(District.class, dbCouchDbConnector);
    }

}
