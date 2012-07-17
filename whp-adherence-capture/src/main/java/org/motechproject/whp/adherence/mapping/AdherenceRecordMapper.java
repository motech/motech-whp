package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;

import java.util.ArrayList;
import java.util.List;

public class AdherenceRecordMapper {

    public static AdherenceRecord map(Adherence adherence) {
        AdherenceRecord request = new AdherenceRecord(adherence.getPatientId(), adherence.getTreatmentId(), adherence.getPillDate());
        request.status(adherence.getPillStatus().getStatus());
        request.tbId(adherence.getTbId());
        request.providerId(adherence.getProviderId());
        return request;
    }

    public static List<AdherenceRecord> map(List<Adherence> adherenceList) {
        List<AdherenceRecord> adherenceData = new ArrayList<>();
        for (Adherence adherence : adherenceList)
            adherenceData.add(AdherenceRecordMapper.map(adherence));
        return adherenceData;
    }

    public static List<AdherenceRecord> map(AdherenceList adherenceList) {
        ArrayList<AdherenceRecord> adherenceRecords = new ArrayList<>();
        for (Adherence adherence : adherenceList) {
            adherenceRecords.add(AdherenceRecordMapper.map(adherence));
        }
        return adherenceRecords;
    }
}
