package org.motechproject.whp.ivr;

import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.whp.ivr.audit.domain.FlashingRequestLog;
import org.motechproject.whp.ivr.audit.repository.AllFlashingRequestLogs;
import org.motechproject.whp.ivr.request.FlashingRequest;
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
    private AllFlashingRequestLogs allFlashingRequestLogs;
    private String ivrCallBackURL;

    @Autowired
    public IvrCallService(IVRService ivrService, ProviderService providerService, AllFlashingRequestLogs allFlashingRequestLogs, @Value("${application.url}") String ivrCallBackURL) {
        this.ivrService = ivrService;
        this.providerService = providerService;
        this.allFlashingRequestLogs = allFlashingRequestLogs;
        this.ivrCallBackURL = ivrCallBackURL;
    }

    public void handleFlashingRequest(FlashingRequest flashingRequest) {
        Map<String, String> params = new HashMap<>();
        CallRequest callRequest = new CallRequest(flashingRequest.getMobileNumber(), params, ivrCallBackURL);

        Provider provider = providerService.findByMobileNumber(flashingRequest.getMobileNumber());

        FlashingRequestLog flashingRequestLog = new FlashingRequestLog(
                flashingRequest.getMobileNumber(),
                flashingRequest.getCallTime());

        if(provider != null){
            ivrService.initiateCall(callRequest);
            flashingRequestLog.setProviderId(provider.getProviderId());
        }

        allFlashingRequestLogs.add(flashingRequestLog);
    }
}