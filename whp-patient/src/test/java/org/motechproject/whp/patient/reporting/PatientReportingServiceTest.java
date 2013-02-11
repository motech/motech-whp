package org.motechproject.whp.patient.reporting;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.patient.PatientDTO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientReportingServiceTest {

    PatientReportingService patientReportingService;

    @Mock
    PatientReportingRequestMapper patientReportingRequestMapper;
    @Mock
    ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp() {
        initMocks(this);
        patientReportingService = new PatientReportingService(patientReportingRequestMapper, reportingPublisherService);
    }

    @Test
    public void shouldReportPatientUpdates() {
        Patient patient = mock(Patient.class);
        PatientDTO patientDTO = mock(PatientDTO.class);

        when(patientReportingRequestMapper.mapToReportingRequest(patient)).thenReturn(patientDTO);

        patientReportingService.reportPatient(patient);

        verify(patientReportingRequestMapper).mapToReportingRequest(patient);
        verify(reportingPublisherService).reportPatient(patientDTO);
    }

}
