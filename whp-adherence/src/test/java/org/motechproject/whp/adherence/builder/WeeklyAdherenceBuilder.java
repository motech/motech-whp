package org.motechproject.whp.adherence.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;

public class WeeklyAdherenceBuilder {

    private static final Map<String, Object> meta = new HashMap<String, Object>();

    private WeeklyAdherence adherence = new WeeklyAdherence("patientId", "treatmentId", currentWeekInstance());

    public WeeklyAdherenceBuilder() {
        meta.put(AdherenceConstants.TB_ID, "tbId");
        meta.put(AdherenceConstants.PROVIDER_ID, "providerId");
    }

    public WeeklyAdherenceBuilder withDefaultLogs() {
        buildWeeklyAdherenceLogs(PillStatus.Taken);
        adherence.setRemark("remark");
        return this;
    }

    private void buildWeeklyAdherenceLogs(PillStatus pillStatus) {
        adherence.addAdherenceLog(Monday, pillStatus, meta);
        adherence.addAdherenceLog(Wednesday, pillStatus, meta);
        adherence.addAdherenceLog(Friday, pillStatus, meta);
    }

    public WeeklyAdherenceBuilder withDefaultLogsForWeek(LocalDate dayInWeek) {
        adherence = new WeeklyAdherence("patientId", "treatmentId", new TreatmentWeek(dayInWeek));
        adherence.addAdherenceLog(Monday, PillStatus.NotTaken, meta);
        adherence.addAdherenceLog(Wednesday, PillStatus.NotTaken, meta);
        adherence.addAdherenceLog(Friday, PillStatus.Taken, meta);
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

    public WeeklyAdherenceBuilder withLog(DayOfWeek dayOfWeek, PillStatus pillStatus) {
        adherence.addAdherenceLog(dayOfWeek, pillStatus, meta);
        return this;
    }

    public WeeklyAdherenceBuilder forPatient(Patient patient){
        HashMap<String, Object> meta = new HashMap<String, Object>();
        meta.put(AdherenceConstants.TB_ID, patient.tbId());
        meta.put(AdherenceConstants.PROVIDER_ID, patient.providerId());
        return this.withPatientId(patient.getPatientId())
                .withMetaData(meta)
                .withTreatmentId(patient.currentTreatmentId());
    }

    private WeeklyAdherenceBuilder withMetaData(HashMap<String, Object> meta) {
        adherence.setMetaData(meta);
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
