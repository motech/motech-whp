package org.motechproject.whp.refdata.repository;


import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.refdata.domain.AlternateDiagnosis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllAlternateDiagnosis extends MotechBaseRepository<AlternateDiagnosis> {

    @Autowired
    public AllAlternateDiagnosis(@Qualifier("whpDbConnector") CouchDbConnector whpDbConnector) {
        super(AlternateDiagnosis.class, whpDbConnector);
    }

    public void addOrReplace(AlternateDiagnosis alternateDiagnosis) {
        try {
            super.addOrReplace(alternateDiagnosis, "code", alternateDiagnosis.getCode());
        } catch (BusinessIdNotUniqueException e) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_PROVIDER_ID);
      }
    }

    @GenerateView
    public AlternateDiagnosis findByCode(String code) {
        if (code == null)
            return null;
        ViewQuery find_by_code = createQuery("by_code").key(code).includeDocs(true);
        return singleResult(db.queryView(find_by_code, AlternateDiagnosis.class));
    }
}