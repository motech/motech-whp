package org.motechproject.whp.ivr.operation;

import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;

@EqualsAndHashCode
public class RecordCallStartTimeOperation implements INodeOperation {

    private DateTime callStartTime;

    public RecordCallStartTimeOperation() {
    }

    public RecordCallStartTimeOperation(DateTime callStartTime) {
        this.callStartTime = callStartTime;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        new IvrSession(session).recordCallStartTime(callStartTime);
    }
}
