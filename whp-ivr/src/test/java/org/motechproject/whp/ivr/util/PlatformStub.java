package org.motechproject.whp.ivr.util;

import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.INodeOperation;
import org.motechproject.decisiontree.core.model.ITransition;

public class PlatformStub {

    public static void play(ITransition transition, FlowSession session, String input) {
        for (INodeOperation operation : transition.getDestinationNode(input, session).getOperations()) {
            operation.perform("8", session);
        }
    }

    public static void replay(ITransition transition, FlowSession session, String input) {
        transition.getDestinationNode(input, session);
    }
}
