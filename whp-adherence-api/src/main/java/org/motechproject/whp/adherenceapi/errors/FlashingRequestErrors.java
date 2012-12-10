package org.motechproject.whp.adherenceapi.errors;

import org.motechproject.whp.adherenceapi.response.AdherenceIVRError;

public class FlashingRequestErrors extends AdherenceErrors {

    private boolean isValidMSISDN;
    private boolean isValidAdherenceDay;

    public FlashingRequestErrors(boolean isValidMSISDN, boolean isValidAdherenceDay) {
        this.isValidMSISDN = isValidMSISDN;
        this.isValidAdherenceDay = isValidAdherenceDay;
    }

    public AdherenceIVRError error() {
        if (!isValidMSISDN) {
            return AdherenceIVRError.INVALID_MOBILE_NUMBER;
        } else if (!isValidAdherenceDay) {
            return AdherenceIVRError.NON_ADHERENCE_DAY;
        } else {
            return null;
        }
    }
}
