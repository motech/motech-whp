package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.user.domain.Provider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_NOT_REPORTED;
import static org.motechproject.whp.providerreminder.domain.ProviderReminderType.ADHERENCE_WINDOW_APPROACHING;

public class ProviderReminderServiceTest {

    public static final String URL = "url";
    public static final String UUID = "uuid";

    @Mock
    HttpClientService httpClientService;
    @Mock
    AdherenceSubmissionService adherenceSubmissionService;

    ProviderReminderService providerReminderService;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderService = new ProviderReminderService(adherenceSubmissionService, httpClientService);
    }

    @Test
    public void shouldRemindProvidersThroughIVR() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("providerId", phoneNumber, null, null));

        when(adherenceSubmissionService.providersToSubmitAdherence()).thenReturn(providers);
        providerReminderService.alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING, URL, UUID);
        verify(httpClientService).post(URL, new ProviderReminderRequest(ADHERENCE_WINDOW_APPROACHING, asList(phoneNumber), UUID).toXML());
    }

    @Test
    public void shouldNotRaiseRequestWhenNoProviderNeedsToSubmitAdherence() {
        when(adherenceSubmissionService.providersToSubmitAdherence()).thenReturn(null);
        providerReminderService.alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_APPROACHING, URL, UUID);
        verifyZeroInteractions(httpClientService);
    }

    @Test
    public void shouldHandleEventForAdherenceNotReportedAlert() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("providerId", phoneNumber, null, null));
        TreatmentWeek treatmentWeek = currentAdherenceCaptureWeek();

        when(adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate(), treatmentWeek.endDate())).thenReturn(providers);
        providerReminderService.alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED, URL, UUID);
        verify(httpClientService).post(URL, new ProviderReminderRequest(ADHERENCE_NOT_REPORTED, asList(phoneNumber), UUID).toXML());
    }

    @Test
    public void shouldNotRaiseRequestWhenNoProviderIsPendingAdherence() {
        TreatmentWeek treatmentWeek = currentAdherenceCaptureWeek();

        when(adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate(), treatmentWeek.endDate())).thenReturn(null);
        providerReminderService.alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED, URL, UUID);
        verifyZeroInteractions(httpClientService);
    }
}
