package org.motechproject.whp.ivr.operation;


import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.ivr.util.IvrSession;

public class GetAdherenceOperation implements INodeOperation{

    @Override
    public void perform(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        ivrSession.adherenceInputForCurrentPatient(input);
    }

}
