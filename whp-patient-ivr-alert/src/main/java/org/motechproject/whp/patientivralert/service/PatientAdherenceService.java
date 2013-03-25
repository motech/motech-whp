package org.motechproject.whp.patientivralert.service;

import org.motechproject.whp.patientivralert.mapper.PatientAdherenceSummaryMapper;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.reporting.service.ReportingDataService;
import org.motechproject.whp.reports.contract.query.PatientAdherenceSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAdherenceService {

    private ReportingDataService reportingDataService;
    private PatientAdherenceSummaryMapper patientAdherenceSummaryMapper;

    @Autowired
    public PatientAdherenceService(ReportingDataService reportingDataService, PatientAdherenceSummaryMapper patientAdherenceSummaryMapper) {
        this.reportingDataService = reportingDataService;
        this.patientAdherenceSummaryMapper = patientAdherenceSummaryMapper;
    }

    public List<PatientAdherenceRecord> getPatientsWithoutAdherence(int skip, int limit) {
        List<PatientAdherenceSummary> patientsWithMissingAdherence = reportingDataService.getPatientsWithMissingAdherence(skip, limit);
        return patientAdherenceSummaryMapper.map(patientsWithMissingAdherence);
    }
}
