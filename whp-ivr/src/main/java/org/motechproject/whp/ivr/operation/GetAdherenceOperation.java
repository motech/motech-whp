package org.motechproject.whp.ivr.operation;


import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;

public class GetAdherenceOperation implements INodeOperation {

    public GetAdherenceOperation() {
    }

    @Override
    public void perform(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        ivrSession.adherenceInputForCurrentPatient(input);
    }

}
