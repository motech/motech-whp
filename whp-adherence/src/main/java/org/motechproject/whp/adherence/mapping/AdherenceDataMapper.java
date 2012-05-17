package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;

public class AdherenceDataMapper {


    public static AdherenceData request(Adherence adherence) {
        AdherenceData request = new AdherenceData(adherence.getPatientId(), adherence.getTreatmentId(), adherence.getPillDate());
        request.status(adherence.getPillStatus().getStatus());
        request.meta(adherence.getMeta());
        return request;
    }

}
