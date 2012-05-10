package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class WeeklyAdherenceMapper {

    private TreatmentWeek treatmentWeek;
    AdherenceRecords adherenceRecords;

    public WeeklyAdherenceMapper(TreatmentWeek treatmentWeek, AdherenceRecords adherenceRecords) {
        this.treatmentWeek = treatmentWeek;
        this.adherenceRecords = adherenceRecords;
    }

    public WeeklyAdherence weeklyAdherence() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(treatmentWeek);

        for (AdherenceRecord adherenceRecord : adherenceRecords.adherenceRecords()) {
            AdherenceLogMapper adherenceLogMapper = new AdherenceLogMapper(adherenceRecord);
            AdherenceLog log = adherenceLogMapper.map();
            weeklyAdherence.addAdherenceLog(log.getPillDay(), log.getPillStatus());
        }
        return weeklyAdherence;
    }
}
