package org.motechproject.whp.adherenceapi.domain;

import org.motechproject.whp.user.domain.Provider;

public class ProviderId {

    private Provider provider;

    public ProviderId() {
        provider = null;
    }

    public ProviderId(Provider provider) {
        this.provider = provider;
    }

    public String value() {
        return (provider == null) ? null : provider.getProviderId();
    }

    public boolean isEmpty() {
        return provider == null;
    }
}
