package org.motechproject.whp.reporting.service;

import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.reporting.ReportingEventKeys;
import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ReportingPublisherService {

    HttpClientService httpClientService;

    @Autowired
    public ReportingPublisherService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public void reportAdherenceCapture(AdherenceCaptureRequest adherenceCaptureRequest){
        httpClientService.post(ReportingEventKeys.REPORT_ADHERENCE_CAPTURE, adherenceCaptureRequest);
    }

}
