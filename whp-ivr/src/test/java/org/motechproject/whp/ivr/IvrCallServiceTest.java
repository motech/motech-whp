package org.motechproject.whp.ivr;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.whp.ivr.request.FlashingRequest;
import org.motechproject.whp.user.service.ProviderService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class IvrCallServiceTest {
    @Mock
    private IVRService ivrService;
    @Mock
    ProviderService providerService;

    private IvrCallService ivrCallService;
    private String ivrCallBackURL = "callBackURL";

    @Before
    public void setUp() {
        initMocks(this);
        ivrCallService = new IvrCallService(ivrService, providerService, ivrCallBackURL);
    }

    @Test
    public void shouldInitiateOutboundCallForRegisteredMobileNumbers(){
        String phoneNumber = "phoneNumber";
        FlashingRequest flashingRequest = new FlashingRequest(phoneNumber, DateTime.now());

        when(providerService.isRegisteredMobileNumber(phoneNumber)).thenReturn(true);

        ivrCallService.handleFlashingRequest(flashingRequest);

        ArgumentCaptor<CallRequest> argumentCaptor = ArgumentCaptor.forClass(CallRequest.class);
        verify(ivrService).initiateCall(argumentCaptor.capture());

        CallRequest callRequest = argumentCaptor.getValue();
        assertThat(callRequest.getPhone(), is(phoneNumber));
        assertThat(callRequest.getCallBackUrl(), is(ivrCallBackURL));
        verify(providerService).isRegisteredMobileNumber(phoneNumber);
    }

    @Test
    public void shouldNotInitiateOutboundCallForUnregisteredMobileNumbers() {
        String phoneNumber = "phoneNumber";
        FlashingRequest flashingRequest = new FlashingRequest(phoneNumber, DateTime.now());

        when(providerService.isRegisteredMobileNumber(phoneNumber)).thenReturn(false);

        ivrCallService.handleFlashingRequest(flashingRequest);

        verify(providerService).isRegisteredMobileNumber(phoneNumber);
        verifyZeroInteractions(ivrService);
    }
}
