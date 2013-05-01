package org.motechproject.whp.adherenceapi.domain;

public class ProviderId {

    String providerId = null;
    public ProviderId() {
        providerId = null;
    }

    public ProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String value() {
        return providerId;
    }

    public boolean isEmpty() {
        return providerId == null;
    }
}
