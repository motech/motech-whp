package org.motechproject.whp.providerreminder.ivr;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;
import org.motechproject.whp.user.domain.Provider;

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
    private HttpClientService httpClientService;

    ProviderAlertService alertService;

    @Before
    public void setup() {
        initMocks(this);
        when(requestProperties.getProviderReminderUrl()).thenReturn(IVRUrl);
        when(requestProperties.getBatchSize()).thenReturn(1);
        when(uuidGenerator.uuid()).thenReturn(UUID);
        alertService = new ProviderAlertService(httpClientService, uuidGenerator, requestProperties);
    }

    @Test
    public void shouldRaiseRequestForProviderMobileNumbers() {
        String phoneNumber = "phoneNumber";
        List<Provider> providers = asList(new Provider("", phoneNumber, "", null));

        alertService.raiseIVRRequest(providers, ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        verify(httpClientService).post(IVRUrl, new ProviderReminderRequest(ProviderReminderType.ADHERENCE_WINDOW_COMMENCED, asList(phoneNumber), UUID).toXML());
    }

    @Test
    public void shouldRaiseRequestForAnyGroupOfProviders() {
        alertService.raiseIVRRequest(asList(new Provider("", "phoneNumber", "", null)), ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        alertService.raiseIVRRequest(asList(new Provider("", "anotherPhoneNumber", "", null)), ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);

        verify(httpClientService).post(IVRUrl, new ProviderReminderRequest(ProviderReminderType.ADHERENCE_WINDOW_COMMENCED, asList("anotherPhoneNumber"), UUID).toXML());
    }

    @Test
    public void shouldRaiseRequestForAnyTypeOfProviderReminder() {
        alertService.raiseIVRRequest(asList(new Provider("", "phoneNumber", "", null)), ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        alertService.raiseIVRRequest(asList(new Provider("", "anotherPhoneNumber", "", null)), ProviderReminderType.ADHERENCE_NOT_REPORTED);

        verify(httpClientService).post(IVRUrl, new ProviderReminderRequest(ProviderReminderType.ADHERENCE_NOT_REPORTED, asList("anotherPhoneNumber"), UUID).toXML());
    }

    @Test
    public void shouldRaiseMoreThanOneRequestWhenTheNumberOfProvidersIsGreaterThenBatchSize() {
        List<Provider> providers = asList(new Provider("", "phoneNumber1", "", null), new Provider("", "phoneNumber2", "", null));
        ProviderReminderType sameType = ProviderReminderType.ADHERENCE_WINDOW_COMMENCED;
        String sameUUID = UUID;

        alertService.raiseIVRRequest(providers, ProviderReminderType.ADHERENCE_WINDOW_COMMENCED);
        verify(httpClientService).post(IVRUrl, new ProviderReminderRequest(sameType, asList("phoneNumber1"), sameUUID).toXML());
        verify(httpClientService).post(IVRUrl, new ProviderReminderRequest(sameType, asList("phoneNumber2"), sameUUID).toXML());
    }

    @Test
    public void shouldRaiseRequestWithUniqueUUIDForEveryRequest() {
        alertService.raiseIVRRequest(Collections.<Provider>emptyList(), ProviderReminderType.ADHERENCE_NOT_REPORTED);
        alertService.raiseIVRRequest(Collections.<Provider>emptyList(), ProviderReminderType.ADHERENCE_NOT_REPORTED);
        verify(uuidGenerator, times(2)).uuid();
    }
}
