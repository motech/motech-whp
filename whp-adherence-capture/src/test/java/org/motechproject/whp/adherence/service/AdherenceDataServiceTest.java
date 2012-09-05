package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class AdherenceDataServiceTest {

    @Mock
    private WHPAdherenceService whpAdherenceService;
    @Mock
    private AllProviders allProviders;
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
        String patientId1 = "patient1";
        String patientId2 = "patient2";
        LocalDate currentAdherenceReportWeekStartDate = currentAdherenceCaptureWeek().startDate();
        Patient patient1 = new PatientBuilder().withDefaults().withPatientId(patientId1).withProviderId(providerId).withAdherenceProvidedForLastWeek().build();
        Patient patient2 = new PatientBuilder().withDefaults().withPatientId(patientId2).withProviderId(providerId).withAdherenceProvidedForLastWeek().build();
        List<Patient> patients = asList(patient1,patient2);

        when(allPatients.getAllWithActiveTreatmentFor(providerId)).thenReturn(patients);

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);
        AdherenceSummaryByProvider expectedSummary = new AdherenceSummaryByProvider(providerId, asList(patient1, patient2));
        assertThat(adherenceSummary, is(expectedSummary));
    }
}
