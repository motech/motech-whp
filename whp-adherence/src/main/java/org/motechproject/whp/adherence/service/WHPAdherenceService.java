package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.AdherenceAuditService;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.mapping.AdherenceListMapper;
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.mapping.AdherenceRecordMapper;
import org.motechproject.whp.adherence.mapping.WeeklyAdherenceSummaryMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;
import static org.motechproject.whp.patient.domain.TreatmentStartCriteria.shouldStartOrRestartTreatment;

@Service
public class WHPAdherenceService {

    private AllPatients allPatients;
    private AdherenceService adherenceService;
    private PatientService patientService;
    private AdherenceAuditService adherenceAuditService;

    @Autowired
    public WHPAdherenceService(AdherenceService adherenceService,
                               AllPatients allPatients,
                               PatientService patientService,
                               AdherenceAuditService adherenceAuditService
    ) {
        this.adherenceService = adherenceService;
        this.allPatients = allPatients;
        this.patientService = patientService;
        this.adherenceAuditService = adherenceAuditService;
    }

    public void recordAdherence(WeeklyAdherenceSummary weeklyAdherenceSummary, AuditParams auditParams) {
        Patient patient = allPatients.findByPatientId(weeklyAdherenceSummary.getPatientId());

        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        adherenceService.saveOrUpdateAdherence(AdherenceRecordMapper.map(adherenceList));

        if (shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patientService.startTherapy(patient.getPatientId(), adherenceList.firstDoseTakenOn());
        }
        adherenceAuditService.log(patient, weeklyAdherenceSummary, auditParams);
    }

    public WeeklyAdherenceSummary currentWeekAdherence(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        List<AdherenceRecord> adherenceRecords = adherenceService.adherence(
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
        List<AdherenceRecord> adherenceData = adherenceService.adherence(DateUtil.today(), pageNumber, pageSize);
        return new AdherenceMapper().map(adherenceData);
    }

    public void addOrUpdateLogsByDoseDate(List<Adherence> adherenceList, String patientId) {
        List<AdherenceRecord> adherenceData = AdherenceRecordMapper.map(adherenceList);
        adherenceService.addOrUpdateLogsByDoseDate(adherenceData, patientId);
    }

    public List<Adherence> findLogsInRange(String patientId, String treatmentId, LocalDate start, LocalDate end) {
        List<AdherenceRecord> adherenceData = adherenceService.adherence(patientId, treatmentId, start, end);
        return new AdherenceMapper().map(adherenceData);
    }

}
