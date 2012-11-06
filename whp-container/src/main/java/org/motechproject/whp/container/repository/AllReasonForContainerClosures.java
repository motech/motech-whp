package org.motechproject.whp.container.repository;


import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.BusinessIdNotUniqueException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.All;

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

    @View(name = "find_by_treatmentPhase", map = "function(doc) {if (doc.type ==='ReasonForContainerClosure' && doc.phase) {emit(doc.phase, doc._id);}}")
    public List<ReasonForContainerClosure> withTreatmentPhase(ApplicableTreatmentPhase phase) {
        ViewQuery viewQuery = createQuery("find_by_treatmentPhase").keys(asList(phase, All)).includeDocs(true);
        return db.queryView(viewQuery, ReasonForContainerClosure.class);
    }

    @View(name = "find_by_applicableToAdmin", map = "function(doc) {if (doc.type ==='ReasonForContainerClosure') {emit(doc.applicableToAdmin, doc._id);}}")
    public List<ReasonForContainerClosure> withApplicableToAdmin(boolean applicableToAdmin) {
        ViewQuery viewQuery = createQuery("find_by_applicableToAdmin").key(applicableToAdmin).includeDocs(true);
        return db.queryView(viewQuery, ReasonForContainerClosure.class);
    }

    @View(name = "find_by_applicableToAdmin_and_phase", map = "function(doc) {if (doc.type ==='ReasonForContainerClosure' && doc.phase) {emit([doc.applicableToAdmin, doc.phase], doc._id);}}")
    public List<ReasonForContainerClosure> withApplicableToAdminAndWithPhase(boolean applicableToAdmin, ApplicableTreatmentPhase phase) {
        ViewQuery viewQuery = createQuery("find_by_applicableToAdmin_and_phase").keys(asList(ComplexKey.of(applicableToAdmin, phase), ComplexKey.of(applicableToAdmin, All))).includeDocs(true);
        return db.queryView(viewQuery, ReasonForContainerClosure.class);
    }
}