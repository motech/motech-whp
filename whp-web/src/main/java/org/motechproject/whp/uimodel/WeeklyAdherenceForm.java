package org.motechproject.whp.uimodel;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.ArrayList;
import java.util.List;

import static org.joda.time.format.DateTimeFormat.forPattern;

@Data
public class WeeklyAdherenceForm {

    LocalDate referenceDate;

    private String patientId;
    private String treatmentId;

    List<AdherenceForm> adherenceList = new ArrayList<AdherenceForm>();

    public WeeklyAdherenceForm() {
    }

    public WeeklyAdherenceForm(WeeklyAdherence weeklyAdherence, TreatmentInterruptions treatmentInterruptions) {
        referenceDate = weeklyAdherence.getWeek().getReference();
        patientId = weeklyAdherence.getPatientId();
        treatmentId = weeklyAdherence.getTreatmentId();

        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            boolean isTreatmentInterrupted = treatmentInterruptions.isTreatmentInterrupted(adherence.getPillDate());
            AdherenceForm adherenceForm = new AdherenceForm(adherence.getPillDay(), adherence.getPillDate(), adherence.getPillStatus(), isTreatmentInterrupted, adherence.getMeta());
            adherenceList.add(adherenceForm);
        }
    }

    public WeeklyAdherence weeklyAdherence() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patientId, treatmentId, new TreatmentWeek(referenceDate));
        for (AdherenceForm form : adherenceList) {
            weeklyAdherence.addAdherenceLog(form.getPillDay(), form.getPillStatus(), form.getMeta());
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
