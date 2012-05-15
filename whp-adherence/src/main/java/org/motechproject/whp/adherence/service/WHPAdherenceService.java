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
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.mapping.WeeklyAdherenceMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentStartCriteria;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.adherence.domain.CurrentTreatmentWeek.currentWeekInstance;
import static org.motechproject.whp.patient.domain.TreatmentStartCriteria.shouldStartOrRestartTreatment;

@Service
public class WHPAdherenceService {

    AllPatients allPatients;
    AdherenceService adherenceService;
    PatientService patientService;

    @Autowired
    public WHPAdherenceService(AdherenceService adherenceService,
                               AllPatients allPatients,
                               PatientService patientService) {
        this.adherenceService = adherenceService;
        this.allPatients = allPatients;
        this.patientService = patientService;
    }

    public void recordAdherence(String patientId, WeeklyAdherence weeklyAdherence, String user, AdherenceSource source) {
        for (AdherenceData request : requests(weeklyAdherence)) {
            adherenceService.recordAdherence(user, source.name(), request);
        }
        if (shouldStartOrRestartTreatment(allPatients.findByPatientId(patientId), weeklyAdherence)) {
            patientService.startOnTreatment(patientId, weeklyAdherence.firstDoseTakenOn()); //implicitly sets doseStartedOn to null if no dose has been taken. this is intended.
        }
    }

    public WeeklyAdherence currentWeekAdherence(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        TreatmentWeek treatmentWeek = currentWeekInstance();
        AdherenceRecords adherenceRecords = adherenceService.adherenceRecords(patientId, patient.currentTreatmentId(), treatmentWeek.startDate(), treatmentWeek.endDate());

        if (adherenceRecords.size() > 0) {
            return new WeeklyAdherenceMapper(treatmentWeek, adherenceRecords).map();
        } else {
            return currentWeekAdherence(patient, treatmentWeek);
        }
    }

    public List<Adherence> allAdherenceData(int pageNumber, int pageSize) {
        List<AdherenceData> adherenceData = adherenceService.adherenceLogs(DateUtil.today(), pageNumber, pageSize);
        return new AdherenceMapper().map(adherenceData);
    }

    private WeeklyAdherence currentWeekAdherence(Patient patient, TreatmentWeek treatmentWeek) {
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
