package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class WeeklyAdherenceMapper {

    AdherenceRecords adherenceRecords;

    public WeeklyAdherenceMapper(AdherenceRecords adherenceRecords) {
        this.adherenceRecords = adherenceRecords;
    }

    public WeeklyAdherence map() {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence();

        for (AdherenceRecord adherenceRecord : adherenceRecords.adherenceRecords()) {
            AdherenceLogMapper adherenceLogMapper = new AdherenceLogMapper(adherenceRecord);
            AdherenceLog log = adherenceLogMapper.adherenceLog();
            weeklyAdherence.addAdherenceLog(log.getPillDay(), log);
        }
        return weeklyAdherence;
    }
}
