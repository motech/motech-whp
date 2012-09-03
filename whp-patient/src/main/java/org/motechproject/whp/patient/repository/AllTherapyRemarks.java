package org.motechproject.whp.patient.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllTherapyRemarks extends MotechBaseRepository<TherapyRemark> {


    @Autowired
    public AllTherapyRemarks(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(TherapyRemark.class, dbCouchDbConnector);
    }

    @View(name = "find_by_therapy_id", map = "function(doc) {if (doc.type ==='TherapyRemark') {emit([doc.therapyUid, doc.creationTime], doc._id);}}")
    public List<TherapyRemark> findByTherapyId(String therapyId) {
        ComplexKey startKey = ComplexKey.of(therapyId, ComplexKey.emptyObject());
        ComplexKey endKey = ComplexKey.of(therapyId, null);

        ViewQuery viewQuery = createQuery("find_by_therapy_id").endKey(endKey).startKey(startKey).includeDocs(true).inclusiveEnd(true).descending(true);

        return db.queryView(viewQuery, TherapyRemark.class);
    }

}
