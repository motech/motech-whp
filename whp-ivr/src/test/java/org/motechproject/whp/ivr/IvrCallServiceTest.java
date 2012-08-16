package org.motechproject.whp.ivr;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.request.FlashingRequest;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.FlashingLogRequest;
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
    private ReportingPublisherService reportingPublisherService;
    
    private IvrCallService ivrCallService;
    private static final String IVR_CALL_BACK_URL = "callBackURL";
    private static final String PHONE_NUMBER_FIELD_KEY = "PHONE_NUMBER_FIELD_KEY";

    @Before
    public void setUp() {
        initMocks(this);
        mockCurrentDate(DateUtil.now());
        ivrCallService = new IvrCallService(ivrService, providerService, reportingPublisherService, IVR_CALL_BACK_URL);
    }

    @Test
    public void shouldInitiateOutboundCallForRegisteredMobileNumbers(){
        Provider provider = ProviderBuilder.newProviderBuilder().withPrimaryMobileNumber(PHONE_NUMBER_FIELD_KEY).build();
        FlashingRequest flashingRequest = new FlashingRequest(PHONE_NUMBER_FIELD_KEY, DateTime.now());

        when(providerService.findByMobileNumber(PHONE_NUMBER_FIELD_KEY)).thenReturn(provider);

        ivrCallService.handleFlashingRequest(flashingRequest);

        ArgumentCaptor<CallRequest> argumentCaptor = ArgumentCaptor.forClass(CallRequest.class);
        verify(ivrService).initiateCall(argumentCaptor.capture());

        CallRequest callRequest = argumentCaptor.getValue();
        assertThat(callRequest.getPhone(), is(PHONE_NUMBER_FIELD_KEY));
        assertThat(callRequest.getCallBackUrl(), is(IVR_CALL_BACK_URL));
        verify(providerService).findByMobileNumber(PHONE_NUMBER_FIELD_KEY);
    }

    @Test
    public void shouldNotInitiateOutboundCallForUnregisteredMobileNumbers() {
        FlashingRequest flashingRequest = new FlashingRequest(PHONE_NUMBER_FIELD_KEY, DateTime.now());

        when(providerService.findByMobileNumber(PHONE_NUMBER_FIELD_KEY)).thenReturn(null);

        ivrCallService.handleFlashingRequest(flashingRequest);

        verify(providerService).findByMobileNumber(PHONE_NUMBER_FIELD_KEY);
        verifyZeroInteractions(ivrService);
    }

    @Test
    public void shouldLogFlashingEvent_whenCallArrivesFromRegisteredMobileNumber(){
        Provider provider = ProviderBuilder.newProviderBuilder().withPrimaryMobileNumber(PHONE_NUMBER_FIELD_KEY).build();
        FlashingRequest flashingRequest = new FlashingRequest(PHONE_NUMBER_FIELD_KEY, DateTime.now());

        when(providerService.findByMobileNumber(PHONE_NUMBER_FIELD_KEY)).thenReturn(provider);

        ivrCallService.handleFlashingRequest(flashingRequest);

        ArgumentCaptor<CallRequest> argumentCaptor = ArgumentCaptor.forClass(CallRequest.class);
        verify(ivrService).initiateCall(argumentCaptor.capture());

        FlashingLogRequest expectedFlashingLogRequest = createFlashingLogRequest(flashingRequest);
        expectedFlashingLogRequest.setProviderId(provider.getProviderId());
        verify(reportingPublisherService).reportFlashingLog(expectedFlashingLogRequest);
    }

    @Test
    public void shouldLogFlashingEvent_whenCallArrivesFromUnregisteredMobileNumber(){
        FlashingRequest flashingRequest = new FlashingRequest(PHONE_NUMBER_FIELD_KEY, DateTime.now());
        when(providerService.findByMobileNumber(PHONE_NUMBER_FIELD_KEY)).thenReturn(null);
        ivrCallService.handleFlashingRequest(flashingRequest);

        FlashingLogRequest expectedFlashingRequestLog = createFlashingLogRequest(flashingRequest);
        expectedFlashingRequestLog.setProviderId(null);
        verify(reportingPublisherService).reportFlashingLog(expectedFlashingRequestLog);
    }

    private FlashingLogRequest createFlashingLogRequest(FlashingRequest flashingRequest) {
        FlashingLogRequest flashingLogRequest = new FlashingLogRequest();
        flashingLogRequest.setCallTime(flashingRequest.getCallTime().toDate());
        flashingLogRequest.setMobileNumber(flashingRequest.getMobileNumber());
        return flashingLogRequest;
    }
}
