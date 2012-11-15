package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.ITransition;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.CaptureAdherenceSubmissionTimeOperation;
import org.motechproject.whp.ivr.operation.RecordCallStartTimeOperation;
import org.motechproject.whp.ivr.session.AdherenceRecordingSession;
import org.motechproject.whp.ivr.session.IvrSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.adherence.criteria.UpdateAdherenceCriteria.isWindowClosedToday;
import static org.motechproject.whp.ivr.prompts.AdherenceCaptureWindowClosedPrompts.adherenceCaptureWindowClosedPrompts;
import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.adherenceSummaryWithCallCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.WelcomeMessagePrompts.welcomeMessagePrompts;


@Component
public class AdherenceSummaryTransition implements ITransition {

    @Autowired
    private WhpIvrMessage whpIvrMessage;
    @Autowired
    private AdherenceRecordingSession recordingSession;

    /*Required for platform autowiring*/
    public AdherenceSummaryTransition() {
    }

    public AdherenceSummaryTransition(WhpIvrMessage whpIvrMessage, AdherenceRecordingSession recordingSession) {
        this.whpIvrMessage = whpIvrMessage;
        this.recordingSession = recordingSession;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        Node captureAdherenceNode = new Node();

        addHangupTransition(captureAdherenceNode);

        captureAdherenceNode.addPrompts(welcomeMessagePrompts(whpIvrMessage));
        IvrSession ivrSession = new IvrSession(recordingSession.initialize(flowSession));
        captureAdherenceNode.addOperations(new RecordCallStartTimeOperation(now()));

        if (isWindowClosedToday()) {
            captureAdherenceNode.addPrompts(adherenceCaptureWindowClosedPrompts(whpIvrMessage));
            ivrSession.callStatus(CallStatus.OUTSIDE_ADHERENCE_CAPTURE_WINDOW);
            return captureAdherenceNode;
        }

        if (ivrSession.hasPatientsWithoutAdherence()) {
            captureAdherenceNode.addPrompts(adherenceSummaryPrompts(whpIvrMessage, ivrSession.patientsWithAdherence(), ivrSession.patientsWithoutAdherence()));
            return addAdherenceCaptureTransitions(ivrSession, captureAdherenceNode);
        }

        return addTransitionToTerminateCall(captureAdherenceNode, ivrSession);
    }

    private void addHangupTransition(Node node) {
        node.addTransition("Hangup", new HangupTransition());
    }

    private Node addAdherenceCaptureTransitions(final IvrSession ivrSession, Node captureAdherenceNode) {
        captureAdherenceNode.addPrompts(captureAdherencePrompts(whpIvrMessage, ivrSession));
        captureAdherenceNode.addOperations(new CaptureAdherenceSubmissionTimeOperation(now()));
        captureAdherenceNode.setMaxTransitionInputDigit(1);
        captureAdherenceNode.addTransition("?", new AdherenceCaptureTransition());
        return captureAdherenceNode;
    }

    private Node addTransitionToTerminateCall(Node captureAdherenceNode, IvrSession ivrSession) {
        ivrSession.callStatus(CallStatus.ADHERENCE_ALREADY_PROVIDED);
        captureAdherenceNode.addPrompts(adherenceSummaryWithCallCompletionPrompts(whpIvrMessage, ivrSession.countOfAllPatients(), ivrSession.countOfPatientsWithAdherence()));
        return captureAdherenceNode;
    }
}