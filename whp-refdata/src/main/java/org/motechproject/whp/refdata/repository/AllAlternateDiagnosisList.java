package org.motechproject.whp.refdata.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.refdata.domain.AlternateDiagnosisList;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllAlternateDiagnosisList extends MotechBaseRepository<AlternateDiagnosisList> {

    @Autowired
    public AllAlternateDiagnosisList(@Qualifier("whpDbConnector") CouchDbConnector whpDbConnector) {
        super(AlternateDiagnosisList.class, whpDbConnector);
    }

    public void addOrReplace(AlternateDiagnosisList alternateDiagnosisList) {
        try {
            super.addOrReplace(alternateDiagnosisList, "code", alternateDiagnosisList.getCode());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
      }
    }

    @GenerateView
    public AlternateDiagnosisList findByCode(String code) {
        if (code == null)
            return null;
        ViewQuery find_by_code = createQuery("by_code").key(code).includeDocs(true);
        return singleResult(db.queryView(find_by_code, AlternateDiagnosisList.class));
    }
}