package org.motechproject.whp.builder;

import org.motechproject.whp.request.ProviderRequest;

public class ProviderRequestBuilder {

    private ProviderRequest providerRequest = new ProviderRequest();

    public ProviderRequestBuilder withDefaults() {
        providerRequest = new ProviderRequest();
        providerRequest.setProvider_id("providerId");
        providerRequest.setPrimary_mobile("9880123456");
        providerRequest.setSecondary_mobile("9880123457");
        providerRequest.setTertiary_mobile("9880123458");
        providerRequest.setDistrict("Patna");
        providerRequest.setDate("12/01/2012 10:10:10");
        return this;
    }

    public ProviderRequest build() {
        return providerRequest;
    }

    public ProviderRequestBuilder withProviderId(String providerId) {
        providerRequest.setProvider_id(providerId);
        return this;
    }

    public ProviderRequestBuilder withPrimaryMobile(String primaryMobile) {
        providerRequest.setPrimary_mobile(primaryMobile);
        return this;
    }

    public ProviderRequestBuilder withSecondaryMobile(String secondaryMobile) {
        providerRequest.setSecondary_mobile(secondaryMobile);
        return this;
    }

    public ProviderRequestBuilder withTertiaryMobile(String tertiaryMobile) {
        providerRequest.setTertiary_mobile(tertiaryMobile);
        return this;
    }

    public ProviderRequestBuilder withDistrict(String district) {
        providerRequest.setDistrict(district);
        return this;
    }

    public ProviderRequestBuilder withDate(String date) {
        providerRequest.setDate(date);
        return this;
    }
}
