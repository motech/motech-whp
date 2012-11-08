package org.motechproject.whp.request;


import org.junit.Test;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallDetailsLogRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class IvrContainerRegistrationCallLogRequestTest {

    @Test
    public void shouldMapToContainerRegistrationCallLogRequest() {
        IvrContainerRegistrationCallLogRequest request = new IvrContainerRegistrationCallLogRequest();
        String callId = "callId";
        String disconnectionType = "disconnectionType";
        String startTime = "10/12/2012 12:32:35";
        String endTime = "10/12/2012 12:32:36";
        String mobileNumber = "mobileNumber";
        String providerId = "providerId";

        request.setCallId(callId);
        request.setDisconnectionType(disconnectionType);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setMobileNumber(mobileNumber);

        ContainerRegistrationCallDetailsLogRequest callLog = request.mapToContainerRegistrationCallLogRequest(providerId);

        assertThat(callLog.getCallId(), is(callId));
        assertThat(callLog.getDisconnectionType(), is(disconnectionType));
        assertThat(callLog.getStartDateTime(), is(startTime));
        assertThat(callLog.getEndDateTime(), is(endTime));
        assertThat(callLog.getMobileNumber(), is(mobileNumber));
        assertThat(callLog.getProviderId(), is(providerId));
    }

}
