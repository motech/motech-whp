package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public class WeeklyAdherenceMapper {

    private String patientId;
    private TreatmentWeek treatmentWeek;
    private List<Adherence> adherenceRecords;

    public WeeklyAdherenceMapper(String patientId, TreatmentWeek treatmentWeek, List<Adherence> adherenceList) {
        this.patientId = patientId;
        this.treatmentWeek = treatmentWeek;
        this.adherenceRecords = adherenceList;
    }

    public WeeklyAdherence map() {
        if (isEmpty(adherenceRecords))
            return null;
        WeeklyAdherence weeklyAdherence = createAdherence();
        for (Adherence adherenceRecord : adherenceRecords) {
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
