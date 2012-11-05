package org.motechproject.whp.request;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.motechproject.whp.reports.contract.ContainerRegistrationCallLogRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

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

        ContainerRegistrationCallLogRequest callLog = request.mapToContainerRegistrationCallLogRequest(providerId);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
        assertThat(callLog.getCallId(), is(callId));
        assertThat(callLog.getDisconnectionType(), is(disconnectionType));
        assertThat(callLog.getStartDateTime(), is(dateTimeFormatter.parseDateTime(startTime).toDate()));
        assertThat(callLog.getEndDateTime(), is(dateTimeFormatter.parseDateTime(endTime).toDate()));
        assertThat(callLog.getMobileNumber(), is(mobileNumber));
        assertThat(callLog.getProviderId(), is(providerId));
    }

}
