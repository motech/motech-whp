package org.motechproject.whp.uimodel;

import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import java.util.LinkedList;
import java.util.List;

public class PillDays {

    public static List<DayOfWeek> takenDays(TreatmentCategory treatmentCategory, int numberOfDosesTaken) {
        LinkedList<DayOfWeek> takenDays = new LinkedList<DayOfWeek>();
        List<DayOfWeek> daysOfWeek = treatmentCategory.getPillDays();
        for (int i = daysOfWeek.size() - 1; i >= daysOfWeek.size() - numberOfDosesTaken; i--) {
            takenDays.push(daysOfWeek.get(i));
        }
        return takenDays;
    }
}