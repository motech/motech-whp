package org.motechproject.whp.ivr.transition;


import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.operation.ResetFlowSessionOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;

@EqualsAndHashCode
public class ConfirmAdherenceTransition extends TransitionToCollectPatientAdherence {
    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    public ConfirmAdherenceTransition() {
    }

    public ConfirmAdherenceTransition(WHPIVRMessage whpivrMessage,
                                      WHPAdherenceService adherenceService,
                                      TreatmentUpdateOrchestrator treatmentUpdateOrchestrator,
                                      ReportingPublisherService reportingPublisherService) {

        super(whpivrMessage, reportingPublisherService);

        this.adherenceService = adherenceService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.reportingPublisherService = reportingPublisherService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatientId();

        Node nextNode = new Node();
        if (input.equals("2")) {
            addPatientPromptsAndTransitions(nextNode, ivrSession);
        } else {
            if (input.equals("1")) {
                ivrSession.recordAdherenceForCurrentPatient();
                nextNode.addOperations(new RecordAdherenceOperation(currentPatientId, treatmentUpdateOrchestrator, reportingPublisherService));
            }
            addTransitionsToNextPatients(ivrSession, nextNode);
        }
        return nextNode.addOperations(new ResetFlowSessionOperation());
    }
}
