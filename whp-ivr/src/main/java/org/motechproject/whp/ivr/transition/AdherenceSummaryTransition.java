package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.CaptureAdherenceSubmissionTimeOperation;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.operation.RecordCallStartTimeOperation;
import org.motechproject.whp.ivr.session.AdherenceRecordingSession;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.adherence.criteria.UpdateAdherenceCriteria.isWindowClosedToday;
import static org.motechproject.whp.ivr.prompts.AdherenceCaptureWindowClosedPrompts.adherenceCaptureWindowClosedPrompts;
import static org.motechproject.whp.ivr.prompts.AdherenceSummaryPrompts.adherenceSummaryPrompts;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.adherenceSummaryWithCallCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;


@Component
public class AdherenceSummaryTransition implements ITransition {

    @Autowired
    private WHPIVRMessage whpivrMessage;
    @Autowired
    private AdherenceRecordingSession recordingSession;
    @Autowired
    private ReportingPublisherService reportingPublisherService;

    /*Required for platform autowiring*/
    public AdherenceSummaryTransition() {
    }

    public AdherenceSummaryTransition(WHPIVRMessage whpivrMessage, AdherenceRecordingSession recordingSession, ReportingPublisherService reportingPublisherService) {
        this.whpivrMessage = whpivrMessage;
        this.recordingSession = recordingSession;
        this.reportingPublisherService = reportingPublisherService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        Node captureAdherenceNode = new Node();
        if (isWindowClosedToday()) {
            captureAdherenceNode.addPrompts(adherenceCaptureWindowClosedPrompts(whpivrMessage));
            return captureAdherenceNode;
        }

        IvrSession ivrSession = new IvrSession(recordingSession.initialize(flowSession));
        captureAdherenceNode.addOperations(new RecordCallStartTimeOperation(now()));

        if (ivrSession.hasPatientsWithoutAdherence()) {
            captureAdherenceNode.addPrompts(adherenceSummaryPrompts(whpivrMessage, ivrSession.patientsWithAdherence(), ivrSession.patientsWithoutAdherence()));
            return addAdherenceCaptureTransitions(ivrSession, captureAdherenceNode);
        }

        return addTransitionToTerminateCall(captureAdherenceNode, ivrSession.countOfAllPatients(), ivrSession.countOfPatientsWithAdherence());
    }

    private Node addAdherenceCaptureTransitions(final IvrSession ivrSession, Node captureAdherenceNode) {
        captureAdherenceNode.addPrompts(captureAdherencePrompts(whpivrMessage, ivrSession.currentPatientId(), ivrSession.currentPatientNumber()));
        captureAdherenceNode.addOperations(new CaptureAdherenceSubmissionTimeOperation(now()));
        captureAdherenceNode.addTransition("?", new AdherenceCaptureTransition());
        return captureAdherenceNode;
    }

    private Node addTransitionToTerminateCall(Node captureAdherenceNode, Integer countOfAllPatients, Integer countOfPatientsWithAdherence) {
        captureAdherenceNode.addPrompts(adherenceSummaryWithCallCompletionPrompts(whpivrMessage, countOfAllPatients, countOfPatientsWithAdherence));
        captureAdherenceNode.addOperations(new PublishCallLogOperation(reportingPublisherService, DateUtil.now()));
        return captureAdherenceNode;
    }
}