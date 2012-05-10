package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import java.util.ArrayList;
import java.util.List;

import static org.joda.time.format.DateTimeFormat.forPattern;

@Data
public class WeeklyAdherenceForm {

    LocalDate referenceDate;

    private String patientId;
    private String treatmentId;
    private String tbId;
    private String providerId;

    List<AdherenceForm> adherenceList = new ArrayList<AdherenceForm>();

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(WeeklyAdherence weeklyAdherence) {
        referenceDate = weeklyAdherence.getWeek().getReference();
        patientId = weeklyAdherence.getPatientId();
        treatmentId = weeklyAdherence.getTreatmentId();
        tbId = weeklyAdherence.getTbId();
        providerId = weeklyAdherence.getProviderId();

        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            adherenceList.add(new AdherenceForm(adherence.getPillDay(), adherence.getPillDate(), adherence.getPillStatus()));
        }
    }

    public WeeklyAdherence weeklyAdherence() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patientId, treatmentId, new TreatmentWeek(referenceDate));
        weeklyAdherence.setProviderId(providerId);
        weeklyAdherence.setTbId(tbId);
        for (AdherenceForm form : adherenceList) {
            weeklyAdherence.addAdherenceLog(form.getPillDay(), form.getPillStatus());
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
