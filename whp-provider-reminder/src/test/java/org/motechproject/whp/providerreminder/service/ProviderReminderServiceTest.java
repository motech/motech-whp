package org.motechproject.whp.providerreminder.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.schedule.domain.ScheduleType;
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
        providerReminderService.alertProvidersWithActivePatients(ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        verify(alertService).raiseIVRRequest(providers, ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
    }

    @Test
    public void shouldNotRaiseRequestWhenNoProviderNeedsToSubmitAdherence() {
        when(adherenceSubmissionService.providersToSubmitAdherence()).thenReturn(null);
        providerReminderService.alertProvidersWithActivePatients(ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        verifyZeroInteractions(alertService);
    }

    @Test
    public void shouldHandleEventForAdherenceNotReportedAlert() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("providerId", phoneNumber, null, null));
        TreatmentWeek treatmentWeek = currentAdherenceCaptureWeek();

        when(adherenceSubmissionService.providersPendingAdherence(treatmentWeek.startDate())).thenReturn(providers);
        providerReminderService.alertProvidersPendingAdherence(ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED);
        verify(alertService).raiseIVRRequest(providers, ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED);
    }

}
