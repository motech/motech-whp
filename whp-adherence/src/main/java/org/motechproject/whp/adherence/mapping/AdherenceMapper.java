package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceMapper {

    public Adherence map(AdherenceRecord record) {
        LocalDate recordDate = record.recordDate();
        Adherence day = new Adherence();
        day.status(PillStatus.get(record.status()));
        day.setPillDate(recordDate);
        day.setPillDay(dayOfWeekOfRecord(recordDate));
        return day;
    }

    public List<Adherence> map(List<AdherenceData> adherenceData) {
        List<Adherence> adherences = new ArrayList<Adherence>();
        for (AdherenceData adherenceDatum : adherenceData) {
            Adherence adherence = new Adherence(adherenceDatum.doseDate());
            adherence.setPatientId(adherenceDatum.externalId());
            adherence.setTreatmentId(adherenceDatum.treatmentId());
            adherence.setTbId(adherenceDatum.meta().get(AdherenceConstants.TB_ID).toString());
            adherence.setPillStatus(PillStatus.get(adherenceDatum.status()));
            adherences.add(adherence);
        }
        return adherences;
    }

    private DayOfWeek dayOfWeekOfRecord(LocalDate recordDate) {
        return DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
    }
}
