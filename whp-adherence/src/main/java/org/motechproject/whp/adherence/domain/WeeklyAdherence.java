package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.Patient;

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
    private String tbId;
    @Getter
    private String providerId;
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

    public WeeklyAdherence(String patientId, String treatmentId, TreatmentWeek week, List<DayOfWeek> pillDays) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.week = week;

        for (DayOfWeek pillDay : pillDays) {
            addAdherenceLog(pillDay, PillStatus.Unknown);
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek pillDay, PillStatus pillStatus) {
        Adherence adherence = new Adherence(patientId, treatmentId, pillDay, week.dateOf(pillDay), pillStatus, tbId, providerId);
        adherenceList.add(adherence);
        return this;
    }

    public LocalDate firstDoseTakenOn() {
        /*Assume that the set is ordered by date*/
        for (Adherence adherence : adherenceList) {
            if (PillStatus.Taken == adherence.getPillStatus())
                return adherence.getPillDate();
        }
        return null;
    }

    public int numberOfDosesTaken() {
        int total = 0;
        for (Adherence adherence : adherenceList) {
            total = (PillStatus.Taken == adherence.getPillStatus()) ? total + 1 : total;
        }
        return total;
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

    public void setTbId(String tbId) {
        this.tbId = tbId;
        for (Adherence adherence : adherenceList) {
            adherence.setTbId(tbId);
        }
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
        for (Adherence adherence : adherenceList) {
            adherence.setProviderId(providerId);
        }
    }

    public static WeeklyAdherence createAdherenceFor(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        WeeklyAdherence adherence = new WeeklyAdherence(
                patient.getPatientId(),
                patient.currentTreatmentId(),
                treatmentWeek,
                pillDays(patient)
        );
        adherence.setTbId(patient.getCurrentProvidedTreatment().getTbId());
        adherence.setProviderId(patient.getCurrentProvidedTreatment().getProviderId());
        return adherence;
    }

    private static List<DayOfWeek> pillDays(Patient patient) {
        return patient.getCurrentProvidedTreatment().getTreatment().getTreatmentCategory().getPillDays();
    }
}
