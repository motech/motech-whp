package org.motechproject.whp.uimodel;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.joda.time.format.DateTimeFormat.forPattern;

@Data
public class WeeklyAdherenceForm {

    LocalDate referenceDate;

    private Set<String> pauseReasons = new LinkedHashSet<String>();
    private String patientId;
    private String treatmentId;

    List<AdherenceForm> adherenceList = new ArrayList<AdherenceForm>();

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(WeeklyAdherence weeklyAdherence, TreatmentInterruptions treatmentInterruptions) {
        referenceDate = weeklyAdherence.getWeek().getReference();
        patientId = weeklyAdherence.getPatientId();
        treatmentId = weeklyAdherence.getTreatmentId();

        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            AdherenceForm adherenceForm = new AdherenceForm(adherence.getPillDay(), adherence.getPillDate(), adherence.getPillStatus(), adherence.getMeta());
            adherenceList.add(adherenceForm);
            populatePauseReason(adherence, treatmentInterruptions);
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

    public boolean isTreatmentPaused() {
        return !pauseReasons.isEmpty();
    }

    public String getTreatmentPauseReason() {
        return StringUtils.join(pauseReasons.toArray(), ", ");
    }

    private void populatePauseReason(Adherence adherence, TreatmentInterruptions treatmentInterruptions) {
        String pauseReason = treatmentInterruptions.getPauseReason(adherence.getPillDate());
        if (pauseReason != null) {
            pauseReasons.add(pauseReason);
        }
    }
}
