package org.motechproject.whp.ivr;

import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.ivr.request.FlashingRequest;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.FlashingLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IvrCallService {

    private IVRService ivrService;
    private ProviderService providerService;
    private String ivrCallBackURL;
    private ReportingPublisherService reportingPublisherService;
    private static final String NATIONAL_TELEPHONE_NUMBER_PREFIX = "0";

    @Autowired
    public IvrCallService(IVRService ivrService, ProviderService providerService, ReportingPublisherService reportingPublisherService, @Value("${application.url}") String ivrCallBackURL) {
        this.ivrService = ivrService;
        this.providerService = providerService;
        this.reportingPublisherService = reportingPublisherService;
        this.ivrCallBackURL = ivrCallBackURL;
    }

    public void handleFlashingRequest(FlashingRequest flashingRequest) {
        FlashingLogRequest flashingRequestLog = buildFlashingLogRequest(flashingRequest);

        String mobileNumber = flashingRequest.getMobileNumber();
        Provider provider = providerService.findByMobileNumber(mobileNumber);
        if(provider != null){
            Map<String, String> params = new HashMap<>();
            mobileNumber = prefixNationalCode(mobileNumber);
            CallRequest callRequest = new CallRequest(mobileNumber, params, ivrCallBackURL);
            ivrService.initiateCall(callRequest);
            flashingRequestLog.setProviderId(provider.getProviderId());
        }

        reportingPublisherService.reportFlashingRequest(flashingRequestLog);
    }

    private String prefixNationalCode(String mobileNumber) {
        return NATIONAL_TELEPHONE_NUMBER_PREFIX + mobileNumber;
    }

    private FlashingLogRequest buildFlashingLogRequest(FlashingRequest flashingRequest) {
        FlashingLogRequest flashingLogRequest = new FlashingLogRequest();
        flashingLogRequest.setCreationTime(DateUtil.now().toDate());
        flashingLogRequest.setCallTime(flashingRequest.getCallTime().toDate());
        flashingLogRequest.setMobileNumber(flashingRequest.getMobileNumber());
        return flashingLogRequest;
    }
}