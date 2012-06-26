package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public class WeeklyAdherenceMapper {

    private String patientId;
    private TreatmentWeek treatmentWeek;

    public WeeklyAdherenceMapper(Patient patient, TreatmentWeek treatmentWeek) {
        this.patientId = patient.getPatientId();
        this.treatmentWeek = treatmentWeek;
    }

    public WeeklyAdherence map(List<Adherence> adherenceList) {
        if (isEmpty(adherenceList))
            return null;
        WeeklyAdherence weeklyAdherence = createAdherence();
        for (Adherence adherenceRecord : adherenceList) {
            LocalDate recordDate = adherenceRecord.getPillDate();
            DayOfWeek pillDay = DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
            PillStatus pillStatus = adherenceRecord.getPillStatus();
            weeklyAdherence.addAdherenceLog(pillDay, patientId, pillStatus, adherenceRecord.getTreatmentId(), adherenceRecord.getProviderId(), adherenceRecord.getTbId());
        }
        return weeklyAdherence;
    }

    private WeeklyAdherence createAdherence() {
        return new WeeklyAdherence(treatmentWeek);
    }

}
