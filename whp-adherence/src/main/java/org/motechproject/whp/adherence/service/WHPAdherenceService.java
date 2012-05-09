package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.mapping.AdherenceRequestMapper;
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

    public void recordAdherence(String patientId, Adherence adherence) {
        for (RecordAdherenceRequest request : recordAdherenceRequests(patientId, adherence)) {
            adherenceService.recordAdherence(request);
        }
        if (treatmentStartCriteria.shouldStartTreatment(patientId, adherence)) {
            patientService.startOnTreatment(patientId, adherence.firstDoseTakenOn());
        }
    }

    public Adherence currentWeekAdherence(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        TreatmentWeek treatmentWeek = new TreatmentWeek(DateUtil.today().minusWeeks(1));
        AdherenceRecords adherenceRecords = adherenceService.adherenceRecords(patientId, null, treatmentWeek.startDate(), treatmentWeek.endDate());

        if (adherenceRecords.size() > 0) {
            return new AdherenceMapper(adherenceRecords).map();
        }
        return new Adherence(treatmentWeek, patient.getCurrentProvidedTreatment());
    }

    public Adherence adherenceAsOf(String patientId, LocalDate asOf) {
        AdherenceRecords adherenceRecords = adherenceService.adherenceRecords(patientId, null, asOf);
        return new AdherenceMapper(adherenceRecords).map();
    }

    private List<RecordAdherenceRequest> recordAdherenceRequests(String patientId, Adherence adherence) {
        List<RecordAdherenceRequest> requests = new ArrayList<RecordAdherenceRequest>();
        for (AdherenceLog adherenceLog : adherence.getAdherenceLogs()) {
            requests.add(new AdherenceRequestMapper(patientId, adherenceLog).map());
        }
        return requests;
    }

}
