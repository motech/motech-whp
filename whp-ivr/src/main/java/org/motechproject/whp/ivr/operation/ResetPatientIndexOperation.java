package org.motechproject.whp.ivr.operation;


import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.ivr.session.IvrSession;

public class ResetPatientIndexOperation implements INodeOperation{
    @Override
    public void perform(String s, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        ivrSession.resetCurrentPatientIndex();
    }
}
