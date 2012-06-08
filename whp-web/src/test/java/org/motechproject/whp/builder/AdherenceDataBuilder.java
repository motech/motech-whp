package org.motechproject.whp.builder;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.HashMap;

public class AdherenceDataBuilder {

    public static AdherenceData createLog(LocalDate doseDate, String providerId, PillStatus pillStatus) {
        AdherenceData log1 = new AdherenceData("externalid", "", doseDate);
        log1.status(pillStatus.getStatus());
        HashMap<String, Object> meta1 = new HashMap<String, Object>();
        meta1.put(AdherenceConstants.PROVIDER_ID, providerId);
        log1.meta(meta1);
        return log1;
    }
}