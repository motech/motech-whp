package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;


@Component
public class AdherenceSummaryTransition implements ITransition {

    @Autowired
    private AdherenceDataService adherenceDataService;
    @Autowired
    private WHPIVRMessage whpivrMessage;
    @Autowired
    private AllProviders allProviders;

    public AdherenceSummaryTransition() {
    }

    public AdherenceSummaryTransition(AdherenceDataService adherenceDataService, WHPIVRMessage whpivrMessage, AllProviders allProviders) {
        this.adherenceDataService = adherenceDataService;
        this.whpivrMessage = whpivrMessage;
        this.allProviders = allProviders;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);

        if(ivrSession.providerId() == null){
            String providerId = allProviders.findByPrimaryMobileNumber(ivrSession.getMobileNumber()).getProviderId();
            AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);

            ivrSession.providerId(providerId);
            ivrSession.patientsWithoutAdherence(adherenceSummary.getAllPatientsWithoutAdherence());
            ivrSession.patientsWithAdherence(adherenceSummary.getAllPatientsWithAdherence());
        }

        Node captureAdherenceNode = new Node();

        captureAdherenceNode.addPrompts(adherenceSummaryPrompts(whpivrMessage, ivrSession.patientsWithAdherence(), ivrSession.patientsWithoutAdherence()));

        if (ivrSession.hasPatientsWithoutAdherence()) {
            captureAdherenceNode.addPrompts(captureAdherencePrompts(whpivrMessage, ivrSession.currentPatientId(), ivrSession.currentPatientNumber()));
            captureAdherenceNode.addTransition("?", new AdherenceCaptureTransition());

            return captureAdherenceNode;
        } else {
            captureAdherenceNode.addPrompts(callCompletionPrompts(whpivrMessage));
            return captureAdherenceNode;
        }
    }

}
