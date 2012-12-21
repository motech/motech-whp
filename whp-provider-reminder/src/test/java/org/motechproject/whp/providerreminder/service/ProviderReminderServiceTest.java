package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;

public class ProviderReminderServiceTest {

    ProviderReminderService providerReminderService;

    @Mock
    PatientService patientService;
    @Mock
    ProviderService providerService;

    @Mock
    AdherenceSubmissionService adherenceSubmissionService;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderService = new ProviderReminderService(providerService, patientService, adherenceSubmissionService);
    }

    @Test
    public void shouldReturnActiveProviderMSISDNs() {
        ProviderIds providerIds = new ProviderIds(asList("providerId1", "providerId2"));
        when(patientService.providersWithActivePatients()).thenReturn(providerIds);
        when(providerService.findByProviderIds(providerIds)).thenReturn(asList(new Provider("providerId1", "msisdn1", null, null), new Provider("providerId2", "msisdn2", null, null)));

        List<String> phoneNumberList =  providerReminderService.getActiveProviderPhoneNumbers();

        List<String> expectedPhoneNumberList = asList("msisdn1", "msisdn2");
        assertEquals(expectedPhoneNumberList, phoneNumberList);
    }

    @Test
    public void shouldReturnProvidersWithPendingAdherence() {
        Provider provider1 = newProviderBuilder().withDefaults().withPrimaryMobileNumber("msisdn1").build();
        Provider provider2 = newProviderBuilder().withDefaults().withPrimaryMobileNumber("msisdn2").build();

        TreatmentWeek treatmentWeek = TreatmentWeekInstance.currentAdherenceCaptureWeek();

        when(adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate(), treatmentWeek.endDate()))
                .thenReturn(asList(provider1, provider2));

        List<String> phoneNumberList =  providerReminderService.getProviderPhoneNumbersWithPendingAdherence();

        List<String> expectedPhoneNumberList = asList(provider1.getPrimaryMobile(), provider2.getPrimaryMobile());
        assertEquals(expectedPhoneNumberList, phoneNumberList);
    }

}
