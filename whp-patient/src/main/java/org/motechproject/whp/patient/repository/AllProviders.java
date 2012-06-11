package org.motechproject.whp.patient.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
            addOrReplace(provider, "providerId", provider.getProviderId());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
        }
    }

    @View(name = "list_alphabetically_sorted", map = "function(doc) {if (doc.type ==='Provider') {emit([doc.providerId, doc.district, doc.primaryMobile, doc.secondaryMobile, doc.tertiaryMobile], doc._id);}}")
    public List<Provider> list() {
        ViewQuery q = createQuery("list_alphabetically_sorted").includeDocs(true);
        List<Provider> providers = db.queryView(q, Provider.class);
        return providers;
    }
}
