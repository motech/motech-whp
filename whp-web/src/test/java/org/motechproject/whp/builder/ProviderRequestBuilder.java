package org.motechproject.whp.builder;

import org.motechproject.whp.request.ProviderWebRequest;

public class ProviderRequestBuilder {

    private ProviderWebRequest providerWebRequest = new ProviderWebRequest();

    public ProviderRequestBuilder() {
        withDefaults();
    }

    public ProviderRequestBuilder withDefaults() {
        providerWebRequest = new ProviderWebRequest();
        providerWebRequest.setProvider_id("providerId");
        providerWebRequest.setPrimary_mobile("9880123456");
        providerWebRequest.setSecondary_mobile("9880123457");
        providerWebRequest.setTertiary_mobile("9880123458");
        providerWebRequest.setDistrict("Patna");
        providerWebRequest.setDate("12/01/2012 10:10:10");
        providerWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        return this;
    }

    public ProviderWebRequest build() {
        return providerWebRequest;
    }

    public ProviderRequestBuilder withProviderId(String providerId) {
        providerWebRequest.setProvider_id(providerId);
        return this;
    }

    public ProviderRequestBuilder withPrimaryMobile(String primaryMobile) {
        providerWebRequest.setPrimary_mobile(primaryMobile);
        return this;
    }

    public ProviderRequestBuilder withSecondaryMobile(String secondaryMobile) {
        providerWebRequest.setSecondary_mobile(secondaryMobile);
        return this;
    }

    public ProviderRequestBuilder withTertiaryMobile(String tertiaryMobile) {
        providerWebRequest.setTertiary_mobile(tertiaryMobile);
        return this;
    }

    public ProviderRequestBuilder withDistrict(String district) {
        providerWebRequest.setDistrict(district);
        return this;
    }

    public ProviderRequestBuilder withDate(String date) {
        providerWebRequest.setDate(date);
        return this;
    }

    public ProviderRequestBuilder withAPIKey(String api_key) {
        providerWebRequest.setApi_key(api_key);
        return this;
    }
}
