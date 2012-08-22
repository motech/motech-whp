package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Properties;

import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPromptsAfterCapturingAdherence;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;

public abstract class TransitionToCollectPatientAdherence implements ITransition {

    public static final String INVALID_INPUT_THRESHOLD_KEY = "ivr.menuRepeat.count.invalidInput";
    public static final String NO_INPUT_THRESHOLD_KEY = "ivr.menuRepeat.count.noInput";

    protected enum InputType {INVALID_INPUT, NO_INPUT;}
    @Autowired
    protected WhpIvrMessage whpIvrMessage;
    @Autowired
    protected ReportingPublisherService reportingPublisherService;
    @Autowired
    private Properties ivrProperties;

    public TransitionToCollectPatientAdherence() {
    }

    public TransitionToCollectPatientAdherence(WhpIvrMessage whpIvrMessage, ReportingPublisherService reportingPublisherService, @Qualifier("ivrProperties") Properties properties) {
        this.whpIvrMessage = whpIvrMessage;
        this.reportingPublisherService = reportingPublisherService;
        this.ivrProperties = properties;
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
            return Integer.valueOf(ivrProperties.getProperty(INVALID_INPUT_THRESHOLD_KEY));
        } else {
            return Integer.valueOf(ivrProperties.getProperty(NO_INPUT_THRESHOLD_KEY));
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
