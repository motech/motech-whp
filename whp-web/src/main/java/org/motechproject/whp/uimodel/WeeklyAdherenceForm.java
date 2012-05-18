package org.motechproject.whp.uimodel;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.adherence.domain.PillStatus.NotTaken;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;
import static org.motechproject.whp.criteria.UpdateAdherenceCriteria.canUpdate;
import static org.motechproject.whp.uimodel.PillDays.takenDays;

@Data
public class WeeklyAdherenceForm {

    LocalDate referenceDate;
    private String patientId;
    private String treatmentId;

    private int numberOfDosesTaken;
    private int totalDoses;

    private Patient patient;
    private Set<String> pauseReasons = new LinkedHashSet<String>();

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(WeeklyAdherence weeklyAdherence, Patient patient, int totalNumberOfDoses) {
        referenceDate = weeklyAdherence.getWeek().getReference();
        patientId = weeklyAdherence.getPatientId();
        treatmentId = weeklyAdherence.getTreatmentId();

        totalDoses = totalNumberOfDoses;
        this.patient = patient;


        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            if (Taken.equals(adherence.getPillStatus())) {
                numberOfDosesTaken++;
            }
            populatePauseReason(adherence);
        }
    }

    public WeeklyAdherence weeklyAdherence(TreatmentCategory treatmentCategory) {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patientId, treatmentId, new TreatmentWeek(referenceDate));
        List<DayOfWeek> takenDays = takenDays(treatmentCategory, numberOfDosesTaken);
        for (DayOfWeek pillDay : treatmentCategory.getPillDays()) {
            PillStatus pillStatus = (takenDays.contains(pillDay)) ? Taken : NotTaken;
            weeklyAdherence.addAdherenceLog(pillDay, pillStatus, null);
        }
        return weeklyAdherence;
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

    public boolean isAdherenceUpdatable() {
        return canUpdate(patient);
    }
}
