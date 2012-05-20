package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;

import java.util.HashMap;
import java.util.Map;

public class AdherenceDataMapper {


    public static AdherenceData request(Adherence adherence) {
        AdherenceData request = new AdherenceData(adherence.getPatientId(), adherence.getTreatmentId(), adherence.getPillDate());
        request.status(adherence.getPillStatus().getStatus());
        request.meta(metaData(adherence));
        return request;
    }

    private static Map<String, Object> metaData(Adherence adherence) {
        HashMap<String, Object> meta = new HashMap<String, Object>();
        meta.put(AdherenceConstants.TB_ID, adherence.getTbId());
        meta.put(AdherenceConstants.PROVIDER_ID, adherence.getProviderId());
        return meta;
    }

}
