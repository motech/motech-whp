package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.whp.adherence.domain.AdherenceLog;

public class AdherenceRequestMapper {

    String patientId;
    AdherenceLog log;

    public AdherenceRequestMapper(String patientId, AdherenceLog log) {
        this.patientId = patientId;
        this.log = log;
    }

    public RecordAdherenceRequest map() {
        RecordAdherenceRequest request = new RecordAdherenceRequest(patientId, null, log.getPillDate());
        request.status(log.getPillStatus().getStatus());
        return request;
    }

}
