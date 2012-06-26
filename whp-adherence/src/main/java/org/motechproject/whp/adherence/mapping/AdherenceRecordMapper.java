package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;

import java.util.HashMap;
import java.util.Map;

public class AdherenceRecordMapper {

    public static AdherenceRecord map(Adherence adherence) {
        AdherenceRecord request = new AdherenceRecord(adherence.getPatientId(), adherence.getTreatmentId(), adherence.getPillDate());
        request.status(adherence.getPillStatus().getStatus());
        request.meta(metaData(adherence));
        return request;
    }

    private static Map<String, Object> metaData(Adherence adherence) {
        HashMap<String, Object> meta = new HashMap<>();
        meta.put(AdherenceConstants.TB_ID, adherence.getTbId());
        meta.put(AdherenceConstants.PROVIDER_ID, adherence.getProviderId());
        return meta;
    }

}
