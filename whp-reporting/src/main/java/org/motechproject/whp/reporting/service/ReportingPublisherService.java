package org.motechproject.whp.reporting.service;

import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.reporting.ReportingEventKeys;
import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ReportingPublisherService {
    @Autowired
    private EventContext eventContext;

    @Autowired
    public ReportingPublisherService(@Qualifier("eventContext") EventContext eventContext) {
        this.eventContext = eventContext;
    }

    public void reportAdherenceCapture(AdherenceCaptureRequest adherenceCaptureRequest){
        eventContext.send(ReportingEventKeys.REPORT_ADHERENCE_CAPTURE, adherenceCaptureRequest);

    }

}
