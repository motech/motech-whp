package org.motechproject.whp.ivr.transition;


import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Properties;

import static org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts.confirmAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;

@EqualsAndHashCode
public class ConfirmAdherenceTransition extends TransitionToCollectPatientAdherence {

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Autowired
    private PatientService patientService;

    public ConfirmAdherenceTransition() {
    }

    public ConfirmAdherenceTransition(WhpIvrMessage whpIvrMessage,
                                      WHPAdherenceService adherenceService,
                                      TreatmentUpdateOrchestrator treatmentUpdateOrchestrator,
                                      ReportingPublisherService reportingPublisherService,
                                      PatientService patientService,
                                      @Qualifier("ivrProperties") Properties properties) {

        super(whpIvrMessage, reportingPublisherService, properties);

        this.adherenceService = adherenceService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.patientService = patientService;
        this.reportingPublisherService = reportingPublisherService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatientId();

        Node nextNode = new Node();
        IVRInput ivrInput = new IVRInput(input);
        if (ivrInput.noInput()) {
            handleImproperInput(ivrSession, currentPatientId, nextNode, InputType.NO_INPUT);
        } else {
            switch (input) {
                case "1":
                    resetRetryCounts(ivrSession);
                    ivrSession.recordAdherenceForCurrentPatient();
                    moveToNextPatient(ivrSession, currentPatientId, nextNode);
                    break;
                case "2":
                    resetRetryCounts(ivrSession);
                    addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
                    break;
                default:
                    handleImproperInput(ivrSession, currentPatientId, nextNode, InputType.INVALID_INPUT);
            }
        }
        return nextNode;
    }

    private void handleImproperInput(IvrSession ivrSession, String currentPatientId, Node nextNode, InputType type) {
        int retryCount = getCurrentRetryCount(ivrSession, type);
        int retryThreshold = getRetryThreshold(type);
        if (retryCount < retryThreshold) {
            setCurrentRetryCount(ivrSession, retryCount++, type);
            repeatCurrentPatient(ivrSession, currentPatientId, nextNode);
        } else {
            moveToNextPatient(ivrSession, currentPatientId, nextNode);
        }
    }

    private void moveToNextPatient(IvrSession ivrSession, String currentPatientId, Node nextNode) {
        resetRetryCounts(ivrSession);
        nextNode.addOperations(new RecordAdherenceOperation(currentPatientId, treatmentUpdateOrchestrator, reportingPublisherService));
        addTransitionsAndPromptsForNextPatient(ivrSession, nextNode);
    }

    private void repeatCurrentPatient(IvrSession ivrSession, String currentPatientId, Node nextNode) {
        Patient patient = patientService.findByPatientId(currentPatientId);
        Integer adherenceInput = Integer.parseInt(ivrSession.adherenceInputForCurrentPatient().input());
        nextNode.addPrompts(providedAdherencePrompts(whpIvrMessage, currentPatientId, adherenceInput, patient.dosesPerWeek()));
        nextNode.addPrompts(confirmAdherencePrompts(whpIvrMessage));
        nextNode.addTransition("?", new ConfirmAdherenceTransition());
    }
}
