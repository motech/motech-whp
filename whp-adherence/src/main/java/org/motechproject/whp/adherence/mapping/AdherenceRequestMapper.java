package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.patient.domain.Patient;

public class AdherenceRequestMapper {

    AdherenceLog log;
    private Patient patient;

    public AdherenceRequestMapper(Patient patient, AdherenceLog log) {
        this.patient = patient;
        this.log = log;
    }

    public AdherenceData request() {
        AdherenceData request = new AdherenceData(patient.getPatientId(), null, log.getPillDate());
        request.status(log.getPillStatus().getStatus());
        request.addMeta("tb_id", patient.tbId());
        return request;
    }

}
