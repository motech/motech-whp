package org.motechproject.whp.ivr;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.audit.domain.FlashingRequestLog;
import org.motechproject.whp.ivr.audit.repository.AllFlashingRequestLogs;
import org.motechproject.whp.ivr.request.FlashingRequest;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class IvrCallServiceTest extends BaseUnitTest {
    @Mock
    private IVRService ivrService;
    @Mock
    ProviderService providerService;
    @Mock
    AllFlashingRequestLogs allFlashingRequestLogs;

    private IvrCallService ivrCallService;
    private String ivrCallBackURL = "callBackURL";

    @Before
    public void setUp() {
        initMocks(this);
        mockCurrentDate(DateUtil.now());
        ivrCallService = new IvrCallService(ivrService, providerService, allFlashingRequestLogs, ivrCallBackURL);
    }

    @Test
    public void shouldInitiateOutboundCallForRegisteredMobileNumbers(){
        String phoneNumber = "phoneNumber";
        Provider provider = ProviderBuilder.newProviderBuilder().withPrimaryMobileNumber(phoneNumber).build();
        FlashingRequest flashingRequest = new FlashingRequest(phoneNumber, DateTime.now());
        FlashingRequestLog expectedFlashingRequestLog = createFlashingRequestLog(flashingRequest);
        expectedFlashingRequestLog.setProviderId(provider.getProviderId());

        when(providerService.findByMobileNumber(phoneNumber)).thenReturn(provider);

        ivrCallService.handleFlashingRequest(flashingRequest);

        verify(allFlashingRequestLogs).add(expectedFlashingRequestLog);

        ArgumentCaptor<CallRequest> argumentCaptor = ArgumentCaptor.forClass(CallRequest.class);
        verify(ivrService).initiateCall(argumentCaptor.capture());

        CallRequest callRequest = argumentCaptor.getValue();
        assertThat(callRequest.getPhone(), is(phoneNumber));
        assertThat(callRequest.getCallBackUrl(), is(ivrCallBackURL));
        verify(providerService).findByMobileNumber(phoneNumber);
    }

    @Test
    public void shouldNotInitiateOutboundCallForUnregisteredMobileNumbers() {
        String phoneNumber = "phoneNumber";
        FlashingRequest flashingRequest = new FlashingRequest(phoneNumber, DateTime.now());
        FlashingRequestLog expectedFlashingRequestLog = createFlashingRequestLog(flashingRequest);
        expectedFlashingRequestLog.setProviderId(null);

        when(providerService.findByMobileNumber(phoneNumber)).thenReturn(null);

        ivrCallService.handleFlashingRequest(flashingRequest);

        verify(providerService).findByMobileNumber(phoneNumber);
        verify(allFlashingRequestLogs).add(expectedFlashingRequestLog);
        verifyZeroInteractions(ivrService);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(allFlashingRequestLogs);
        verifyNoMoreInteractions(providerService);
        verifyNoMoreInteractions(ivrService);
    }

    private FlashingRequestLog createFlashingRequestLog(FlashingRequest flashingRequest) {
        return new FlashingRequestLog(flashingRequest.getMobileNumber(), flashingRequest.getCallTime());
    }
}
