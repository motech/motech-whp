package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class AllTreatments extends MotechBaseRepository<Treatment> {

    @Autowired
    public AllTreatments(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Treatment.class, dbCouchDbConnector);
    }

    @Override
    public void add(Treatment treatment) {
         ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!treatment.isValid(errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        super.add(treatment);
    }

    @Override
    public void update(Treatment treatment) {
         ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!treatment.isValid(errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        super.update(treatment);
    }
}
