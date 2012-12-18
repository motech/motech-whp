package org.motechproject.whp.controller;

import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.request.IvrProviderReminderCallLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/providerreminder")
public class ProviderReminderCallLogController {

    private ReportingPublisherService reportingPublisherService;
    private ProviderService providerService;

    @Autowired
    public ProviderReminderCallLogController(ReportingPublisherService reportingPublisherService, ProviderService providerService) {
        this.reportingPublisherService = reportingPublisherService;
        this.providerService = providerService;
    }

    @RequestMapping(value = "/calllog", method = POST)
    @ResponseBody
    public void recordCallLog(@RequestBody IvrProviderReminderCallLogRequest request) {
        Provider provider = providerService.findByMobileNumber(request.getMsisdn());
        String providerId = provider != null ? provider.getProviderId() : null;
        reportingPublisherService.reportProviderReminderCallLog(request.mapToReportingRequest(providerId));
    }

}
