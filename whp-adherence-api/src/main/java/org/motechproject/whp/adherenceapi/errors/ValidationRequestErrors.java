package org.motechproject.whp.adherenceapi.errors;

import org.motechproject.whp.adherenceapi.response.AdherenceIVRError;

public class ValidationRequestErrors extends AdherenceErrors {

    private boolean isValidProvider;
    private boolean isValidPatient;
    private boolean belongsToProvider;

    public ValidationRequestErrors(boolean isValidProvider, boolean isValidPatient, boolean belongsToProvider) {
        this.isValidProvider = isValidProvider;
        this.isValidPatient = isValidPatient;
        this.belongsToProvider = belongsToProvider;
    }

    public AdherenceIVRError error() {
        if (!isValidProvider) {
            return AdherenceIVRError.INVALID_PROVIDER;
        } else if (!isValidPatient) {
            return AdherenceIVRError.INVALID_PATIENT;
        } else if (!belongsToProvider) {
            return AdherenceIVRError.INVALID_PATIENT_PROVIDER_COMBINATION;
        } else {
            return null;
        }
    }
}
