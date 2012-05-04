package org.motechproject.whp.adherence.mapping;

import org.joda.time.DateTime;
import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceLog;

public class AdherenceRequestMapper {

    String patientId;
    AdherenceLog log;

    public AdherenceRequestMapper(String patientId, AdherenceLog log) {
        this.patientId = patientId;
        this.log = log;
    }

    public RecordAdherenceRequest request() {
        RecordAdherenceRequest request = new RecordAdherenceRequest(patientId, null, logDateTime());
        request.dosesTaken(numberOfDosesTaken());
        request.dosesMissed(numberOfDosesMissed());
        return request;
    }

    private DateTime logDateTime() {
        return DateUtil.newDateTime(log.getPillDate());
    }

    private int numberOfDosesMissed() {
        return log.getIsNotTaken() ? 1 : 0;
    }

    private int numberOfDosesTaken() {
        return log.getIsTaken() ? 1 : 0;
    }
}
