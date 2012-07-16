package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.AdherenceLogMapper;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceService {

    private AllAdherenceLogs allAdherenceLogs;

    @Autowired
    public AdherenceService(AllAdherenceLogs allAdherenceLogs) {
        this.allAdherenceLogs = allAdherenceLogs;
    }

    public void saveOrUpdateAdherence(List<AdherenceRecord> record) {
        for (AdherenceRecord adherenceRecord : record) {
            AdherenceLog adherenceLog = new AdherenceLog(adherenceRecord.externalId(), adherenceRecord.treatmentId(), adherenceRecord.doseDate());
            adherenceLog.status(adherenceRecord.status());
            adherenceLog.meta(adherenceRecord.meta());
            allAdherenceLogs.add(adherenceLog);
        }
    }

    public List<AdherenceRecord> adherence(LocalDate asOf, int pageNumber, int pageSize) {
        return allAdherenceLogs.findLogsAsOf(asOf, pageNumber, pageSize);
    }

    public List<AdherenceRecord> adherence(String externalId, String treatmentId, LocalDate startDate, LocalDate endDate) {
        return allAdherenceLogs.findLogsInRange(externalId, treatmentId, startDate, endDate);
    }

    public void addOrUpdateLogsByDoseDate(List<AdherenceRecord> adherenceRecord, String externalId) {
        List<AdherenceLog> adherenceLogs = new AdherenceLogMapper().map(adherenceRecord);
        allAdherenceLogs.addOrUpdateLogsForExternalIdByDoseDate(adherenceLogs, externalId);
    }

    public int countOfDosesTakenBetween(String patientId, String treatmentId, LocalDate from, LocalDate to) {
        return allAdherenceLogs.countOfDosesTakenBetween(patientId, treatmentId, from, to);
    }

    public List<AdherenceRecord> allTakenLogsFrom(String patientId, String treatmentId, LocalDate startDate) {
        return allAdherenceLogs.allTakenLogsFrom(patientId, treatmentId, startDate);
    }
}
