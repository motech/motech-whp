package org.motechproject.whp.provider.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.provider.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/12/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class AllProviders extends MotechBaseRepository<Provider> {

    @Autowired
    public AllProviders(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Provider.class, dbCouchDbConnector);
    }

    @GenerateView
    public Provider findByProviderId(String providerId) {
        ViewQuery find_by_providerId = createQuery("by_providerId").key(providerId).includeDocs(true);
        return singleResult(db.queryView(find_by_providerId, Provider.class));
    }

    public void addOrReplace(Provider provider) {
        addOrReplace(provider, "providerId", provider.getProviderId());
    }

}
