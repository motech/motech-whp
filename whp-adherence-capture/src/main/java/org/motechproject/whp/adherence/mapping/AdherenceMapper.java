package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.List;

public class AdherenceMapper {

    public AdherenceList map(List<AdherenceRecord> adherenceRecords) {
        AdherenceList adherenceList = new AdherenceList();
        for (AdherenceRecord adherenceDatum : adherenceRecords) {
            adherenceList.add(map(adherenceDatum));
        }
        return adherenceList;
    }

    public Adherence map(AdherenceRecord adherenceRecord) {
        Adherence adherence = new Adherence(adherenceRecord.doseDate());

        adherence.setPatientId(adherenceRecord.externalId());
        adherence.setTreatmentId(adherenceRecord.treatmentId());

        adherence.setTbId(adherenceRecord.tbId());
        adherence.setProviderId(adherenceRecord.providerId());

        adherence.setPillStatus(PillStatus.get(adherenceRecord.status()));
        adherence.setPillDay(dayOfWeekOfRecord(adherenceRecord.doseDate()));
        adherence.setDistrict(adherenceRecord.district());
        return adherence;
    }

    private DayOfWeek dayOfWeekOfRecord(LocalDate recordDate) {
        return DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
    }

}
