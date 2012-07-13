package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.domain.Patient;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

public class WeeklyAdherenceSummaryBuilder {

    private WeeklyAdherenceSummary adherenceSummary = new WeeklyAdherenceSummary(PATIENT_ID, currentWeekInstance());

    public WeeklyAdherenceSummaryBuilder() {
        adherenceSummary.setPatientId(PATIENT_ID);
        adherenceSummary.setDosesTaken(3);
    }

    public WeeklyAdherenceSummaryBuilder withPatientId(String patientId) {
        adherenceSummary.setPatientId(patientId);
        return this;
    }

    public WeeklyAdherenceSummaryBuilder withDosesTaken(int dosesTaken) {
        adherenceSummary.setDosesTaken(dosesTaken);
        return this;
    }

    public WeeklyAdherenceSummaryBuilder forPatient(Patient patient) {
        return this.withPatientId(patient.getPatientId());
    }

    public WeeklyAdherenceSummaryBuilder forWeek(LocalDate dayInWeek) {
        this.adherenceSummary.setWeek(new TreatmentWeek(dayInWeek));
        return this;
    }

    public WeeklyAdherenceSummary build() {
        return adherenceSummary;
    }

}
