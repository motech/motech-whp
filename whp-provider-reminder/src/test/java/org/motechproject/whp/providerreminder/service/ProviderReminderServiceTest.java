package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.ivr.ProviderAlertService;
import org.motechproject.whp.user.domain.Provider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class ProviderReminderServiceTest {

    @Mock
    AdherenceSubmissionService adherenceSubmissionService;
    @Mock
    ProviderAlertService alertService;

    ProviderReminderService providerReminderService;

    @Before
    public void setUp() {
        initMocks(this);
        providerReminderService = new ProviderReminderService(adherenceSubmissionService, alertService);
    }

    @Test
    public void shouldRemindProvidersThroughIVR() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("providerId", phoneNumber, null, null));

        when(adherenceSubmissionService.providersToSubmitAdherence()).thenReturn(providers);
        providerReminderService.alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        verify(alertService).raiseIVRRequest(providers, ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
    }

    @Test
    public void shouldNotRaiseRequestWhenNoProviderNeedsToSubmitAdherence() {
        when(adherenceSubmissionService.providersToSubmitAdherence()).thenReturn(null);
        providerReminderService.alertProvidersWithActivePatients(ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        verifyZeroInteractions(alertService);
    }

    @Test
    public void shouldHandleEventForAdherenceNotReportedAlert() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("providerId", phoneNumber, null, null));
        TreatmentWeek treatmentWeek = currentAdherenceCaptureWeek();

        when(adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate(), treatmentWeek.endDate())).thenReturn(providers);
        providerReminderService.alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED);
        verify(alertService).raiseIVRRequest(providers, ProviderReminderType.ADHERENCE_NOT_REPORTED);
    }

    @Test
    public void shouldNotRaiseRequestWhenNoProviderIsPendingAdherence() {
        TreatmentWeek treatmentWeek = currentAdherenceCaptureWeek();

        when(adherenceSubmissionService.providersWithoutAnyAdherence(treatmentWeek.startDate(), treatmentWeek.endDate())).thenReturn(null);
        providerReminderService.alertProvidersPendingAdherence(ProviderReminderType.ADHERENCE_NOT_REPORTED);
        verifyZeroInteractions(alertService);
    }
}
