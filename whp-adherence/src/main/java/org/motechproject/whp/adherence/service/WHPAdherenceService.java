package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.AdherenceAuditService;
import org.motechproject.whp.adherence.audit.AuditParams;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.mapping.AdherenceRecordMapper;
import org.motechproject.whp.adherence.mapping.WeeklyAdherenceMapper;
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

    AllPatients allPatients;
    AdherenceService adherenceService;
    PatientService patientService;
    AdherenceAuditService adherenceAuditService;

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

    public void recordAdherence(String patientId, WeeklyAdherence weeklyAdherence, AuditParams auditParams) {
        Patient patient = allPatients.findByPatientId(patientId);

        List<AdherenceRecord> requests = AdherenceRecordMapper.map(weeklyAdherence);
        adherenceService.saveOrUpdateAdherence(requests);
        if (shouldStartOrRestartTreatment(patient, weeklyAdherence)) {
            patientService.startTherapy(patientId, weeklyAdherence.firstDoseTakenOn());
        }
        adherenceAuditService.log(patient, weeklyAdherence, auditParams);

    }

    public WeeklyAdherence currentWeekAdherence(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        List<AdherenceRecord> adherenceRecords = adherenceService.adherence(
                patient.getPatientId(),
                patient.currentTherapyId(),
                treatmentWeek.startDate(),
                treatmentWeek.endDate());

        if (adherenceRecords.size() > 0) {
            return new WeeklyAdherenceMapper(patient, treatmentWeek).map(new AdherenceMapper().map(adherenceRecords));
        } else {
            return WeeklyAdherence.createAdherenceFor(patient);
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
