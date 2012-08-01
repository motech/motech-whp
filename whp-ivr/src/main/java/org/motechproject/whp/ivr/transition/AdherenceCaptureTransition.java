package org.motechproject.whp.ivr.transition;

import org.apache.commons.lang.StringUtils;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.operation.ResetPatientIndexOperation;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;

@Component
public class AdherenceCaptureTransition implements ITransition {

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Autowired
    private PatientService patientService;
    @Autowired
    private WHPIVRMessage whpivrMessage;
    @Autowired
    private ReportingPublisherService reportingService;

    private static final String SKIP_PATIENT_CODE = "9";

    AdherenceCaptureTransition() {
    }

    public AdherenceCaptureTransition(WHPIVRMessage whpivrMessage, WHPAdherenceService adherenceService, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.whpivrMessage = whpivrMessage;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatientId();

        Node nextNode = new Node();
        boolean isValidInput = false;
        if (!shouldSkipInput(input)) {
            isValidInput = confirmAdherenceForCurrentPatient(nextNode, parseInt(input), currentPatientId);
        }
        if (isValidInput) {
            nextNode.addTransition("?", new ConfirmAdherenceTransition());
        } else {
            if (ivrSession.hasNextPatient()) {
                ivrSession.nextPatient();
                addNextPatientPromptsAndTransition(nextNode, ivrSession);
            } else {
                nextNode.addPrompts(callCompletionPrompts(whpivrMessage));
            }
        }

        return nextNode.addOperations(new ResetPatientIndexOperation());
    }

    private boolean confirmAdherenceForCurrentPatient(Node node, Integer adherenceInput, String currentPatientId) {
        Patient patient = patientService.findByPatientId(currentPatientId);
        if (patient.isValidDose(adherenceInput)) {
            node.addPrompts(providedAdherencePrompts(whpivrMessage, currentPatientId, adherenceInput, patient.dosesPerWeek()));
            node.addOperations(new GetAdherenceOperation());
            return true;
        }
        return false;
    }

    private void addNextPatientPromptsAndTransition(Node node, IvrSession ivrSession) {
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
