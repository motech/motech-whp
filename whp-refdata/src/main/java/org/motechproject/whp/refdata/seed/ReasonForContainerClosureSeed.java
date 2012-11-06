package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;
import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.All;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.PreTreatment;

@Service
public class ReasonForContainerClosureSeed {

    @Autowired
    private AllReasonForContainerClosures allReasonForContainerClosures;

    @Seed(priority = 0, version = "4.0")
    public void load() {
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Sputum container mapped to patient", CLOSURE_DUE_TO_MAPPING, All, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Diagnosis TB Negative", TB_NEGATIVE_CODE, PreTreatment, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Container lost", "2", All, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Invalid Container", "3", All, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient lost to follow-up", "4", PreTreatment, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient did not get sputum tested (no reason stated)", "5", All, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient did not get sputum tested (cough improved)", "6", All, true));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient started on TB treatment elsewhere", "7", PreTreatment, true));
    }
}

