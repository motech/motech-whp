package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherence {

    @Getter
    private String patientId;
    @Getter
    private String treatmentId;
    @Getter
    private TreatmentWeek week;

    private Set<Adherence> adherenceList = new LinkedHashSet<Adherence>();

    public WeeklyAdherence() {
        this.week = currentWeekInstance();
    }

    public WeeklyAdherence(String patientId, String treatmentId, TreatmentWeek week) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.week = week;
    }

    public WeeklyAdherence(String patientId, String treatmentId, TreatmentWeek week, List<DayOfWeek> pillDays, String tbId, String providerId) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.week = week;
        for (DayOfWeek pillDay : pillDays) {
            addAdherenceLog(pillDay, PillStatus.Unknown, tbId, providerId);
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek pillDay, PillStatus pillStatus, String tbId, String providerId) {
        Adherence adherence = new Adherence(patientId, treatmentId, pillDay, week.dateOf(pillDay), pillStatus, tbId, providerId);
        adherenceList.add(adherence);
        return this;
    }

    //Assume implicitly that list is ordered.
    public LocalDate firstDoseTakenOn() {
        for (Adherence adherence : adherenceList) {
            if (PillStatus.Taken == adherence.getPillStatus())
                return adherence.getPillDate();
        }
        return null;
    }

    public List<Adherence> getAdherenceLogs() {
        return new ArrayList<Adherence>(adherenceList);
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
        for (Adherence adherence : adherenceList) {
            adherence.setPatientId(patientId);
        }
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
        for (Adherence adherence : adherenceList) {
            adherence.setTreatmentId(treatmentId);
        }
    }

    public void setProviderId(String providerId) {
        for (Adherence adherence : adherenceList) {
            adherence.setProviderId(providerId);
        }
    }

    public void setTbId(String tbId) {
        for (Adherence adherence : adherenceList) {
            adherence.setTbId(tbId);
        }
    }

}
