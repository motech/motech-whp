package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;

public class AdherenceDataMapper {

    Adherence adherence;

    public AdherenceDataMapper(Adherence adherence) {
        this.adherence = adherence;
    }

    public AdherenceData request() {
        AdherenceData request = new AdherenceData(adherence.getPatientId(), adherence.getTreatmentId(), adherence.getPillDate());
        request.status(adherence.getPillStatus().getStatus());
        request.addMeta(AdherenceConstants.PROVIDER_ID, adherence.getProviderId());
        request.addMeta(AdherenceConstants.TB_ID, adherence.getTbId());
        return request;
    }

}
