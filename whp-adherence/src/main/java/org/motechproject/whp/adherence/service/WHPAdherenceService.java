package org.motechproject.whp.adherence.service;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.AdherenceAuditService;
import org.motechproject.whp.adherence.domain.*;
import org.motechproject.whp.adherence.mapping.AdherenceDataMapper;
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.mapping.WeeklyAdherenceMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void recordAdherence(String patientId, WeeklyAdherence weeklyAdherence, String user, AdherenceSource source) {
        Patient patient = allPatients.findByPatientId(patientId);
        updateMetaData(weeklyAdherence, patient);

        List<AdherenceData> requests = requests(weeklyAdherence);
        adherenceService.saveOrUpdateAdherence(user, source.name(), requests.toArray(new AdherenceData[requests.size()]));

        if (shouldStartOrRestartTreatment(patient, weeklyAdherence)) {
            patientService.startTreatment(patientId, weeklyAdherence.firstDoseTakenOn()); //implicitly sets doseStartedOn to null if no dose has been taken. this is intended.
        }

        adherenceAuditService.log(user, weeklyAdherence);
    }

    private void updateMetaData(WeeklyAdherence weeklyAdherence, Patient patient) {
        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(AdherenceConstants.TB_ID, patient.tbId());
        meta.put(AdherenceConstants.PROVIDER_ID, patient.providerId());
        weeklyAdherence.setMetaData(meta);
    }

    public WeeklyAdherence currentWeekAdherence(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();
        AdherenceRecords adherenceRecords = adherenceService.adherenceRecords(patient.getPatientId(), patient.currentTreatmentId(), treatmentWeek.startDate(), treatmentWeek.endDate());

        if (adherenceRecords.size() > 0) {
            return new WeeklyAdherenceMapper(treatmentWeek, adherenceRecords).map();
        } else {
            return null;
        }
    }

    public List<Adherence> allAdherenceData(int pageNumber, int pageSize) {
        List<AdherenceData> adherenceData = adherenceService.adherenceLogs(DateUtil.today(), pageNumber, pageSize);
        return new AdherenceMapper().map(adherenceData);
    }

    public WeeklyAdherence currentWeekAdherenceTemplate(Patient patient) {
        TreatmentWeek treatmentWeek = currentWeekInstance();

        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(AdherenceConstants.TB_ID, patient.getCurrentProvidedTreatment().getTbId());
        meta.put(AdherenceConstants.PROVIDER_ID, patient.getCurrentProvidedTreatment().getProviderId());

        return new WeeklyAdherence(patient.getPatientId(), patient.currentTreatmentId(), treatmentWeek, pillDays(patient), meta);
    }

    private List<DayOfWeek> pillDays(Patient patient) {
        return patient.getCurrentProvidedTreatment().getTreatment().getTreatmentCategory().getPillDays();
    }

    private List<AdherenceData> requests(WeeklyAdherence weeklyAdherence) {
        List<AdherenceData> requests = new ArrayList<AdherenceData>();

        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            requests.add(AdherenceDataMapper.request(adherence));
        }
        return requests;
    }
}
