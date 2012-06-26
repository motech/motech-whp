package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdherenceRecordMapper {

    public static AdherenceRecord map(Adherence adherence) {
        AdherenceRecord request = new AdherenceRecord(adherence.getPatientId(), adherence.getTreatmentId(), adherence.getPillDate());
        request.status(adherence.getPillStatus().getStatus());
        request.meta(metaData(adherence));
        return request;
    }

    public static List<AdherenceRecord> map(List<Adherence> adherenceList) {
        List<AdherenceRecord> adherenceData = new ArrayList<>();
        for (Adherence adherence : adherenceList)
            adherenceData.add(AdherenceRecordMapper.map(adherence));
        return adherenceData;
    }

    public static List<AdherenceRecord> map(WeeklyAdherence weeklyAdherence) {
        return map(weeklyAdherence.getAdherenceLogs());
    }

    private static Map<String, Object> metaData(Adherence adherence) {
        HashMap<String, Object> meta = new HashMap<>();
        meta.put(AdherenceConstants.TB_ID, adherence.getTbId());
        meta.put(AdherenceConstants.PROVIDER_ID, adherence.getProviderId());
        return meta;
    }

}
