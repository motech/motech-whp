package org.motechproject.whp.patient.reporting;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.patient.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientReportingService {

    private final PatientReportingRequestMapper patientReportingRequestMapper;
    private final ReportingPublisherService reportingPublisherService;

    @Autowired
    public PatientReportingService(PatientReportingRequestMapper patientReportingRequestMapper, ReportingPublisherService reportingPublisherService) {
        this.patientReportingRequestMapper = patientReportingRequestMapper;
        this.reportingPublisherService = reportingPublisherService;
    }

    public void reportPatient(Patient patient) {
        reportingPublisherService.reportPatient(populatePatientDTO(patient));
    }

    public void removePatient(Patient patient) {
        reportingPublisherService.removePatient(populatePatientDTO(patient));
    }

    private PatientDTO populatePatientDTO(Patient patient) {
        PatientDTO patientDTO = patientReportingRequestMapper.mapToReportingRequest(patient);
        return patientDTO;
    }
}
