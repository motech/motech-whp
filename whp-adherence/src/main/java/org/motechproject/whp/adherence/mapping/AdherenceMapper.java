package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.ArrayList;
import java.util.List;

public class AdherenceMapper {

    public List<Adherence> map(List<AdherenceRecord> adherenceRecords) {
        List<Adherence> adherenceList = new ArrayList<>();
        for (AdherenceRecord adherenceDatum : adherenceRecords) {
            adherenceList.add(map(adherenceDatum));
        }
        return adherenceList;
    }

    public Adherence map(AdherenceRecord adherenceRecord) {
        Adherence adherence = new Adherence(adherenceRecord.doseDate());

        adherence.setPatientId(adherenceRecord.externalId());
        adherence.setTreatmentId(adherenceRecord.treatmentId());

        adherence.setTbId(adherenceRecord.meta().get(AdherenceConstants.TB_ID).toString());
        adherence.setProviderId(adherenceRecord.meta().get(AdherenceConstants.PROVIDER_ID).toString());

        adherence.setPillStatus(PillStatus.get(adherenceRecord.status()));
        adherence.setPillDay(dayOfWeekOfRecord(adherenceRecord.doseDate()));
        return adherence;
    }

    private DayOfWeek dayOfWeekOfRecord(LocalDate recordDate) {
        return DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
    }

}
