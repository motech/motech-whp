package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;


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

        String providerId = allProviders.findByPrimaryMobileNumber(ivrSession.getMobileNumber()).getProviderId();
        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);

        if(ivrSession.providerId() == null){
            ivrSession.providerId(providerId);
            ivrSession.setPatientsWithoutAdherence(adherenceSummary.getAllPatientsWithoutAdherence());
        }

        List<String> patientsWithoutAdherence = adherenceSummary.getAllPatientsWithoutAdherence();

        Node captureAdherenceNode = new Node();
        captureAdherenceNode.addPrompts(adherenceSummaryPrompts(whpivrMessage, adherenceSummary));

        if (patientsWithoutAdherence.size() == 0) {
            return captureAdherenceNode;
        }

        captureAdherenceNode.addPrompts(CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, patientsWithoutAdherence.get(0), 1));
        captureAdherenceNode.addTransition("?", new AdherenceCaptureTransition());

        ivrSession.setPatientsWithoutAdherence(patientsWithoutAdherence);
        return captureAdherenceNode;
    }

}
