package org.motechproject.whp.ivr.transition;


import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts.confirmAdherencePrompts;

@EqualsAndHashCode
public class ConfirmAdherenceTransition extends TransitionToCollectPatientAdherence {

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    public ConfirmAdherenceTransition() {
    }

    public ConfirmAdherenceTransition(WhpIvrMessage whpIvrMessage,
                                      WHPAdherenceService adherenceService,
                                      TreatmentUpdateOrchestrator treatmentUpdateOrchestrator,
                                      ReportingPublisherService reportingPublisherService) {

        super(whpIvrMessage, reportingPublisherService);

        this.adherenceService = adherenceService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.reportingPublisherService = reportingPublisherService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatientId();

        Node nextNode = new Node();
        switch (input) {
            case "1":
                ivrSession.recordAdherenceForCurrentPatient();
                nextNode.addOperations(new RecordAdherenceOperation(currentPatientId, treatmentUpdateOrchestrator, reportingPublisherService));
                addTransitionsAndPromptsForNextPatient(ivrSession, nextNode);
                break;
            case "2":
                addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
                break;
            default:
                nextNode.addOperations(new GetAdherenceOperation());
                nextNode.addPrompts(confirmAdherencePrompts(whpIvrMessage));
                nextNode.addTransition("?", new ConfirmAdherenceTransition());
        }
        return nextNode;
    }

}
