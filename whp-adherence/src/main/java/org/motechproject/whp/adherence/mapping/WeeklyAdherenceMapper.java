package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;
import static org.springframework.util.CollectionUtils.isEmpty;

public class WeeklyAdherenceMapper {

    private TreatmentWeek treatmentWeek;
    private AdherenceRecords adherenceRecords;

    public WeeklyAdherenceMapper(TreatmentWeek treatmentWeek, AdherenceRecords adherenceRecords) {
        this.treatmentWeek = treatmentWeek;
        this.adherenceRecords = adherenceRecords;
    }

    public WeeklyAdherence map() {
        if (isEmpty(adherenceRecords.adherenceRecords()))
            return null;
        WeeklyAdherence weeklyAdherence = createAdherence();
        for (AdherenceRecord adherenceRecord : adherenceRecords.adherenceRecords()) {
            LocalDate recordDate = adherenceRecord.recordDate();
            DayOfWeek pillDay = DayOfWeek.getDayOfWeek(recordDate.getDayOfWeek());
            PillStatus pillStatus = PillStatus.get(adherenceRecord.status());
            weeklyAdherence.addAdherenceLog(pillDay, pillStatus);
        }
        return weeklyAdherence;
    }

    private WeeklyAdherence createAdherence() {
        String patientId = adherenceRecords.externalId();
        String treatmentId = adherenceRecords.treatmentId();
        String tbId = metaData(AdherenceConstants.TB_ID);
        String providerId = metaData(AdherenceConstants.PROVIDER_ID);

        WeeklyAdherence adherence = new WeeklyAdherence(patientId, treatmentId, treatmentWeek);
        adherence.setTbId(tbId);
        adherence.setProviderId(providerId);
        return adherence;
    }

    private String metaData(String key) {
        List<AdherenceRecord> records = adherenceRecords.adherenceRecords();
        return CollectionUtils.isEmpty(records) ? null : records.get(0).meta().get(key).toString();
    }
}
