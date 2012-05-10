package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.PillStatus;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceLogMapper {

    AdherenceRecord record;

    public AdherenceLogMapper(AdherenceRecord record) {
        this.record = record;
    }

    public AdherenceLog map() {
        LocalDate recordDate = record.recordDate();
        AdherenceLog log = new AdherenceLog();
        log.status(PillStatus.get(record.status()));
        log.setPillDate(recordDate);
        log.setPillDay(dayOfWeekOfRecord(recordDate));
        return log;
    }

    private DayOfWeek dayOfWeekOfRecord(LocalDate recordDate) {
        return DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
    }
}
