package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.DosageLog;
import org.motechproject.whp.adherence.repository.AllDosageLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdherenceService {

    private AllDosageLogs allDosageLogs;

    @Autowired
    public AdherenceService(AllDosageLogs allDosageLogs) {
        this.allDosageLogs = allDosageLogs;
    }

    public DosageLog recordAdherence(String patientId, LocalDate startDate, LocalDate endDate, int doseTakenCount, int idealDoseCount, Map<String, String> metaData) {
        DosageLog dosageLog = new DosageLog(patientId, startDate, endDate, doseTakenCount, idealDoseCount, metaData);
        allDosageLogs.add(dosageLog);
        return dosageLog;
    }

    public List<DosageLog> getDosageLogs(String patientId, LocalDate fromDate, LocalDate toDate) {
        return allDosageLogs.findAllByPatientIdAndDateRange(patientId, fromDate, toDate);
    }

}
