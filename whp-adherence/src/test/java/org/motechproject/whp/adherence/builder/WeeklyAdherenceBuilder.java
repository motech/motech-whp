package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.CurrentTreatmentWeek;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;

import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherenceBuilder {

    private WeeklyAdherence adherence = new WeeklyAdherence("patientId", "treatmentId", currentWeekInstance());

    public WeeklyAdherenceBuilder withDefaultLogs() {
        adherence.addAdherenceLog(Monday, PillStatus.Taken);
        adherence.addAdherenceLog(Wednesday, PillStatus.Taken);
        adherence.addAdherenceLog(Friday, PillStatus.Taken);
        return this;
    }

    public WeeklyAdherenceBuilder withDefaultLogsForWeek(LocalDate dayInWeek) {
        adherence = new WeeklyAdherence("patientId", "treatmentId", new TreatmentWeek(dayInWeek));
        adherence.addAdherenceLog(Monday, PillStatus.Taken);
        adherence.addAdherenceLog(Wednesday, PillStatus.Taken);
        adherence.addAdherenceLog(Friday, PillStatus.Taken);
        return this;
    }


    public WeeklyAdherenceBuilder withLog(DayOfWeek dayOfWeek, PillStatus pillStatus) {
        adherence.addAdherenceLog(dayOfWeek, pillStatus);
        return this;
    }

    public WeeklyAdherenceBuilder withPatientId(String patientId){
        adherence.setPatientId(patientId);
        return this;
    }

    public WeeklyAdherenceBuilder withTreatmentId(String treatmentId){
        adherence.setTreatmentId(treatmentId);
        return this;
    }

    public WeeklyAdherenceBuilder withProviderId(String providerId){
        adherence.setProviderId(providerId);
        return this;
    }

    public WeeklyAdherenceBuilder withTbId(String tbId){
        adherence.setTbId(tbId);
        return this;
    }

    public WeeklyAdherenceBuilder forPatient(Patient patient){
        ProvidedTreatment currentTreatment = patient.getCurrentProvidedTreatment();
        return this.withPatientId(patient.getPatientId())
                .withProviderId(currentTreatment.getProviderId())
                .withTbId(patient.tbId())
                .withTreatmentId(patient.currentTreatmentId());
    }

    public WeeklyAdherenceBuilder zeroDosesTaken() {
        adherence.addAdherenceLog(Monday, PillStatus.NotTaken);
        adherence.addAdherenceLog(Wednesday, PillStatus.NotTaken);
        adherence.addAdherenceLog(Friday, PillStatus.NotTaken);
        return this;
    }

    public WeeklyAdherence build() {
        return adherence;
    }

}
