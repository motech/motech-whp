package org.motechproject.whp.container.service;

import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.InTreatment;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.PreTreatment;

@Service
public class ReasonsForClosureService {
    AllReasonForContainerClosures allReasonForContainerClosures;

    @Autowired
    public ReasonsForClosureService(AllReasonForContainerClosures allReasonForContainerClosures) {
        this.allReasonForContainerClosures = allReasonForContainerClosures;
    }

    public List<ReasonForContainerClosure> getAllReasonsPreTreatmentClosureReasons() {
        return allReasonForContainerClosures.withTreatmentPhase(PreTreatment);
    }

    public List<ReasonForContainerClosure> getAllInTreatmentClosureReasons() {
        return allReasonForContainerClosures.withTreatmentPhase(InTreatment);
    }

    public List<ReasonForContainerClosure> getAllPreTreatmentClosureReasonsForAdmin() {
        return allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment);
    }

    public List<ReasonForContainerClosure> getAllInTreatmentClosureReasonsForAdmin() {
        return allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, InTreatment);
    }

    public ReasonForContainerClosure getClosureReasonForMapping() {
        return allReasonForContainerClosures.findByCode(CLOSURE_DUE_TO_MAPPING);
    }
}
