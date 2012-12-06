package org.motechproject.whp.adherenceapi.builder;

import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;

public class AdherenceValidationRequestBuilder {

    public static final String GOVT_CATEGORY_DOSE_TAKEN_COUNT = "3";
    private AdherenceValidationRequest adherenceValidationRequest;

    public AdherenceValidationRequestBuilder withDefaults() {
        adherenceValidationRequest = new AdherenceValidationRequest();
        adherenceValidationRequest.setPatientId("1234");
        adherenceValidationRequest.setDoseTakenCount(GOVT_CATEGORY_DOSE_TAKEN_COUNT);
        adherenceValidationRequest.setMsisdn("1234567890");
        adherenceValidationRequest.setCallId("call_id");
        return this;
    }


    public AdherenceValidationRequest build() {
        return adherenceValidationRequest;
    }

    public AdherenceValidationRequestBuilder withDoseTakenCount(String doseTakenCount) {
        adherenceValidationRequest.setDoseTakenCount(doseTakenCount);
        return this;
    }
}
