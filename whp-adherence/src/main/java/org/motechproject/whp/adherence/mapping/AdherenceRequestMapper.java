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

    public RecordAdherenceRequest request() {
        RecordAdherenceRequest request = new RecordAdherenceRequest(patientId, null, log.getPillDate());
        request.dosesTaken(numberOfDosesTaken());
        request.dosesMissed(numberOfDosesMissed());
        return request;
    }

    private int numberOfDosesMissed() {
        return log.getIsNotTaken() ? 1 : 0;
    }

    private int numberOfDosesTaken() {
        return log.getIsTaken() ? 1 : 0;
    }
}
