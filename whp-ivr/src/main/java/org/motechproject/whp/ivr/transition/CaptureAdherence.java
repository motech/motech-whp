package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Prompt;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED;
import static org.motechproject.whp.ivr.WHPIVRMessage.NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE;

@Component
public class CaptureAdherence implements ITransition {

    @Autowired
    private AdherenceDataService adherenceDataService;
    @Autowired
    private WHPIVRMessage whpivrMessage;
    @Autowired
    private AllProviders allProviders;

    public CaptureAdherence() {
    }

    public CaptureAdherence(AdherenceDataService adherenceDataService, WHPIVRMessage whpivrMessage, AllProviders allProviders) {
        this.adherenceDataService = adherenceDataService;
        this.whpivrMessage = whpivrMessage;
        this.allProviders = allProviders;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        String mobileNumber = flowSession.get("cid");
        String providerId = allProviders.findByPrimaryMobileNumber(mobileNumber).getProviderId();

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);
        Prompt[] prompts = new PromptBuilder(whpivrMessage)
                .audio(NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithAdherence()))
                .audio(NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithoutAdherence()))
                .build();

        return new Node().setPrompts(prompts);
    }
}
