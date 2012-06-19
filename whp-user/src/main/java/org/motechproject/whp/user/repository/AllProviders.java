package org.motechproject.whp.user.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.common.exception.WHPRuntimeException;
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

    @View(name = "list_district_sorted", map = "function(doc) {if (doc.type ==='Provider') {emit(doc.district, doc._id);}}")
    public List<Provider> list() {
        ViewQuery q = createQuery("list_district_sorted").includeDocs(true);
        return db.queryView(q, Provider.class);
    }
}
