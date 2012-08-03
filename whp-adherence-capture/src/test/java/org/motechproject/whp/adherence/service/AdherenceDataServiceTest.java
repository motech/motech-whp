package org.motechproject.whp.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.ProviderBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

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
        adherenceDataService = new AdherenceDataService(whpAdherenceService, allPatients);
    }


    @Test
    public void shouldReturnAdherenceSummaryByProviderId() {
        String providerId = "providerId";
        String patientId1 = "patient1";
        String patientId2 = "patient2";
        LocalDate currentAdherenceReportWeekStartDate = currentWeekInstance().startDate();
        Patient patient1 = new PatientBuilder().withDefaults().withPatientId(patientId1).withProviderId(providerId).withLastAdherenceProvidedWeekStartDate(currentAdherenceReportWeekStartDate).build();
        Patient patient2 = new PatientBuilder().withDefaults().withPatientId(patientId2).withProviderId(providerId).withLastAdherenceProvidedWeekStartDate(currentAdherenceReportWeekStartDate).build();
        List<Patient> patients = asList(patient1,patient2);

        when(allPatients.getAllWithActiveTreatmentFor(providerId)).thenReturn(patients);

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);
        AdherenceSummaryByProvider expectedSummary = new AdherenceSummaryByProvider(providerId, asList(patient1, patient2));
        assertThat(adherenceSummary, is(expectedSummary));
    }
}
