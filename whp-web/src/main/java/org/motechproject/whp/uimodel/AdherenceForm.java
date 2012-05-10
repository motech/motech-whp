package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import java.util.ArrayList;
import java.util.List;

import static org.joda.time.format.DateTimeFormat.forPattern;

@Data
public class AdherenceForm {

    LocalDate referenceDate;

    List<AdherenceLog> adherenceLogs = new ArrayList<AdherenceLog>();

    public AdherenceForm() {
    }

    public AdherenceForm(WeeklyAdherence adherence) {
        referenceDate = adherence.getWeek().getReference();
        adherenceLogs = adherence.getAdherenceLogs();
    }

    public WeeklyAdherence weeklyAdherence() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(new TreatmentWeek(referenceDate));
        for (AdherenceLog log : adherenceLogs) {
            weeklyAdherence.addAdherenceLog(log.getPillDay(), log.getPillStatus());
        }
        return weeklyAdherence;
    }

    public String getReferenceDateString() {
        return referenceDate.toString("dd-MM-YYYY");
    }

    public void setReferenceDateString(String pillDate) {
        this.referenceDate = LocalDate.parse(pillDate, forPattern("dd-MM-YYYY"));
    }
}
