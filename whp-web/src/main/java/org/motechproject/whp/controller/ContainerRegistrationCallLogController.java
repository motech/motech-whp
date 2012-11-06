package org.motechproject.whp.controller;


import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.request.IvrContainerRegistrationCallLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/ivr/containerRegistration")
public class ContainerRegistrationCallLogController {

    private ReportingPublisherService reportingPublisherService;
    private ProviderService providerService;

    @Autowired
    public ContainerRegistrationCallLogController(ReportingPublisherService reportingPublisherService, ProviderService providerService) {
        this.reportingPublisherService = reportingPublisherService;
        this.providerService = providerService;
    }

    @RequestMapping(value = "/callLog", method = POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void recordCallLog(@RequestBody IvrContainerRegistrationCallLogRequest request) {
        Provider provider = providerService.findByMobileNumber(request.getMobileNumber());
        String providerId = provider != null ? provider.getProviderId() : null;
        reportingPublisherService.reportContainerRegistrationCallLog(request.mapToContainerRegistrationCallLogRequest(providerId));
    }
}
