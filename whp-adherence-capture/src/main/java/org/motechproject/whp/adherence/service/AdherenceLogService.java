package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.mapping.AdherenceLogMapper;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.user.domain.ProviderIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceLogService {

    private AllAdherenceLogs allAdherenceLogs;

    @Autowired
    public AdherenceLogService(AllAdherenceLogs allAdherenceLogs) {
        this.allAdherenceLogs = allAdherenceLogs;
    }

    public void saveOrUpdateAdherence(List<AdherenceRecord> record) {
        for (AdherenceRecord adherenceRecord : record) {
            AdherenceLog adherenceLog = new AdherenceLog(adherenceRecord.externalId(), adherenceRecord.treatmentId(), adherenceRecord.doseDate());
            adherenceLog.status(adherenceRecord.status());
            adherenceLog.tbId(adherenceRecord.tbId());
            adherenceLog.providerId(adherenceRecord.providerId());
            adherenceLog.district(adherenceRecord.district());
            allAdherenceLogs.add(adherenceLog);
        }
    }

    public List<AdherenceRecord> adherence(LocalDate asOf, int pageNumber, int pageSize) {
        return allAdherenceLogs.findLogsInRange(asOf.minusMonths(3), asOf, pageNumber, pageSize);
    }

    public ProviderIds providersWithAdherence(String district, LocalDate from, LocalDate to) {
        return allAdherenceLogs.withKnownAdherenceReportedByProviders(district, from, to);
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

    public List<AdherenceRecord> allTakenLogs(String patientId, String treatmentId) {
        return allAdherenceLogs.allTakenLogs(patientId, treatmentId);
    }

    public ProviderIds providersWithAdherence(LocalDate from, LocalDate to) {
        return allAdherenceLogs.findProvidersWithAdherence(from, to);
    }
}
