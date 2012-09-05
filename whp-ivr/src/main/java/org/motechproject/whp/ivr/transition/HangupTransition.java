package org.motechproject.whp.ivr.transition;

import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.ITransition;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.operation.PublishCallLogOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode
public class HangupTransition implements ITransition {

    @Autowired
    private ReportingPublisherService reportingPublisherService;

    public HangupTransition() {
    }

    public HangupTransition(ReportingPublisherService reportingPublisherService) {
        this.reportingPublisherService = reportingPublisherService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);

        Node nextNode = new Node();
        nextNode.addOperations(new PublishCallLogOperation(reportingPublisherService, ivrSession.callStatus(), DateUtil.now()));

        return nextNode;
    }
}
