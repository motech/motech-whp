package org.motechproject.whp.adherence.service;

import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.mapping.AdherenceDataMapper;
import org.motechproject.whp.adherence.mapping.WeeklyAdherenceMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentStartCriteria;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WHPAdherenceService {

    AllPatients allPatients;
    AdherenceService adherenceService;
    TreatmentStartCriteria treatmentStartCriteria;
    PatientService patientService;

    @Autowired
    public WHPAdherenceService(AdherenceService adherenceService,
                               AllPatients allPatients,
                               TreatmentStartCriteria treatmentStartCriteria,
                               PatientService patientService) {
        this.adherenceService = adherenceService;
        this.allPatients = allPatients;
        this.treatmentStartCriteria = treatmentStartCriteria;
        this.patientService = patientService;
    }

    public void recordAdherence(String patientId, WeeklyAdherence weeklyAdherence, String user, AdherenceSource source) {
        for (AdherenceData request : requests(weeklyAdherence)) {
            adherenceService.recordAdherence(user, source.name(), request);
        }
        if (treatmentStartCriteria.shouldStartTreatment(patientId, weeklyAdherence)) {
            patientService.startOnTreatment(patientId, weeklyAdherence.firstDoseTakenOn());
        }
    }

    public WeeklyAdherence currentWeekAdherence(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        TreatmentWeek treatmentWeek = new TreatmentWeek(DateUtil.today().minusWeeks(1));
        AdherenceRecords adherenceRecords = adherenceService.adherenceRecords(patientId, patient.currentTreatmentId(), treatmentWeek.startDate(), treatmentWeek.endDate());

        if (adherenceRecords.size() > 0) {
            return new WeeklyAdherenceMapper(treatmentWeek, adherenceRecords).map();
        } else {
            return currentWeekAdherece(patient, treatmentWeek);
        }
    }

    private WeeklyAdherence currentWeekAdherece(Patient patient, TreatmentWeek treatmentWeek) {
        WeeklyAdherence weeklyAdherence = new WeeklyAdherence(patient.getPatientId(), patient.currentTreatmentId(), treatmentWeek, pillDays(patient));
        weeklyAdherence.setProviderId(patient.getCurrentProvidedTreatment().getProviderId());
        weeklyAdherence.setTbId(patient.getCurrentProvidedTreatment().getTbId());
        return weeklyAdherence;
    }

    private List<DayOfWeek> pillDays(Patient patient) {
        return patient.getCurrentProvidedTreatment().getTreatment().getTreatmentCategory().getPillDays();
    }

    private List<AdherenceData> requests(WeeklyAdherence weeklyAdherence) {
        List<AdherenceData> requests = new ArrayList<AdherenceData>();
        for (Adherence adherence : weeklyAdherence.getAdherenceLogs()) {
            requests.add(new AdherenceDataMapper(adherence).request());
        }
        return requests;
    }

}
