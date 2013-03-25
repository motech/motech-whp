package org.motechproject.whp.providerreminder.ivr;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.schedule.domain.ScheduleType;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderAlertServiceTest {

    public static final String UUID = "uuid";
    public static final String IVRUrl = "some wgn url";

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private ProviderReminderRequestProperties requestProperties;
    @Mock
    private WGNGateway gateway;

    ProviderAlertService alertService;

    @Before
    public void setup() {
        initMocks(this);
        when(requestProperties.getProviderReminderUrl()).thenReturn(IVRUrl);
        when(requestProperties.getBatchSize()).thenReturn(1);
        when(uuidGenerator.uuid()).thenReturn(UUID);
        alertService = new ProviderAlertService(uuidGenerator, requestProperties, gateway);
    }

    @Test
    public void shouldRaiseRequestForProviderMobileNumbers() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("", phoneNumber, "", null));

        alertService.raiseIVRRequest(providers, ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        verify(gateway).post(IVRUrl, new ProviderReminderRequest(ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED, asList(phoneNumber), UUID));
    }

    @Test
    public void shouldRaiseRequestForAnyGroupOfProviders() {
        alertService.raiseIVRRequest(asList(new Provider("", "phoneNumber", "", null)), ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        alertService.raiseIVRRequest(asList(new Provider("", "anotherPhoneNumber", "", null)), ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);

        verify(gateway).post(IVRUrl, new ProviderReminderRequest(ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED, asList("anotherPhoneNumber"), UUID));
    }

    @Test
    public void shouldRaiseRequestForAnyTypeOfProviderReminder() {
        alertService.raiseIVRRequest(asList(new Provider("", "phoneNumber", "", null)), ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        alertService.raiseIVRRequest(asList(new Provider("", "anotherPhoneNumber", "", null)), ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED);

        verify(gateway).post(IVRUrl, new ProviderReminderRequest(ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED, asList("anotherPhoneNumber"), UUID));
    }

    @Test
    public void shouldRaiseMoreThanOneRequestWhenTheNumberOfProvidersIsGreaterThenBatchSize() {
        List<Provider> providers = asList(new Provider("", "phoneNumber1", "", null), new Provider("", "phoneNumber2", "", null));
        ScheduleType sameType = ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED;
        String sameUUID = UUID;

        alertService.raiseIVRRequest(providers, ScheduleType.PROVIDER_ADHERENCE_WINDOW_COMMENCED);
        verify(gateway).post(IVRUrl, new ProviderReminderRequest(sameType, asList("phoneNumber1"), sameUUID));
        verify(gateway).post(IVRUrl, new ProviderReminderRequest(sameType, asList("phoneNumber2"), sameUUID));
    }

    @Test
    public void shouldRaiseRequestWithUniqueUUIDForEveryRequest() {
        alertService.raiseIVRRequest(Collections.<Provider>emptyList(), ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED);
        alertService.raiseIVRRequest(Collections.<Provider>emptyList(), ScheduleType.PROVIDER_ADHERENCE_NOT_REPORTED);
        verify(uuidGenerator, times(2)).uuid();
    }
}
