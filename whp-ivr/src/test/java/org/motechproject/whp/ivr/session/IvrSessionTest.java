package org.motechproject.whp.ivr.session;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.motechproject.server.kookoo.KookooCallServiceImpl;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.util.FlowSessionStub;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class IvrSessionTest {

    @Test
    public void shouldUseProviderDisconnectAsDefaultCallStatus() {
        IvrSession ivrSession = new IvrSession(new FlowSessionStub());
        assertThat(ivrSession.callStatus(), Is.is(CallStatus.PROVIDER_DISCONNECT));
    }

    @Test
    public void shouldReturnFlashingCallIdFromJsonDataMap() throws IOException {
        FlowSessionStub flowSession = new FlowSessionStub();
        flowSession.set(KookooCallServiceImpl.CUSTOM_DATA_KEY, "{\"flashingCallId\": \"abcd1234\"}");
        IvrSession ivrSession = new IvrSession(flowSession);

        assertThat(ivrSession.flashingCallId(), is("abcd1234"));
    }
}
