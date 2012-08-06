package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.session.IvrSession;
import org.springframework.beans.factory.annotation.Autowired;

import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;

public abstract class TransitionToCollectPatientAdherence implements ITransition {

    @Autowired
    protected WHPIVRMessage whpivrMessage;

    TransitionToCollectPatientAdherence() {

    }

    public TransitionToCollectPatientAdherence(WHPIVRMessage whpivrMessage) {
        this.whpivrMessage = whpivrMessage;
    }

    protected void addTransitionsToNextPatients(IvrSession ivrSession, Node nextNode) {
        if (ivrSession.hasNextPatient()) {
            ivrSession.nextPatient();
            addPatientPromptsAndTransitions(nextNode, ivrSession);
        } else {
            nextNode.addPrompts(callCompletionPromptsAfterCapturingAdherence(whpivrMessage, ivrSession.countOfAllPatients(), ivrSession.countOfCurrentPatientsWithAdherence()));
        }
    }

    protected void addPatientPromptsAndTransitions(Node node, IvrSession ivrSession) {
        node.addPrompts(captureAdherencePrompts(whpivrMessage,
                ivrSession.currentPatientId(),
                ivrSession.currentPatientNumber()));
        node.addTransition("?", new AdherenceCaptureTransition());
    }
}
