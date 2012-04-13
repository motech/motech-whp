package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Patient;
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
public class AllPatients  extends MotechBaseRepository<Patient> {

    @Autowired
    public AllPatients(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Patient.class, dbCouchDbConnector);
    }

    @GenerateView
    public Patient findByPatientId(String patientId) {
        ViewQuery find_by_patientId = createQuery("by_patientId").key(patientId).includeDocs(true);
        return singleResult(db.queryView(find_by_patientId, Patient.class));
    }
}
