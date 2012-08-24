package org.motechproject.whp.ivr.transition;

import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.operation.InvalidAdherenceOperation;
import org.motechproject.whp.ivr.operation.NoInputAdherenceOperation;
import org.motechproject.whp.ivr.operation.SkipAdherenceOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts.confirmAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.InvalidAdherencePrompts.invalidAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;

@Component
@EqualsAndHashCode
public class AdherenceCaptureTransition extends TransitionToCollectPatientAdherence {

    @Autowired
    private PatientService patientService;

    public AdherenceCaptureTransition() {
    }

    public AdherenceCaptureTransition(WhpIvrMessage whpIvrMessage,
                                      PatientService patientService,
                                      ReportingPublisherService reportingPublisherService,
                                      @Qualifier("ivrProperties") Properties ivrProperties) {
        super(whpIvrMessage, reportingPublisherService, ivrProperties);
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        IVRInput ivrInput = new IVRInput(input);
        Patient patient = patientService.findByPatientId(ivrSession.currentPatientId());

        Node nextNode = new Node();
        if (ivrInput.noInput()) {
            handleNoInput(ivrSession, nextNode);
        } else if (ivrInput.isSkipInput()) {
            moveToNextPatient(ivrSession, nextNode, false);
        } else if (ivrInput.isNumeric() && patient.isValidDose(parseInt(ivrInput.input()))) {
            handleValidInput(input, patient, nextNode, ivrSession);
        } else {
            handleInvalidInput(ivrSession, patient, nextNode);
        }
        return nextNode;
    }

    private void handleValidInput(String input, Patient patient, Node nextNode, IvrSession ivrSession) {
        resetRetryCounts(ivrSession);
        nextNode.addOperations(new GetAdherenceOperation());
        nextNode.addPrompts(providedAdherencePrompts(whpIvrMessage, patient.getPatientId(), parseInt(input), patient.dosesPerWeek()));
        nextNode.addPrompts(confirmAdherencePrompts(whpIvrMessage));
        nextNode.addTransition("?", new ConfirmAdherenceTransition());
    }

    private void handleInvalidInput(IvrSession ivrSession, Patient patient, Node nextNode) {
        boolean isVeryFirstInvalidInput = ivrSession.firstInvalidInput();
        if (isVeryFirstInvalidInput) {
            ivrSession.firstInvalidInput(false);
            resetRetryCounts(ivrSession);
        }
        InputType type = InputType.INVALID_INPUT;
        int retryCount = getCurrentRetryCount(ivrSession, type);
        int retryThreshold = getRetryThreshold(type);
        if (retryCount < retryThreshold) {
            retryCount = isVeryFirstInvalidInput ? retryCount : ++retryCount;
            setCurrentRetryCount(ivrSession, retryCount, type);
            nextNode.addPrompts(invalidAdherencePrompts(whpIvrMessage, patient.getCurrentTherapy().getTreatmentCategory()));
            nextNode.addOperations(new InvalidAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
            addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
        } else {
            moveToNextPatient(ivrSession, nextNode, true);
        }
    }

    private void handleNoInput(IvrSession ivrSession, Node nextNode) {
        InputType type = InputType.NO_INPUT;
        int retryCount = getCurrentRetryCount(ivrSession, type);
        int retryThreshold = getRetryThreshold(type);
        if (retryCount < retryThreshold) {
            setCurrentRetryCount(ivrSession,++retryCount,type);
            addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
            nextNode.addOperations(new NoInputAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
        } else {
            moveToNextPatient(ivrSession, nextNode, true);
        }
    }

    private void moveToNextPatient(IvrSession ivrSession, Node nextNode, boolean isThresholdRollover) {
        resetRetryCounts(ivrSession);
        resetInvalidInputState(ivrSession);
        nextNode.addOperations(new SkipAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
        if(isThresholdRollover) {
            addThresholdRolloverPromptsForCurrentPatient(nextNode);
        }
        addTransitionsAndPromptsForNextPatient(ivrSession, nextNode);
    }

    private void resetInvalidInputState(IvrSession ivrSession) {
        ivrSession.firstInvalidInput(true);
    }
}
