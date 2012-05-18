package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.Patient;

import java.util.*;

import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherence {

    @Getter
    private String patientId;
    @Getter
    private String treatmentId;
    @Getter
    private TreatmentWeek week;
    @Getter
    @Setter
    private String remark;

    private Set<Adherence> adherenceList = new LinkedHashSet<Adherence>();

    public WeeklyAdherence() {
        this.week = currentWeekInstance();
    }

    public WeeklyAdherence(String patientId, String treatmentId, TreatmentWeek week) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.week = week;
    }

    public WeeklyAdherence(String patientId, String treatmentId, TreatmentWeek week, List<DayOfWeek> pillDays, Map<String, Object> map) {
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.week = week;
        for (DayOfWeek pillDay : pillDays) {
            addAdherenceLog(pillDay, PillStatus.Unknown, map);
        }
    }

    public WeeklyAdherence addAdherenceLog(DayOfWeek pillDay, PillStatus pillStatus, Map<String, Object> meta) {
        Adherence adherence = new Adherence(patientId, treatmentId, pillDay, week.dateOf(pillDay), pillStatus, meta);
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

    public void setMetaData(Map<String, Object> meta) {
        for (Adherence adherence : adherenceList) {
            adherence.setMeta(meta);
        }
    }

    public Map<String, Object> getMetaData() {
        if (adherenceList.isEmpty())
            return new HashMap<String, Object>();
        else
            return new ArrayList<Adherence>(adherenceList).get(0).getMeta();
    }

    public static WeeklyAdherence createAdherenceFor(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(AdherenceConstants.TB_ID, patient.getCurrentProvidedTreatment().getTbId());
        meta.put(AdherenceConstants.PROVIDER_ID, patient.getCurrentProvidedTreatment().getProviderId());
        return new WeeklyAdherence(patient.getPatientId(), patient.currentTreatmentId(), treatmentWeek, pillDays(patient), meta);
    }

    private static List<DayOfWeek> pillDays(Patient patient) {
        return patient.getCurrentProvidedTreatment().getTreatment().getTreatmentCategory().getPillDays();
    }
}
