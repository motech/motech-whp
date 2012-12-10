package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.PhoneNumber;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderReminderServiceTest {

    ProviderReminderService providerReminderService;

    @Mock
    PatientService patientService;
    @Mock
    ProviderService providerService;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderService = new ProviderReminderService(providerService, patientService);
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

}
