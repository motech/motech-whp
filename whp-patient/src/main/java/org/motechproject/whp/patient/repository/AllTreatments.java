package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllTreatments extends MotechBaseRepository<Treatment> {

    @Autowired
    public AllTreatments(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Treatment.class, dbCouchDbConnector);
    }

    @Override
    public void add(Treatment treatment) {
        ValidationErrors validationErrors = new ValidationErrors();
        if (!treatment.isValid(validationErrors)) {
            throw new WHPDomainException("Invalid treatment data." + validationErrors);
        }
        super.add(treatment);
    }

    @Override
    public void update(Treatment treatment) {
        ValidationErrors validationErrors = new ValidationErrors();
        if (!treatment.isValid(validationErrors)) {
            throw new WHPDomainException("Invalid treatment data." + validationErrors);
        }
        super.update(treatment);
    }
}
