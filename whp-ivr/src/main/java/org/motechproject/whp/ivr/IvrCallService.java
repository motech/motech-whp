package org.motechproject.whp.ivr;

import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class IvrCallService {

    private IVRService ivrService;
    private String ivrCallBackURL;

    public IvrCallService(IVRService ivrService, @Value("${application.url}") String ivrCallBackURL) {
        this.ivrService = ivrService;
        this.ivrCallBackURL = ivrCallBackURL;
    }

    public void initiateCall(String phoneNumber) {
        Map<String, String> params = new HashMap<>();
        CallRequest callRequest = new CallRequest(phoneNumber, params, ivrCallBackURL);

        ivrService.initiateCall(callRequest);
    }
}
