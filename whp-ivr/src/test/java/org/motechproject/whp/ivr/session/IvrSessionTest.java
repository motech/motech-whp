package org.motechproject.whp.ivr.session;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.util.FlowSessionStub;

import static org.hamcrest.MatcherAssert.assertThat;


public class IvrSessionTest {

    @Test
    public void shouldUseProviderDisconnectAsDefaultCallStatus() {
        IvrSession ivrSession = new IvrSession(new FlowSessionStub());
        assertThat(ivrSession.callStatus(), Is.is(CallStatus.PROVIDER_DISCONNECT));
    }


}
