package org.motechproject.whp.user.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllProviders extends MotechBaseRepository<Provider> {

    @Autowired
    public AllProviders(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Provider.class, dbCouchDbConnector);
    }

    @GenerateView
    public Provider findByProviderId(String providerId) {
        if(providerId==null)
            return null;
        ViewQuery find_by_providerId = createQuery("by_providerId").key(providerId.toLowerCase()).includeDocs(true);
        return singleResult(db.queryView(find_by_providerId, Provider.class));
    }

    public void addOrReplace(Provider provider) {
        try {
            super.addOrReplace(provider, "providerId", provider.getProviderId());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
        }
    }

    @View(name = "find_by_district_and_provider_id", map = "function(doc) {if (doc.type ==='Provider') {emit([doc.district, doc.providerId], doc._id);}}")
    public List<Provider> findByDistrictAndProviderId(String district, String providerId) {
        ViewQuery q = createQuery("find_by_district_and_provider_id").startKey(ComplexKey.of(district, providerId)).endKey(ComplexKey.of(district, providerId)).includeDocs(true);
        return db.queryView(q, Provider.class);
    }

    public List<Provider> findByDistrict(String district) {
        ViewQuery q = createQuery("find_by_district_and_provider_id").startKey(ComplexKey.of(district)).endKey(ComplexKey.of(district, ComplexKey.emptyObject())).includeDocs(true);
        return db.queryView(q, Provider.class);
    }

}
