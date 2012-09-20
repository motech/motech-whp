package org.motechproject.whp.adherence.mapping;

import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceLog;

import java.util.ArrayList;
import java.util.List;

public class AdherenceLogMapper {

    public AdherenceLog map(AdherenceRecord adherenceRecord) {
        AdherenceLog log = new AdherenceLog(adherenceRecord.externalId(), adherenceRecord.treatmentId(), adherenceRecord.doseDate());
        log.providerId(adherenceRecord.providerId());
        log.tbId(adherenceRecord.tbId());
        log.status(adherenceRecord.status());
        return log;
    }

    public List<AdherenceLog> map(List<AdherenceRecord> adherenceRecord) {
        List<AdherenceLog> logs = new ArrayList();
        for (AdherenceRecord datum : adherenceRecord) {
            logs.add(map(datum));
        }
        return logs;
    }
}
