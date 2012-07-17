package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.model.*;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.ivr.WHPIVRMessage.*;

@Component
public class CaptureAdherence implements ITransition {

    @Autowired
    private AdherenceDataService adherenceDataService;

    @Autowired
    private WHPIVRMessage whpivrMessage;

    public CaptureAdherence() {
    }

    public CaptureAdherence(AdherenceDataService adherenceDataService, WHPIVRMessage whpivrMessage) {
        this.adherenceDataService = adherenceDataService;
        this.whpivrMessage = whpivrMessage;
    }

    @Override
    public Node getDestinationNode(String input) {
        //TODO: get these from context when it is implemented
        String providerId = "providerId";
        String preferredLanguage = "";

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);

        Prompt[] prompts = new PromptBuilder(whpivrMessage, preferredLanguage)
                .audio(NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithAdherence()))
                .audio(NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE)
                .audio(String.valueOf(adherenceSummary.countOfPatientsWithoutAdherence()))
                .build();

        return new Node().setPrompts(prompts);
    }
}
