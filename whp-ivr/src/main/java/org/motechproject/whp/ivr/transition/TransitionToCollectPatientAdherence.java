package org.motechproject.whp.ivr.transition;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.MenuRepeatFailurePrompts.noValidInputMovingOn;

public abstract class TransitionToCollectPatientAdherence implements ITransition {

    protected enum InputType {INVALID_INPUT, NO_INPUT}

    @Autowired
    protected WhpIvrMessage whpIvrMessage;
    @Autowired
    protected ReportingPublisherService reportingPublisherService;
    @Value("${ivr.menuRepeat.count.invalidInput}")
    private String invalidInputThreshold;

    @Value("${ivr.menuRepeat.count.noInput}")
    private String noInputThreshold;

    public TransitionToCollectPatientAdherence() {
    }

    public TransitionToCollectPatientAdherence(WhpIvrMessage whpIvrMessage, ReportingPublisherService reportingPublisherService, String invalidInputThreshold, String noInputThreshold) {
        this.whpIvrMessage = whpIvrMessage;
        this.reportingPublisherService = reportingPublisherService;
        this.invalidInputThreshold = invalidInputThreshold;
        this.noInputThreshold = noInputThreshold;
    }

    protected boolean canRetry(IvrSession ivrSession, InputType type) {
        int retryCount = getCurrentRetryCount(ivrSession, type);
        int retryThreshold = getRetryThreshold(type);
        return retryCount < retryThreshold;
    }

    protected void moveToNextPatient(IvrSession ivrSession, Node nextNode, boolean isThresholdRollover) {
        resetRetryCounts(ivrSession);
        if(isThresholdRollover) {
            addThresholdRolloverPromptsForCurrentPatient(nextNode);
        }
        addTransitionsAndPromptsForNextPatient(ivrSession, nextNode);
    }

    protected void addTransitionsAndPromptsForNextPatient(IvrSession ivrSession, Node nextNode) {
        if (ivrSession.hasNextPatient()) {
            ivrSession.nextPatient();
            addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
        } else {
            nextNode.addPrompts(callCompletionPromptsAfterCapturingAdherence(whpIvrMessage, ivrSession.countOfAllPatients(), ivrSession.countOfCurrentPatientsWithAdherence()));
            nextNode.addOperations(new PublishCallLogOperation(reportingPublisherService, CallStatus.VALID_ADHERENCE_CAPTURE, DateUtil.now()));
        }
    }

    protected void addThresholdRolloverPromptsForCurrentPatient(Node nextNode) {
        nextNode.addPrompts(noValidInputMovingOn(whpIvrMessage));
    }

    protected void addTransitionsAndPromptsForCurrentPatient(Node node, IvrSession ivrSession) {
        node.addPrompts(captureAdherencePrompts(whpIvrMessage,
                ivrSession.currentPatientId(),
                ivrSession.currentPatientNumber()));
        node.addTransition("?", new AdherenceCaptureTransition());
    }

    protected void resetRetryCounts(IvrSession ivrSession) {
        setCurrentRetryCount(ivrSession, 0, InputType.INVALID_INPUT);
        setCurrentRetryCount(ivrSession, 0, InputType.NO_INPUT);
    }

    protected void setCurrentRetryCount(IvrSession ivrSession, int retryCount, InputType type) {
        if (type == InputType.INVALID_INPUT) {
            ivrSession.currentInvalidInputRetryCount(retryCount);
        } else {
            ivrSession.currentNoInputRetryCount(retryCount);
        }
    }

    protected int getRetryThreshold(InputType type) {
        if (type == InputType.INVALID_INPUT) {
            return Integer.valueOf(invalidInputThreshold);
        } else {
            return Integer.valueOf(noInputThreshold);
        }
    }

    protected int getCurrentRetryCount(IvrSession ivrSession, InputType type) {
        if (type == InputType.INVALID_INPUT) {
            return ivrSession.currentInvalidInputRetryCount();
        } else {
            return ivrSession.currentNoInputRetryCount();
        }
    }
}
