package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;
import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

@Service
public class ReasonForContainerClosureSeed {

    @Autowired
    private AllReasonForContainerClosures allReasonForContainerClosures;

    @Seed(priority = 0, version = "4.0")
    public void load() {
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Sputum mapped to patient", CLOSURE_DUE_TO_MAPPING));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Diagnosis TB Negative", TB_NEGATIVE_CODE));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Container lost", "2"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Invalid Container", "3"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient lost to follow-up", "4"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient did not get sputum tested (no reason stated)", "5"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("Patient started on TB treatment elsewhere", "6"));
    }
}
