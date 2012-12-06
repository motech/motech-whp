package org.motechproject.whp.adherenceapi.validator;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceRequestsValidator {

    private ProviderService providerService;
    private AdherenceWindow adherenceWindow;

    @Autowired
    public AdherenceRequestsValidator(ProviderService providerService, AdherenceWindow adherenceWindow) {
        this.providerService = providerService;
        this.adherenceWindow = adherenceWindow;
    }

    public ErrorWithParameters validateFlashingRequest(AdherenceCaptureFlashingRequest flashingRequest, LocalDate requestDate) {

        if(!validateMobileNumber(flashingRequest))
            return new ErrorWithParameters(AdherenceCaptureError.INVALID_MOBILE_NUMBER.name());

        if(!validateAdherenceDay(requestDate))
            return new ErrorWithParameters(AdherenceCaptureError.NON_ADHERENCE_DAY.name());

        return null;
    }

    private Boolean validateAdherenceDay(LocalDate requestDate) {
        return adherenceWindow.isValidAdherenceDay(requestDate);
    }

    private Boolean validateMobileNumber(AdherenceCaptureFlashingRequest flashingRequest) {
        return (providerService.findByMobileNumber(flashingRequest.getMsisdn()) != null);
    }
}
