package org.motechproject.whp.containermapping.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.crud.repository.CouchDBCrudRepository;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllProviderContainerMappings extends CouchDBCrudRepository<ProviderContainerMapping> {

    @Autowired
    public AllProviderContainerMappings(@Qualifier("whpDbConnector") CouchDbConnector db) {
        super(ProviderContainerMapping.class, db);
    }

    @GenerateView
    public ProviderContainerMapping findByProviderId(String providerId) {
        if (providerId != null)
            providerId = providerId.toLowerCase();
        ViewQuery find_by_providerId = createQuery("by_providerId").key(providerId).includeDocs(true);
        return singleResult(db.queryView(find_by_providerId, ProviderContainerMapping.class));
    }
}
