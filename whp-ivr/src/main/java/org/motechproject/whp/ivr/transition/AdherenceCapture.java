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
import org.motechproject.whp.ivr.builder.CaptureAdherenceNodeBuilder;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;
import static org.motechproject.whp.ivr.transition.ListPatientsForProvider.CURRENT_PATIENT_POSITION;

@Component
public class AdherenceCapture implements ITransition {

    @Autowired
    private PatientService patientService;

    @Autowired
    private WHPAdherenceService adherenceService;

    @Autowired
    private WHPIVRMessage whpivrMessage;

    private static String SKIP_PATIENT_CODE = "9";


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
        CaptureAdherenceNodeBuilder captureAdherenceNodeBuilder = new CaptureAdherenceNodeBuilder(whpivrMessage);
        String currentPatientId = patients.get(0).toString();

        if (!shouldSkipInput(input))
            recordAdherenceBasedOnInput(captureAdherenceNodeBuilder, parseInt(input), currentPatientId);

        removeCurrentPatientFromSession(flowSession, patients);
        return getNextNode(captureAdherenceNodeBuilder, patients, flowSession);
    }

    private void recordAdherenceBasedOnInput(CaptureAdherenceNodeBuilder captureAdherenceNodeBuilder, Integer adherenceInput, String currentPatientId) {
        Patient patient = patientService.findByPatientId(currentPatientId);
        int dosesPerWeek = patient.getCurrentTherapy().getTreatmentCategory().getDosesPerWeek();

        if (adherenceInput <= dosesPerWeek) {
            AuditParams auditParams = new AuditParams(patient.getCurrentTreatment().getProviderId(), AdherenceSource.IVR, "");
            adherenceService.recordAdherence(new WeeklyAdherenceSummary(currentPatientId, currentWeekInstance()), auditParams);
            captureAdherenceNodeBuilder.confirmAdherence(currentPatientId, adherenceInput, dosesPerWeek);
        }
    }

    private Node getNextNode(CaptureAdherenceNodeBuilder captureAdherenceNodeBuilder, SerializableList patients, FlowSession flowSession) {
        if (patients.size() > 0) {
            Integer nextPatientPosition = ((Integer) flowSession.get(CURRENT_PATIENT_POSITION)) + 1;
            String nextPatientId = patients.get(0).toString();

            return captureAdherenceNodeBuilder.captureAdherence(nextPatientId, nextPatientPosition).build();
        }
        return captureAdherenceNodeBuilder.build();
    }

    private void removeCurrentPatientFromSession(FlowSession flowSession, SerializableList patients) {
        patients.remove(0);
        flowSession.set(ListPatientsForProvider.PATIENTS_WITHOUT_ADHERENCE, patients);
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
