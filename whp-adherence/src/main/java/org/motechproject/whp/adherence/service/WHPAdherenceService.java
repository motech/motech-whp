package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.mapping.AdherenceRecordMapper;
import org.motechproject.whp.adherence.mapping.AdherenceRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;
import static org.motechproject.util.DateUtil.newDateTime;

@Service
public class WHPAdherenceService {

    AdherenceService adherenceService;

    @Autowired
    public WHPAdherenceService(AdherenceService adherenceService) {
        this.adherenceService = adherenceService;
    }

    public void recordAdherence(String patientId, Adherence logs) {
        for (RecordAdherenceRequest request : recordAdherenceRequests(patientId, logs)) {
            adherenceService.recordAdherence(request);
        }
    }

    public Adherence adherenceAsOf(String patientId, LocalDate asOf) {
        AdherenceRecords adherenceRecords = adherenceService.adherenceRecords(patientId, null, newDateTime(asOf));
        return adherenceLogs(adherenceRecords);
    }

    private Adherence adherenceLogs(AdherenceRecords adherenceRecords) {
        Adherence logs = new Adherence();
        for (AdherenceRecord record : adherenceRecords.adherenceRecords()) {
            logs.addAdherenceLog(new AdherenceRecordMapper(record).adherenceLog());
        }
        return logs;
    }

    private List<RecordAdherenceRequest> recordAdherenceRequests(String patientId, Adherence logs) {
        List<RecordAdherenceRequest> requests = new ArrayList<RecordAdherenceRequest>();
        for (AdherenceLog log : logs.getAdherenceLogs()) {
            requests.add(new AdherenceRequestMapper(patientId, log).request());
        }
        return requests;
    }
}
