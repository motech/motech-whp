package org.motechproject.whp.adherenceapi.errors;

import org.motechproject.whp.adherenceapi.response.AdherenceIVRError;

public class ValidationRequestErrors extends AdherenceErrors {

    private boolean isValidMSISDN;
    private boolean isValidPatient;
    private boolean belongsToProvider;

    public ValidationRequestErrors(boolean isValidMSISDN, boolean isValidPatient, boolean belongsToProvider) {
        this.isValidMSISDN = isValidMSISDN;
        this.isValidPatient = isValidPatient;
        this.belongsToProvider = belongsToProvider;
    }

    public AdherenceIVRError error() {
        if (!isValidMSISDN) {
            return AdherenceIVRError.INVALID_MOBILE_NUMBER;
        } else if (!isValidPatient) {
            return AdherenceIVRError.INVALID_PATIENT;
        } else if (!belongsToProvider) {
            return AdherenceIVRError.INVALID_PATIENT_PROVIDER_COMBINATION;
        } else {
            return null;
        }
    }
}
