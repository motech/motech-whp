package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.*;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class WeeklyAdherenceMapper {

    private TreatmentWeek treatmentWeek;
    AdherenceRecords adherenceRecords;

    public WeeklyAdherenceMapper(TreatmentWeek treatmentWeek, AdherenceRecords adherenceRecords) {
        this.treatmentWeek = treatmentWeek;
        this.adherenceRecords = adherenceRecords;
    }

    public WeeklyAdherence map() {
        if (adherenceRecords.adherenceRecords().isEmpty())
            return null;

        WeeklyAdherence weeklyAdherence = createAdherence();

        for (AdherenceRecord adherenceRecord : adherenceRecords.adherenceRecords()) {
            LocalDate recordDate = adherenceRecord.recordDate();
            DayOfWeek pillDay = DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
            PillStatus pillStatus = PillStatus.get(adherenceRecord.status());
            weeklyAdherence.addAdherenceLog(pillDay, pillStatus, (String) adherenceRecord.meta(AdherenceConstants.TB_ID),
                    (String) adherenceRecord.meta(AdherenceConstants.PROVIDER_ID));
        }
        return weeklyAdherence;
    }

    private WeeklyAdherence createAdherence() {
        String patientId = adherenceRecords.externalId();
        String treatmentId = adherenceRecords.treatmentId();
        return new WeeklyAdherence(patientId, treatmentId, treatmentWeek);
    }
}
