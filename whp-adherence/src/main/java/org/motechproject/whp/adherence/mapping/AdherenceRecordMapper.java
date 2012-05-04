package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.AdherenceLog;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceRecordMapper {

    AdherenceRecord record;

    public AdherenceRecordMapper(AdherenceRecord record) {
        this.record = record;
    }

    public AdherenceLog adherenceLog() {
        LocalDate recordDate = record.recordDate();
        AdherenceLog log = new AdherenceLog();
        log.setIsTaken(isTaken());
        log.setPillDate(recordDate);
        log.setPillDay(dayOfWeekOfRecord(recordDate));
        return log;
    }

    private boolean isTaken() {
        return record.dosesTaken() != 0;
    }

    private DayOfWeek dayOfWeekOfRecord(LocalDate recordDate) {
        return DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
    }
}
