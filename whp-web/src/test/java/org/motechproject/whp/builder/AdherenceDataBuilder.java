package org.motechproject.whp.builder;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.HashMap;

public class AdherenceDataBuilder {

    public static Adherence createLog(LocalDate doseDate, String providerId, PillStatus pillStatus) {
        Adherence log = new Adherence();
        log.setPatientId("externalid");
        log.setPillDate(doseDate);
        log.setPillStatus(pillStatus);
        log.setProviderId(providerId);
        return log;
    }
}