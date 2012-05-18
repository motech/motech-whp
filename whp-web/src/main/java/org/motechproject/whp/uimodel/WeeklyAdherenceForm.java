package org.motechproject.whp.uimodel;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.criteria.UpdateAdherenceCriteria.canUpdate;

@Data
public class WeeklyAdherenceForm {

    LocalDate referenceDate;

    private Set<String> pauseReasons = new LinkedHashSet<String>();
    private String patientId;
    private String treatmentId;
    private Patient patient;

    List<AdherenceForm> adherenceList = new ArrayList<AdherenceForm>();

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(WeeklyAdherence weeklyAdherence, Patient patient) {
        this.referenceDate = weeklyAdherence.getWeek().getReference();
        this.patientId = weeklyAdherence.getPatientId();
        this.treatmentId = weeklyAdherence.getTreatmentId();
        this.patient = patient;

        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            AdherenceForm adherenceForm = new AdherenceForm(adherence.getPillDay(), adherence.getPillDate(), adherence.getPillStatus(), adherence.getMeta());
            this.adherenceList.add(adherenceForm);
            populatePauseReason(adherence);
        }
    }

    public WeeklyAdherence updatedWeeklyAdherence() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patientId, treatmentId, new TreatmentWeek(referenceDate));
        for (AdherenceForm form : adherenceList) {
            if(form.updated()) weeklyAdherence.addAdherenceLog(form.getPillDay(), form.getPillStatus(), form.getMeta());
        }
        return weeklyAdherence;
    }

    public WeeklyAdherence weeklyAdherence() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patientId, treatmentId, new TreatmentWeek(referenceDate));
        for (AdherenceForm form : adherenceList) {
            weeklyAdherence.addAdherenceLog(form.getPillDay(), form.getPillStatus(), form.getMeta());
        }
        return weeklyAdherence;
    }

    public String getReferenceDateString() {
        return referenceDate.toString("dd-MM-YYYY");
    }

    public void setReferenceDateString(String pillDate) {
        this.referenceDate = LocalDate.parse(pillDate, forPattern("dd-MM-YYYY"));
    }

    public String getWarningMessage() {
        List<String> warningMessages = new ArrayList<String>();
        if (isTreatmentPaused())
            warningMessages.add(String.format("The patient's treatment has been paused for one or more days in the last week. Reason: %s", getTreatmentPauseReason()));
        if (!isAdherenceUpdatable())
            warningMessages.add("Please contact the CMF admin to update adherence.");
        return StringUtils.join(warningMessages.toArray(), "<br/>");
    }

    private void populatePauseReason(Adherence adherence) {
        String pauseReason = patient.getTreatmentInterruptions().getPauseReason(adherence.getPillDate());
        if (pauseReason != null) {
            pauseReasons.add(pauseReason);
        }
    }

    boolean isTreatmentPaused() {
        return !pauseReasons.isEmpty();
    }

    String getTreatmentPauseReason() {
        return StringUtils.join(pauseReasons.toArray(), ", ");
    }

    public boolean isAdherenceUpdatable() {
        return canUpdate(patient);
    }
}
