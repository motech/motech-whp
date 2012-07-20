package org.motechproject.whp.ivr.transition;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;

public class AdherenceCapture implements ITransition {

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        return null;
    }

}
