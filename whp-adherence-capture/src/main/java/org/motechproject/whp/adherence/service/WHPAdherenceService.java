package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.adherence.service.AdherenceLogService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.mapping.AdherenceRecordMapper;
import org.motechproject.whp.adherence.mapping.WeeklyAdherenceSummaryMapper;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

@Service
public class WHPAdherenceService {

    private AdherenceLogService adherenceLogService;
    private AdherenceAuditService adherenceAuditService;
    private AllAdherenceLogs allAdherenceLogs;

    @Autowired
    public WHPAdherenceService(AdherenceLogService adherenceLogService,
                               AdherenceAuditService adherenceAuditService,
                               AllAdherenceLogs allAdherenceLogs) {
        this.adherenceLogService = adherenceLogService;
        this.adherenceAuditService = adherenceAuditService;
        this.allAdherenceLogs = allAdherenceLogs;
    }

    public void recordWeeklyAdherence( AdherenceList adherenceList, WeeklyAdherenceSummary weeklyAdherenceSummary, Patient patient, AuditParams auditParams) {
        adherenceLogService.saveOrUpdateAdherence(AdherenceRecordMapper.map(adherenceList));
        adherenceAuditService.auditWeeklyAdherence(patient, weeklyAdherenceSummary, auditParams);
    }

    public WeeklyAdherenceSummary currentWeekAdherence(Patient patient) {
        TreatmentWeek treatmentWeek = currentAdherenceCaptureWeek();
        List<AdherenceRecord> adherenceRecords = adherenceLogService.adherence(
                patient.getPatientId(),
                patient.currentTherapyId(),
                treatmentWeek.startDate(),
                treatmentWeek.endDate());

        if (adherenceRecords.size() > 0) {
            return new WeeklyAdherenceSummaryMapper(patient, treatmentWeek).map(new AdherenceMapper().map(adherenceRecords));
        } else {
            return WeeklyAdherenceSummary.forFirstWeek(patient);
        }
    }

    public List<Adherence> allAdherenceData(int pageNumber, int pageSize) {
        List<AdherenceRecord> adherenceData = adherenceLogService.adherence(DateUtil.today(), pageNumber, pageSize);
        return new AdherenceMapper().map(adherenceData);
    }

    public void addOrUpdateLogsByDoseDate(List<Adherence> adherenceList, Patient patient, AuditParams auditParams) {
        List<AdherenceRecord> adherenceData = AdherenceRecordMapper.map(adherenceList);
        adherenceLogService.addOrUpdateLogsByDoseDate(adherenceData, patient.getPatientId());
        adherenceAuditService.auditDailyAdherence(patient, adherenceList, auditParams);
    }

    public AdherenceList findLogsInRange(String patientId, String treatmentId, LocalDate start, LocalDate end) {
        List<AdherenceRecord> adherenceData = new ArrayList<>();
        if (start != null && WHPDateUtil.isOnOrBefore(start, end)) {
            adherenceData = adherenceLogService.adherence(patientId, treatmentId, start, end);
        }
        return new AdherenceMapper().map(adherenceData);
    }

    public int countOfDosesTakenBetween(String patientId, String therapyUid, LocalDate startDate, LocalDate endDate) {
        if (!endDate.isBefore(startDate)) {
            return adherenceLogService.countOfDosesTakenBetween(patientId, therapyUid, startDate, endDate);
        } else {
            return 0;
        }
    }

    public void recordDailyAdherence(List<DailyAdherenceRequest> dailyAdherenceRequests, Patient patient, AuditParams auditParams) {
        List<Adherence> adherenceData = new ArrayList<>();

        for (DailyAdherenceRequest request : dailyAdherenceRequests) {
            Adherence datum = new Adherence();
            datum.setPatientId(patient.getPatientId());
            datum.setTreatmentId(patient.currentTherapyId());
            datum.setPillDate(request.getDoseDate());
            datum.setPillStatus(PillStatus.get(request.getPillStatus()));
            adherenceData.add(datum);

            Treatment doseForTreatment = patient.getTreatment(request.getDoseDate());
            if (doseForTreatment != null) {
                datum.setTbId(doseForTreatment.getTbId());
                datum.setProviderId(doseForTreatment.getProviderId());
            } else {
                datum.setTbId(WHPConstants.UNKNOWN);
                datum.setProviderId(WHPConstants.UNKNOWN);
            }
        }

        addOrUpdateLogsByDoseDate(adherenceData, patient, auditParams);
    }

    public AdherenceRecord nThTakenDose(String patientId, String therapyUid, Integer doseNumber, LocalDate startDate) {
        List<AdherenceRecord> adherenceRecords = adherenceLogService.allTakenLogsFrom(patientId, therapyUid, startDate);
        return adherenceRecords.get(doseNumber - 1);
    }

    public HashMap<LocalDate, PillStatus> getDateAdherenceMap(Patient patient) {
        AdherenceList logs = findLogsInRange(patient.getPatientId(), patient.currentTherapyId(), patient.getCurrentTherapy().getStartDate(), today());
        HashMap<LocalDate, PillStatus> datePillStatusHashMap = new HashMap<>();
        for (Adherence log : logs) {
            datePillStatusHashMap.put(log.getPillDate(), log.getPillStatus());
        }
        return datePillStatusHashMap;
    }

    public List<String> patientsWithAdherence(String providerId, TreatmentWeek week) {
        return allAdherenceLogs.findPatientsWithAdherence(providerId, week.startDate(), week.endDate());
    }

    public AdherenceList getAdherenceSortedByDate(String patientId, String therapyUid) {
        List<AdherenceRecord> adherenceRecords = adherenceLogService.allTakenLogs(patientId, therapyUid);
        return new AdherenceMapper().map(adherenceRecords);
    }

}
