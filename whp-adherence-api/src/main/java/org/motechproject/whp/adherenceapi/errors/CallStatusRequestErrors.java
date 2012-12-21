package org.motechproject.whp.adherenceapi.errors;

import org.motechproject.whp.adherenceapi.response.AdherenceIVRError;

public class CallStatusRequestErrors extends AdherenceErrors {

    private boolean isMobileNumberBelongsToProvider;

    public CallStatusRequestErrors(boolean isMobileNumberBelongsToProvider) {
        this.isMobileNumberBelongsToProvider = isMobileNumberBelongsToProvider;
    }

    public AdherenceIVRError error() {
        if (!isMobileNumberBelongsToProvider) {
            return AdherenceIVRError.INVALID_PROVIDER;
        } else {
            return null;
        }
    }
}
