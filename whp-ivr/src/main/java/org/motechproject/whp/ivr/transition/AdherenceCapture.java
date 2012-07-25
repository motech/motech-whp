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
import org.motechproject.whp.ivr.builder.CaptureAdherencePrompts;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;
import static org.motechproject.whp.ivr.builder.ConfirmAdherencePrompts.confirmAdherence;

@Component
public class AdherenceCapture implements ITransition {

    @Autowired
    private PatientService patientService;

    @Autowired
    private WHPAdherenceService adherenceService;

    @Autowired
    private WHPIVRMessage whpivrMessage;

    private static String SKIP_PATIENT_CODE = "9";

    private static final String CURRENT_PATIENT_POSITION = "currentPatientPosition";


    public AdherenceCapture() {
    }

    public AdherenceCapture(WHPAdherenceService adherenceService, WHPIVRMessage whpivrMessage, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.whpivrMessage = whpivrMessage;
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        SerializableList patients = flowSession.get(ListPatientsForProvider.PATIENTS_WITHOUT_ADHERENCE);
        String currentPatientId = patients.get(getCurrentPosition(flowSession)).toString();

        Node nextNode = new Node();
        if (!shouldSkipInput(input)) {
            handleAdherenceCaptureForCurrentPatient(nextNode, parseInt(input), currentPatientId);
        }

        handleAdherenceCaptureForNextPatient(nextNode, patients, flowSession);
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

    private void handleAdherenceCaptureForNextPatient(Node node, SerializableList patients, FlowSession flowSession) {
        Integer currentNodePosition = getCurrentPosition(flowSession);
        Integer nextPatientPosition = currentNodePosition + 1;

        if (patients.size() > nextPatientPosition) {
            setPosition(flowSession, nextPatientPosition);
            String nextPatientId = patients.get(nextPatientPosition).toString();
            node.addPrompts(CaptureAdherencePrompts.captureAdherencePrompts(whpivrMessage, nextPatientId, nextPatientPosition + 1));
            node.addTransition("?", new AdherenceCapture());
        }
    }

    private Integer getCurrentPosition(FlowSession flowSession) {
        Integer currentNodePosition = (Integer) flowSession.get(CURRENT_PATIENT_POSITION);
        if (currentNodePosition == null) {
            currentNodePosition = 0;
            flowSession.set(CURRENT_PATIENT_POSITION, currentNodePosition);
        }
        return currentNodePosition;
    }


    private void setPosition(FlowSession flowSession, int value) {
        flowSession.set(CURRENT_PATIENT_POSITION, value);
    }

    private boolean shouldSkipInput(String input) {
        return !StringUtils.isNumeric(input) || input.equals(SKIP_PATIENT_CODE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdherenceCapture)) return false;

        AdherenceCapture that = (AdherenceCapture) o;

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
