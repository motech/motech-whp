package org.motechproject.whp.ivr.operation;


import org.joda.time.DateTime;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;

public class CaptureAdherenceSubmissionTimeOperation implements INodeOperation {

    private DateTime captureTime;

    public CaptureAdherenceSubmissionTimeOperation() {
    }

    public CaptureAdherenceSubmissionTimeOperation(DateTime captureTime) {
        this.captureTime = captureTime;
    }

    @Override
    public void perform(String input, FlowSession session) {
        new IvrSession(session).startOfAdherenceSubmission(captureTime);
    }
}
