package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import java.util.LinkedList;
import java.util.List;

import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherenceSummary {

    @Getter
    @Setter
    private TreatmentWeek week;

    @Getter
    @Setter
    private int dosesTaken;

    @Getter
    @Setter
    private String patientId;

    public WeeklyAdherenceSummary() {
        this.week = currentWeekInstance();
    }

    public WeeklyAdherenceSummary(TreatmentWeek week) {
        this.week = week;
    }

    public WeeklyAdherenceSummary(Patient patient, TreatmentWeek week) {
        this.week = week;
        this.patientId = patient.getPatientId();
    }

    public static WeeklyAdherenceSummary currentWeek(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        return new WeeklyAdherenceSummary(patient, treatmentWeek);
    }

    public List<DayOfWeek> takenDays(TreatmentCategory treatmentCategory) {
        LinkedList<DayOfWeek> takenDays = new LinkedList<>();
        List<DayOfWeek> daysOfWeek = treatmentCategory.getPillDays();
        for (int i = daysOfWeek.size() - 1; i >= daysOfWeek.size() - dosesTaken; i--) {
            takenDays.push(daysOfWeek.get(i));
        }
        return takenDays;
    }

}
