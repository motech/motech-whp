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
import org.motechproject.whp.patient.domain.TreatmentInterruption;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;
import static org.motechproject.whp.adherence.domain.PillStatus.NotTaken;
import static org.motechproject.whp.adherence.domain.PillStatus.Taken;
import static org.motechproject.whp.criteria.UpdateAdherenceCriteria.canUpdate;
import static org.motechproject.whp.uimodel.PillDays.takenDays;
import static org.motechproject.whp.patient.util.WHPDateUtil.getDatesInRange;

@Data
public class WeeklyAdherenceForm {

    LocalDate referenceDate;
    private String patientId;
    private String treatmentId;
    private int numberOfDosesTaken;

    private Patient patient;
    private Set<String> pauseReasons = new LinkedHashSet<String>();
    private String latestPauseDate;
    private String latestResumptionDate;

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(WeeklyAdherence weeklyAdherence, Patient patient) {
        referenceDate = weeklyAdherence.getWeek().getReference();
        patientId = weeklyAdherence.getPatientId();
        treatmentId = weeklyAdherence.getTreatmentId();

        this.patient = patient;
        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            if (Taken.equals(adherence.getPillStatus())) {
                numberOfDosesTaken++;
            }
        }
        populatePauseRestartData(currentWeekInstance().startDate());
    }

    public WeeklyAdherence weeklyAdherence(TreatmentCategory treatmentCategory) {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patientId, treatmentId, new TreatmentWeek(referenceDate));
        List<DayOfWeek> takenDays = takenDays(treatmentCategory, numberOfDosesTaken);
        for (DayOfWeek pillDay : treatmentCategory.getPillDays()) {
            PillStatus pillStatus = (takenDays.contains(pillDay)) ? Taken : NotTaken;
            weeklyAdherence.addAdherenceLog(pillDay, pillStatus);
        }
        return weeklyAdherence;
    }

    private void populatePauseRestartData(LocalDate weekStartDate) {
        List<LocalDate> datesInRange = getDatesInRange(weekStartDate, today());
        List<String> pauseReasons = patient.getTreatmentInterruptions().getPauseReasons(datesInRange);
        if (!pauseReasons.isEmpty()) {
            this.pauseReasons.addAll(pauseReasons);
        }
        TreatmentInterruption latestInterruption = patient.getCurrentProvidedTreatment().getInterruptions().latestInterruption();
        if (latestInterruption != null && latestInterruption.isTreatmentInterrupted(datesInRange)){
            latestPauseDate = latestInterruption.getPauseDate().toString("dd-MM-YYYY");
            latestResumptionDate = latestInterruption.isCurrentlyPaused() ? null : latestInterruption.getResumptionDate().toString("dd-MM-YYYY");
        }
    }

    boolean isTreatmentCurrentlyPaused() {
        return !isEmpty(latestPauseDate) && isEmpty(latestResumptionDate);
    }

    boolean wasTreatmentInPauseStateLastWeekAndIsNowRestarted() {
        return !isEmpty(latestPauseDate) && !isEmpty(latestResumptionDate);
    }

    String getTreatmentPauseReasons() {
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

        if (isTreatmentCurrentlyPaused()) {
            warningMessages.add(String.format("Patient has been paused on medication since %s. Please contact CMF Admin for further details. Reasons for pause: %s",
                    latestPauseDate, getTreatmentPauseReasons()));
        }else if (wasTreatmentInPauseStateLastWeekAndIsNowRestarted()) {
            warningMessages.add(String.format("This patient has been restarted on medicines on %s after being paused on %s. Reasons for pause: %s",
                    latestResumptionDate, latestPauseDate, getTreatmentPauseReasons()));
        }

        if (!isAdherenceUpdatable())
            warningMessages.add("Please contact the CMF admin to update adherence.");
        return StringUtils.join(warningMessages.toArray(), "<br/>");
    }

    public boolean isAdherenceUpdatable() {
        return canUpdate(patient);
    }

}
