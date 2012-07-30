package org.motechproject.whp.adherence.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.common.domain.TreatmentWeek;
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
        String primaryMobile = "primaryMobile";
        String providerId = "providerId";
        String patientId1 = "patient1";
        String patientId2 = "patient2";
        List<Patient> patients = asList(
                new PatientBuilder().withDefaults().withPatientId(patientId1).withProviderId(providerId).build(),
                new PatientBuilder().withDefaults().withPatientId(patientId2).withProviderId(providerId).build()
        );
        Provider provider = ProviderBuilder.newProviderBuilder().withDefaults().withProviderId(providerId).withPrimaryMobileNumber(primaryMobile).build();

        when(allProviders.findByMobileNumber(primaryMobile)).thenReturn(provider);
        when(allPatients.getAllWithActiveTreatmentFor(providerId)).thenReturn(patients);
        when(whpAdherenceService.patientsWithAdherence(eq(providerId), any(TreatmentWeek.class))).thenReturn(asList(patientId1, patientId2));

        AdherenceSummaryByProvider adherenceSummary = adherenceDataService.getAdherenceSummary(providerId);
        AdherenceSummaryByProvider expectedSummary = new AdherenceSummaryByProvider(providerId, asList(patientId1, patientId2), asList(patientId1, patientId2));
        assertThat(adherenceSummary, is(expectedSummary));
    }
}
