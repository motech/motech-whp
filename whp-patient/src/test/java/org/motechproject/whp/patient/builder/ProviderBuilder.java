package org.motechproject.whp.patient.builder;

import org.motechproject.util.DateUtil;
import org.motechproject.whp.user.domain.Provider;

public class ProviderBuilder {

    private Provider provider;

    private ProviderBuilder() {
        provider = new Provider();
    }

    public ProviderBuilder withDefaults() {
        provider.setProviderId("providerId");
        provider.setLastModifiedDate(DateUtil.now());
        provider.setPrimaryMobile("1234567890");
        return this;
    }

    public ProviderBuilder withId(String id) {
        provider.setId(id);
        return this;
    }

    public ProviderBuilder withProviderId(String providerId) {
        provider.setProviderId(providerId);
        return this;
    }

    public static ProviderBuilder startRecording() {
        return new ProviderBuilder();
    }

    public Provider build() {
        return provider;
    }
}
