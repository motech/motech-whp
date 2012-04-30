package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllTreatmentCategories extends MotechBaseRepository<TreatmentCategory> {

    @Autowired
    public AllTreatmentCategories(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(TreatmentCategory.class, dbCouchDbConnector);
    }

    @GenerateView
    public TreatmentCategory findByCode(String categoryCode) {
        ViewQuery find_by_code = createQuery("by_code").key(categoryCode).includeDocs(true);
        return singleResult(db.queryView(find_by_code, TreatmentCategory.class));
    }

}
