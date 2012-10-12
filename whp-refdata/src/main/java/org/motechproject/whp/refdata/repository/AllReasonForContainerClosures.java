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
            super.addOrReplace(reasonForContainerClosure, "code", reasonForContainerClosure.getCode());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
      }
    }

    @GenerateView
    public ReasonForContainerClosure findByCode(String code) {
        if (code == null)
            return null;
        ViewQuery find_by_code = createQuery("by_code").key(code).includeDocs(true);
        return singleResult(db.queryView(find_by_code, ReasonForContainerClosure.class));
    }
}