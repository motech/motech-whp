package org.motechproject.whp.ivr;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.model.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class IvrCallServiceTest {

    private IvrCallService ivrCallService;

    @Mock
    private IVRService ivrService;

    private String ivrCallBackURL = "callBackURL";

    @Before
    public void setUp() {
        initMocks(this);
        ivrCallService = new IvrCallService(ivrService, ivrCallBackURL);
    }

    @Test
    public void shouldStartOutboundCall(){
        String phoneNumber = "phoneNumber";

        ivrCallService.initiateCall(phoneNumber);

        ArgumentCaptor<CallRequest> argumentCaptor = ArgumentCaptor.forClass(CallRequest.class);
        verify(ivrService).initiateCall(argumentCaptor.capture());

        CallRequest callRequest = argumentCaptor.getValue();
        assertThat(callRequest.getPhone(), is(phoneNumber));
        assertThat(callRequest.getCallBackUrl(), is(ivrCallBackURL));
    }

}
