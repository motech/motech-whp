package org.motechproject.whp.adherenceapi.errors;

import org.motechproject.whp.adherenceapi.response.AdherenceIVRError;

public abstract class AdherenceErrors {

    public abstract AdherenceIVRError error();

    public String errorMessage() {
        AdherenceIVRError error = error();
        if (null == error) {
            return "";
        } else {
            return error.name();
        }
    }

    public boolean isNotEmpty() {
        return error() != null;
    }
}
