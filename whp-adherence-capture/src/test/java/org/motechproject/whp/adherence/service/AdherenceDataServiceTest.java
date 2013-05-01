package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.model.PatientAdherenceStatus;
import org.motechproject.whp.patient.repository.AllPatients;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class AdherenceDataServiceTest {
    @Mock
    private AllPatients allPatients;

    private AdherenceDataService adherenceDataService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceDataService = new AdherenceDataService(allPatients);
    }


    @Test
    public void shouldReturnAdherenceSummaryByProviderId() {
        String providerId = "providerId";
        LocalDate currentAdherenceReportWeekStartDate = currentAdherenceCaptureWeek().startDate();
        PatientAdherenceStatus patientAdherenceStatus1 = new PatientAdherenceStatus("patient1", currentAdherenceReportWeekStartDate);
        PatientAdherenceStatus patientAdherenceStatus2 = new PatientAdherenceStatus("patient2", currentAdherenceReportWeekStartDate);

        List<PatientAdherenceStatus> patients = asList(patientAdherenceStatus1,patientAdherenceStatus2);

        when(allPatients.getPatientAdherenceStatusesFor(providerId)).thenReturn(patients);

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);
        AdherenceSummaryByProvider expectedSummary = new AdherenceSummaryByProvider(providerId, patients);
        assertThat(adherenceSummary, is(expectedSummary));
    }
}
