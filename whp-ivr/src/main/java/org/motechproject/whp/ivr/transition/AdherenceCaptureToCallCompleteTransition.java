package org.motechproject.whp.ivr.transition;

import org.apache.commons.lang.StringUtils;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;
import static org.motechproject.whp.ivr.prompts.CallCompletionPrompts.callCompletionPrompts;
import static org.motechproject.whp.ivr.prompts.CaptureAdherencePrompts.captureAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts.confirmAdherence;

@Component
public class AdherenceCaptureToCallCompleteTransition implements ITransition {

    @Autowired
    private PatientService patientService;
    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private WHPIVRMessage whpivrMessage;

    private static final String SKIP_PATIENT_CODE = "9";

    AdherenceCaptureToCallCompleteTransition() {
    }

    public AdherenceCaptureToCallCompleteTransition(WHPAdherenceService adherenceService, WHPIVRMessage whpivrMessage, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.whpivrMessage = whpivrMessage;
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatient();

        Node nextNode = new Node();
        if (!shouldSkipInput(input)) {
            handleAdherenceCaptureForCurrentPatient(nextNode, parseInt(input), currentPatientId);
        }

        handleAdherenceCaptureForNextPatient(nextNode, ivrSession);
        return nextNode;
    }

    private void handleAdherenceCaptureForCurrentPatient(Node node, Integer adherenceInput, String currentPatientId) {
        Patient patient = patientService.findByPatientId(currentPatientId);
        int dosesPerWeek = patient.getCurrentTherapy().getTreatmentCategory().getDosesPerWeek();

        if (adherenceInput <= dosesPerWeek) {
            recordAdherence(adherenceInput, currentPatientId, patient);
            node.addPrompts(confirmAdherence(whpivrMessage, currentPatientId, adherenceInput, dosesPerWeek));
        }
    }

    private void recordAdherence(Integer adherenceInput, String currentPatientId, Patient patient) {
        AuditParams auditParams = new AuditParams(patient.getCurrentTreatment().getProviderId(), AdherenceSource.IVR, "");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(currentPatientId, currentWeekInstance());
        weeklyAdherenceSummary.setDosesTaken(adherenceInput);
        adherenceService.recordAdherence(weeklyAdherenceSummary, auditParams);
    }

    private void handleAdherenceCaptureForNextPatient(Node node, IvrSession ivrSession) {
        if(ivrSession.hasNextPatient()){
            ivrSession.nextPatient();
            node.addPrompts(captureAdherencePrompts(whpivrMessage,
                    ivrSession.currentPatient(),
                    ivrSession.currentPatientNumber()));
            node.addTransition("?", new AdherenceCaptureToCallCompleteTransition());
        } else {
            node.addPrompts(callCompletionPrompts(whpivrMessage));
        }
    }


    private boolean shouldSkipInput(String input) {
        return !StringUtils.isNumeric(input) || input.equals(SKIP_PATIENT_CODE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdherenceCaptureToCallCompleteTransition)) return false;

        AdherenceCaptureToCallCompleteTransition that = (AdherenceCaptureToCallCompleteTransition) o;

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
