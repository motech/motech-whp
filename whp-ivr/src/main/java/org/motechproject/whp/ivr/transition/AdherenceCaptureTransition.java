package org.motechproject.whp.ivr.transition;

import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.node.ConfirmAdherenceNode;
import org.motechproject.whp.ivr.operation.ResetPatientIndexOperation;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsWithAdherenceSummary;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;

@Component
@EqualsAndHashCode
public class AdherenceCaptureTransition implements ITransition {

    @Autowired
    private PatientService patientService;
    @Autowired
    private WHPIVRMessage whpivrMessage;
    @Autowired
    private AdherenceDataService adherenceDataService;

    AdherenceCaptureTransition() {
    }

    public AdherenceCaptureTransition(WHPIVRMessage whpivrMessage, AdherenceDataService adherenceDataService, PatientService patientService) {
        this.whpivrMessage = whpivrMessage;
        this.adherenceDataService = adherenceDataService;
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        IVRInput ivrInput = new IVRInput(input);
        Patient patient = patientService.findByPatientId(ivrSession.currentPatientId());

        Node nextNode = new Node();
        if (ivrInput.isNotSkipInput() && patient.isValidDose(ivrInput.input())) {
            nextNode = new ConfirmAdherenceNode(whpivrMessage).with(patient, parseInt(input)).node();
        } else {
            addTransitionsToNextPatient(ivrSession, nextNode);
        }
        return nextNode.addOperations(new ResetPatientIndexOperation());
    }

    private void addTransitionsToNextPatient(IvrSession ivrSession, Node nextNode) {
        if (ivrSession.hasNextPatient()) {
            ivrSession.nextPatient();
            addNextPatientPromptsAndTransition(nextNode, ivrSession);
        } else {
            AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(ivrSession.providerId());
            nextNode.addPrompts(callCompletionPromptsWithAdherenceSummary(whpivrMessage, adherenceSummary.getAllPatientsWithAdherence(), adherenceSummary.getAllPatientsWithoutAdherence()));
        }
    }

    private void addNextPatientPromptsAndTransition(Node node, IvrSession ivrSession) {
        node.addPrompts(captureAdherencePrompts(whpivrMessage,
                ivrSession.currentPatientId(),
                ivrSession.currentPatientNumber()));
        node.addTransition("?", new AdherenceCaptureTransition());
    }
}
