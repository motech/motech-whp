package org.motechproject.whp.ivr.controller;

import org.motechproject.ivr.domain.IVRMessage;
import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooIVRResponseBuilder;
import org.motechproject.ivr.kookoo.KookooResponseFactory;
import org.motechproject.ivr.kookoo.controller.SafeIVRController;
import org.motechproject.ivr.kookoo.controller.StandardResponseController;
import org.motechproject.ivr.kookoo.service.KookooCallDetailRecordsService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerURLs.ADHERENCE_URL)
public class IVRAdherenceCaptureController extends SafeIVRController {

    @Autowired
    public IVRAdherenceCaptureController(KookooCallDetailRecordsService callDetailRecordsService, IVRMessage ivrMessage, StandardResponseController standardResponseController) {
        super(ivrMessage, callDetailRecordsService, standardResponseController);
    }

    @Override
    public KookooIVRResponseBuilder newCall(KooKooIVRContext kooKooIVRContext) {
        return KookooResponseFactory.dtmfResponseWithWav(kooKooIVRContext.callId(), WHPIVRMessage.WELCOME);
    }
}
