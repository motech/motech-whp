package org.motechproject.whp.ivr.transition;

import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.Node;
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
import org.springframework.stereotype.Component;

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
                                      String invalidInputThreshold,
                                      String noInputThreshold) {
        super(whpIvrMessage, reportingPublisherService, invalidInputThreshold, noInputThreshold);
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
        nextNode.setMaxTransitionInputDigit(1);
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
        nextNode.addOperations(new InvalidAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
        if (canRetry(ivrSession, type)) {
            retryCount = isVeryFirstInvalidInput ? retryCount : ++retryCount;
            setCurrentRetryCount(ivrSession, retryCount, type);
            nextNode.addPrompts(invalidAdherencePrompts(whpIvrMessage, patient.getCurrentTherapy().getTreatmentCategory()));
            addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
        } else {
            moveToNextPatient(ivrSession, nextNode, true);
        }
    }

    private void handleNoInput(IvrSession ivrSession, Node nextNode) {
        InputType type = InputType.NO_INPUT;
        int retryCount = getCurrentRetryCount(ivrSession, type);
        nextNode.addOperations(new NoInputAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
        if (canRetry(ivrSession, type)) {
            setCurrentRetryCount(ivrSession,++retryCount,type);
            addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
        } else {
            moveToNextPatient(ivrSession, nextNode, true);
        }
    }

    protected void moveToNextPatient(IvrSession ivrSession, Node nextNode, boolean isThresholdRollover) {
        ivrSession.firstInvalidInput(true);
        if(!isThresholdRollover) {
            nextNode.addOperations(new SkipAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
        }
        super.moveToNextPatient(ivrSession, nextNode, isThresholdRollover);
    }
}
