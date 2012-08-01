package org.motechproject.whp.ivr.transition;


import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.operation.ResetPatientIndexOperation;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;

public class ConfirmAdherenceTransition implements ITransition {

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Autowired
    private PatientService patientService;
    @Autowired
    private WHPIVRMessage whpivrMessage;

    ConfirmAdherenceTransition() {
    }

    public ConfirmAdherenceTransition(WHPIVRMessage whpivrMessage, WHPAdherenceService adherenceService, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.whpivrMessage = whpivrMessage;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.patientService = patientService;
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
                Integer adherenceInput = ivrSession.adherenceInputForCurrentPatient();
                nextNode.addOperations(new RecordAdherenceOperation(adherenceInput, currentPatientId, treatmentUpdateOrchestrator));
            }
            if (ivrSession.hasNextPatient()) {
                ivrSession.nextPatient();
                addPatientPromptsAndTransitions(nextNode, ivrSession);
            } else {
                nextNode.addPrompts(callCompletionPrompts(whpivrMessage));
            }
        }

        return nextNode.addOperations(new ResetPatientIndexOperation());
    }

    private void addPatientPromptsAndTransitions(Node node, IvrSession ivrSession) {
        node.addPrompts(captureAdherencePrompts(whpivrMessage,
                ivrSession.currentPatientId(),
                ivrSession.currentPatientNumber()));
        node.addTransition("?", new AdherenceCaptureTransition());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfirmAdherenceTransition)) return false;

        ConfirmAdherenceTransition that = (ConfirmAdherenceTransition) o;

        if (adherenceService != null ? !adherenceService.equals(that.adherenceService) : that.adherenceService != null)
            return false;
        if (patientService != null ? !patientService.equals(that.patientService) : that.patientService != null)
            return false;
        if (treatmentUpdateOrchestrator != null ? !treatmentUpdateOrchestrator.equals(that.treatmentUpdateOrchestrator) : that.treatmentUpdateOrchestrator != null)
            return false;
        if (whpivrMessage != null ? !whpivrMessage.equals(that.whpivrMessage) : that.whpivrMessage != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = adherenceService != null ? adherenceService.hashCode() : 0;
        result = 31 * result + (treatmentUpdateOrchestrator != null ? treatmentUpdateOrchestrator.hashCode() : 0);
        result = 31 * result + (patientService != null ? patientService.hashCode() : 0);
        result = 31 * result + (whpivrMessage != null ? whpivrMessage.hashCode() : 0);
        return result;
    }
}
