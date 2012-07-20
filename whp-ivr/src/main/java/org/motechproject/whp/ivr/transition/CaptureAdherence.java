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

import static org.motechproject.whp.ivr.WHPIVRMessage.*;

@Component
public class CaptureAdherence implements ITransition {

    public static final String ADHERENCE_PROVIDED_FOR = "instructionalMessage1";
    public static final String ADHERENCE_TO_BE_PROVIDED_FOR = "instructionalMessage2";
    public static final String ADHERENCE_CAPTURE_INSTRUCTION = "instructionalMessage3";

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
                .wav(MUSIC_ENTER)
                .wav(WELCOME_MESSAGE)
                .wav(ADHERENCE_PROVIDED_FOR)
                .number(adherenceSummary.countOfPatientsWithAdherence())
                .wav(ADHERENCE_TO_BE_PROVIDED_FOR)
                .number(adherenceSummary.countOfPatientsWithoutAdherence())
                .wav(ADHERENCE_CAPTURE_INSTRUCTION)
                .build();

        return new Node().setPrompts(prompts);
    }
}
