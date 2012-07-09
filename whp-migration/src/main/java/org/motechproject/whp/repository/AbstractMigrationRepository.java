package org.motechproject.whp.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.motechproject.model.MotechBaseDataObject;

import java.util.List;

public abstract class AbstractMigrationRepository<T extends MotechBaseDataObject> extends CouchDbRepositorySupport<T> {

    protected AbstractMigrationRepository(Class<T> type, CouchDbConnector db, String designDocType) {
        super(type, db, designDocType);
    }

    @Override
    @GenerateView
    public List<T> getAll() {
        return super.getAll();
    }

}
