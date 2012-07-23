package org.motechproject.whp.adherence.domain;

import lombok.Getter;
import lombok.Setter;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.LinkedList;
import java.util.List;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

public class WeeklyAdherenceSummary {

    @Getter
    @Setter
    private TreatmentWeek week = currentWeekInstance();

    @Getter
    @Setter
    private int dosesTaken;

    @Getter
    @Setter
    private String patientId;

    public WeeklyAdherenceSummary() {
    }

    public WeeklyAdherenceSummary(String patientId, TreatmentWeek week) {
        this.week = week;
        this.patientId = patientId;
    }

    public static WeeklyAdherenceSummary forFirstWeek(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        return new WeeklyAdherenceSummary(patient.getPatientId(), treatmentWeek);
    }

    public List<DayOfWeek> takenDays(TreatmentCategory treatmentCategory) {
        LinkedList<DayOfWeek> takenDays = new LinkedList<>();
        List<DayOfWeek> daysOfWeek = treatmentCategory.getPillDays();
        for (int i = daysOfWeek.size() - 1; i >= daysOfWeek.size() - dosesTaken; i--) {
            takenDays.push(daysOfWeek.get(i));
        }
        return takenDays;
    }

    public PillStatus pillStatusOn(DayOfWeek pillDay, TreatmentCategory treatmentCategory) {
        if(takenDays(treatmentCategory).contains(pillDay)) return PillStatus.Taken;
        return PillStatus.NotTaken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeeklyAdherenceSummary)) return false;

        WeeklyAdherenceSummary that = (WeeklyAdherenceSummary) o;

        if (dosesTaken != that.dosesTaken) return false;
        if (!patientId.equals(that.patientId)) return false;
        if (!week.equals(that.week)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = week.hashCode();
        result = 31 * result + dosesTaken;
        result = 31 * result + patientId.hashCode();
        return result;
    }
}
