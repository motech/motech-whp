package org.motechproject.whp.ivr.transition;

import org.apache.commons.lang.StringUtils;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.SavedAdherencePrompts.savedAdherencePrompts;

@Component
public class AdherenceCaptureTransition implements ITransition {

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;
    @Autowired
    private PatientService patientService;
    @Autowired
    private WHPIVRMessage whpivrMessage;

    private static final String SKIP_PATIENT_CODE = "9";

    AdherenceCaptureTransition() {
    }

    public AdherenceCaptureTransition(WHPIVRMessage whpivrMessage, WHPAdherenceService adherenceService, PhaseUpdateOrchestrator phaseUpdateOrchestrator, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.whpivrMessage = whpivrMessage;
        this.phaseUpdateOrchestrator = phaseUpdateOrchestrator;
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatientId();

        Node nextNode = new Node();
        if (!shouldSkipInput(input)) {
            captureAdherenceForCurrentPatient(nextNode, parseInt(input), currentPatientId);
        }

        if (ivrSession.hasNextPatient()) {
            ivrSession.nextPatient();
            addNextPatientPromptsAndTransitions(nextNode, ivrSession);
        } else {
            nextNode.addPrompts(callCompletionPrompts(whpivrMessage));
        }

        return nextNode;
    }

    private void captureAdherenceForCurrentPatient(Node node, Integer adherenceInput, String currentPatientId) {
        Patient patient = patientService.findByPatientId(currentPatientId);
        if (patient.isValidDose(adherenceInput)) {
            node.addPrompts(savedAdherencePrompts(whpivrMessage, currentPatientId, adherenceInput, patient.dosesPerWeek()));
            node.addOperations(new RecordAdherenceOperation(adherenceService, patientService, phaseUpdateOrchestrator, currentPatientId));
        }
    }

    private void addNextPatientPromptsAndTransitions(Node node, IvrSession ivrSession) {
        node.addPrompts(captureAdherencePrompts(whpivrMessage,
                ivrSession.currentPatientId(),
                ivrSession.currentPatientNumber()));
        node.addTransition("?", new AdherenceCaptureTransition());
    }


    private boolean shouldSkipInput(String input) {
        return !StringUtils.isNumeric(input) || input.equals(SKIP_PATIENT_CODE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdherenceCaptureTransition)) return false;

        AdherenceCaptureTransition that = (AdherenceCaptureTransition) o;

        if (adherenceService != null ? !adherenceService.equals(that.adherenceService) : that.adherenceService != null)
            return false;
        if (patientService != null ? !patientService.equals(that.patientService) : that.patientService != null)
            return false;
        if (whpivrMessage != null ? !whpivrMessage.equals(that.whpivrMessage) : that.whpivrMessage != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = patientService != null ? patientService.hashCode() : 0;
        result = 31 * result + (adherenceService != null ? adherenceService.hashCode() : 0);
        result = 31 * result + (whpivrMessage != null ? whpivrMessage.hashCode() : 0);
        return result;
    }
}
