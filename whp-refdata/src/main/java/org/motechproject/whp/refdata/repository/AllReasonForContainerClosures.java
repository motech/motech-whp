package org.motechproject.whp.refdata.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllReasonForContainerClosures extends MotechBaseRepository<ReasonForContainerClosure> {

    @Autowired
    public AllReasonForContainerClosures(@Qualifier("whpDbConnector") CouchDbConnector whpDbConnector) {
        super(ReasonForContainerClosure.class, whpDbConnector);
    }

    public void addOrReplace(ReasonForContainerClosure reasonForContainerClosure) {
        try {
            super.addOrReplace(reasonForContainerClosure, "name", reasonForContainerClosure.getName());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
      }
    }

    @GenerateView
    public ReasonForContainerClosure findByName(String name) {
        if (name == null)
            return null;
        ViewQuery find_by_name = createQuery("by_name").key(name).includeDocs(true);
        return singleResult(db.queryView(find_by_name, ReasonForContainerClosure.class));
    }
}