package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;

import java.util.ArrayList;
import java.util.List;

@Data
public class Adherence {

    private List<AdherenceLog> adherenceLogs = new ArrayList<AdherenceLog>();

    public Adherence() {
    }

    public Adherence(TreatmentWeek week, ProvidedTreatment providedTreatment) {
        Treatment treatment = providedTreatment.getTreatment();
        for (DayOfWeek pillDay : treatment.getTreatmentCategory().getPillDays()) {
            adherenceLogs.add(new AdherenceLog(pillDay, week.dateOf(pillDay)));
        }
    }

    public Adherence(List<AdherenceLog> adherenceLogs) {
        this.adherenceLogs = adherenceLogs;
    }

    public Adherence addAdherenceLog(AdherenceLog log) {
        adherenceLogs.add(log);
        return this;
    }
}
