package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;

import java.util.*;

import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;
import static org.motechproject.whp.refdata.domain.WHPConstants.UNKNOWN;

public class WeeklyAdherence {

    @Getter
    private TreatmentWeek week;

    private Set<Adherence> adherenceList = new LinkedHashSet<>();

    public WeeklyAdherence() {
        this.week = currentWeekInstance();
    }

    public WeeklyAdherence(TreatmentWeek week) {
        this.week = week;
    }

    public WeeklyAdherence(Patient patient, TreatmentWeek week, List<DayOfWeek> pillDays) {
        this.week = week;

        for (DayOfWeek pillDay : pillDays) {
            Treatment treatment = patient.getTreatment(week.dateOf(pillDay));
            // AAAAAAAAhhhh redundant code : need to fix
            if (treatment == null) {
                addAdherenceLog(pillDay, patient.getPatientId(), PillStatus.Unknown, UNKNOWN, UNKNOWN, UNKNOWN);
            } else {
                addAdherenceLog(pillDay, patient.getPatientId(), PillStatus.Unknown, patient.currentTherapyId(), treatment.getProviderId(), treatment.getTbId());
            }
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek pillDay, String patientId, PillStatus pillStatus, String therapyDocId, String providerId, String tbId) {
        Adherence adherence = new Adherence(patientId, therapyDocId, pillDay, week.dateOf(pillDay), pillStatus, tbId, providerId);
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
        return new ArrayList<>(adherenceList);
    }

    public String getPatientId() {
        if (adherenceList.size() > 0) {
            return adherenceList.toArray(new Adherence[adherenceList.size()])[0].getPatientId();
        }
        return null;
    }

    public void setPatientId(String patientId) {
        for (Adherence adherence : adherenceList) {
            adherence.setPatientId(patientId);
        }
    }

    public void setTreatmentId(String treatmentId) {
        for (Adherence adherence : adherenceList) {
            adherence.setTreatmentId(treatmentId);
        }
    }

    public static WeeklyAdherence createAdherenceFor(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        return new WeeklyAdherence(patient, treatmentWeek, pillDays(patient));
    }

    private static List<DayOfWeek> pillDays(Patient patient) {
        return patient.currentTherapy().getTreatmentCategory().getPillDays();
    }
}
