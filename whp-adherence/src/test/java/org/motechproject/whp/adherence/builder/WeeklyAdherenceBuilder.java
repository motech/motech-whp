package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;

import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;
import static org.motechproject.whp.patient.builder.PatientBuilder.*;

public class WeeklyAdherenceBuilder {

    public static final String THERAPY_ID = "therapyId";

    private WeeklyAdherence adherence = new WeeklyAdherence(currentWeekInstance());

    public WeeklyAdherenceBuilder() {
    }

    public WeeklyAdherenceBuilder withDefaultLogs() {
        buildWeeklyAdherenceLogs(PillStatus.Taken);
        return this;
    }

    private void buildWeeklyAdherenceLogs(PillStatus pillStatus) {
        adherence.addAdherenceLog(Monday, PATIENT_ID, pillStatus, THERAPY_ID, PROVIDER_ID, TB_ID);
        adherence.addAdherenceLog(Wednesday, PATIENT_ID, pillStatus, THERAPY_ID, PROVIDER_ID, TB_ID);
        adherence.addAdherenceLog(Friday, PATIENT_ID, pillStatus, THERAPY_ID, PROVIDER_ID, TB_ID);
    }

    public WeeklyAdherenceBuilder withDefaultLogsForWeek(LocalDate dayInWeek) {
        adherence = new WeeklyAdherence(new TreatmentWeek(dayInWeek));
        adherence.addAdherenceLog(Monday, PATIENT_ID, PillStatus.NotTaken, THERAPY_ID, PROVIDER_ID, TB_ID);
        adherence.addAdherenceLog(Wednesday, PATIENT_ID, PillStatus.NotTaken, THERAPY_ID, PROVIDER_ID, TB_ID);
        adherence.addAdherenceLog(Friday, PATIENT_ID, PillStatus.Taken, THERAPY_ID, PROVIDER_ID, TB_ID);
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
        adherence.addAdherenceLog(dayOfWeek, PATIENT_ID, pillStatus, THERAPY_ID, PROVIDER_ID, TB_ID);
        return this;
    }

    public WeeklyAdherenceBuilder forPatient(Patient patient) {
        return this.withPatientId(patient.getPatientId()).withTreatmentId(patient.currentTherapyId());
    }

    public WeeklyAdherenceBuilder zeroDosesTaken() {
        buildWeeklyAdherenceLogs(PillStatus.NotTaken);
        return this;
    }

    public WeeklyAdherence build() {
        return adherence;
    }

}
