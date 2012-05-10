package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
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

    public WeeklyAdherence map() {
        if (adherenceRecords.adherenceRecords().isEmpty())
            return null;

        WeeklyAdherence weeklyAdherence = createAdherence();
        AdherenceRecord record = adherenceRecords.adherenceRecords().get(0);
        mapTbId(record, weeklyAdherence);
        mapProviderId(record, weeklyAdherence);

        for (AdherenceRecord adherenceRecord : adherenceRecords.adherenceRecords()) {
            AdherenceMapper adherenceMapper = new AdherenceMapper(adherenceRecord);
            Adherence day = adherenceMapper.map();
            weeklyAdherence.addAdherenceLog(day.getPillDay(), day.getPillStatus());
        }
        return weeklyAdherence;
    }

    private void mapProviderId(AdherenceRecord record, WeeklyAdherence weeklyAdherence) {
        String providerId = (String) record.meta().get(AdherenceConstants.PROVIDER_ID);
        weeklyAdherence.setProviderId(providerId);
    }

    private void mapTbId(AdherenceRecord record, WeeklyAdherence weeklyAdherence) {
        String tbId = (String) record.meta().get(AdherenceConstants.TB_ID);
        weeklyAdherence.setTbId(tbId);
    }

    private WeeklyAdherence createAdherence() {
        String patientId = adherenceRecords.externalId();
        String treatmentId = adherenceRecords.treatmentId();
        return new WeeklyAdherence(patientId, treatmentId, treatmentWeek);
    }
}
