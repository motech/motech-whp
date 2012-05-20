package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;

import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherenceBuilder {

    private WeeklyAdherence adherence = new WeeklyAdherence("patientId", "treatmentId", currentWeekInstance());

    public WeeklyAdherenceBuilder() {
        adherence.setTbId("tbId");
        adherence.setProviderId("providerId");
    }

    public WeeklyAdherenceBuilder withDefaultLogs() {
        buildWeeklyAdherenceLogs(PillStatus.Taken);
        adherence.setRemark("remark");
        return this;
    }

    private void buildWeeklyAdherenceLogs(PillStatus pillStatus) {
        adherence.addAdherenceLog(Monday, pillStatus);
        adherence.addAdherenceLog(Wednesday, pillStatus);
        adherence.addAdherenceLog(Friday, pillStatus);
    }

    public WeeklyAdherenceBuilder withDefaultLogsForWeek(LocalDate dayInWeek) {
        adherence = new WeeklyAdherence("patientId", "treatmentId", new TreatmentWeek(dayInWeek));
        adherence.addAdherenceLog(Monday, PillStatus.NotTaken);
        adherence.addAdherenceLog(Wednesday, PillStatus.NotTaken);
        adherence.addAdherenceLog(Friday, PillStatus.Taken);
        return this;
    }

    public WeeklyAdherenceBuilder withPatientId(String patientId) {
        adherence.setPatientId(patientId);
        return this;
    }

    public WeeklyAdherenceBuilder withTreatmentId(String treatmentId) {
        adherence.setTreatmentId(treatmentId);
        return this;
    }

    public WeeklyAdherenceBuilder withLog(DayOfWeek dayOfWeek, PillStatus pillStatus) {
        adherence.addAdherenceLog(dayOfWeek, pillStatus);
        return this;
    }

    public WeeklyAdherenceBuilder forPatient(Patient patient) {
        return this.withPatientId(patient.getPatientId())
                .withTbId(patient.tbId())
                .withProviderId(patient.providerId())
                .withTreatmentId(patient.currentTreatmentId());
    }

    private WeeklyAdherenceBuilder withTbId(String tbId) {
        adherence.setTbId(tbId);
        return this;
    }

    private WeeklyAdherenceBuilder withProviderId(String providerId) {
        adherence.setProviderId(providerId);
        return this;
    }

    public WeeklyAdherenceBuilder zeroDosesTaken() {
        buildWeeklyAdherenceLogs(PillStatus.NotTaken);
        return this;
    }

    public WeeklyAdherence build() {
        return adherence;
    }

}
