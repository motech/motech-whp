package org.motechproject.whp.patientivralert.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patientivralert.mapper.PatientAdherenceSummaryMapper;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.reporting.service.ReportingDataService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientAdherenceServiceTest {

    PatientAdherenceService patientAdherenceService;
    @Mock
    ReportingDataService reportingDataService;
    @Mock
    PatientAdherenceSummaryMapper patientAdherenceSummaryMapper;

    @Before
    public void setUp() {
        initMocks(this);
        patientAdherenceService = new PatientAdherenceService(reportingDataService, patientAdherenceSummaryMapper);
    }

    @Test
    public void shouldGetPatientAdherenceRecordsForGivenOffset() {
        int skip = 10;
        int limit = 100;

        List patientAdherenceSummaries = mock(List.class);
        List expectedPatientAdherenceRecords = mock(List.class);
        when(reportingDataService.getPatientsWithMissingAdherence(skip, limit)).thenReturn(patientAdherenceSummaries);
        when(patientAdherenceSummaryMapper.map(patientAdherenceSummaries)).thenReturn(expectedPatientAdherenceRecords);

        List<PatientAdherenceRecord> records =  patientAdherenceService.getPatientsWithoutAdherence(skip, limit);
        assertEquals(expectedPatientAdherenceRecords, records);
    }
}
