package org.motechproject.whp.tools;


import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.springframework.beans.factory.annotation.Autowired;

public class CouchDB {
    @Autowired
    private CouchDbInstance couchDbInstance;
    @Autowired
    private CouchDbConnector whpDbConnector;
    @Autowired
    private CouchDbConnector webSecurityDbConnector;

    public void recreate() {
        String dbName = whpDbConnector.getDatabaseName();
        couchDbInstance.deleteDatabase(dbName);
        couchDbInstance.createDatabase(dbName);

        dbName = webSecurityDbConnector.getDatabaseName();
        couchDbInstance.deleteDatabase(dbName);
        couchDbInstance.createDatabase(dbName);
    }
}
