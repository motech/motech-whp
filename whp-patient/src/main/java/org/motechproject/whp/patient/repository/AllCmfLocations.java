package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.CmfLocation;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllCmfLocations extends MotechBaseRepository<CmfLocation> {

    @Autowired
    public AllCmfLocations(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(CmfLocation.class, dbCouchDbConnector);
    }

   public void addOrReplace(CmfLocation location) {
        try {
            super.addOrReplace(location, "location", location.getLocation());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
        }
    }

    @GenerateView
    public CmfLocation findByLocation(String location) {
        if(location==null)
            return null;
        ViewQuery find_by_location = createQuery("by_location").key(location).includeDocs(true);
        return singleResult(db.queryView(find_by_location, CmfLocation.class));
    }

}
