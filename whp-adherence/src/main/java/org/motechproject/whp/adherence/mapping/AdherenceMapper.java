package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceMapper {

    AdherenceRecord record;

    public AdherenceMapper(AdherenceRecord record) {
        this.record = record;
    }

    public Adherence map() {
        LocalDate recordDate = record.recordDate();
        Adherence day = new Adherence();
        day.status(PillStatus.get(record.status()));
        day.setPillDate(recordDate);
        day.setPillDay(dayOfWeekOfRecord(recordDate));
        return day;
    }

    private DayOfWeek dayOfWeekOfRecord(LocalDate recordDate) {
        return DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
    }
}
