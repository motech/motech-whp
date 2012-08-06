package org.motechproject.whp.ivr.operation;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.ivr.util.FlowSessionStub;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.ivr.session.IvrSession.CALL_START_TIME;

public class RecordCallStartTimeOperationTest {

    @Test
    public void shouldSetCallStartTimeToFlowSession(){
        FlowSessionStub flowSession = new FlowSessionStub();

        DateTime callStartTime = new DateTime();
        RecordCallStartTimeOperation operation = new RecordCallStartTimeOperation(callStartTime);
        operation.perform("", flowSession);

        assertThat((DateTime) flowSession.get(CALL_START_TIME), is(callStartTime));
    }


}
