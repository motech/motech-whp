package org.motechproject.whp.importer.csv.builder;

import org.motechproject.whp.importer.csv.request.ImportProviderRequest;

public class ImportProviderRequestBuilder {
    private ImportProviderRequest importProviderRequest = new ImportProviderRequest();
    public ImportProviderRequestBuilder withDefaults(String providerId){
        importProviderRequest.setDistrict("district_name");
        importProviderRequest.setPrimaryMobile("12345678");
        importProviderRequest.setProviderId(providerId);
        return this;
    }

    public ImportProviderRequest build(){
        return importProviderRequest;
    }

    public ImportProviderRequestBuilder withPrimaryNumber(String number){
        importProviderRequest.setPrimaryMobile(number);
        return this;
    }

    public ImportProviderRequestBuilder withSecondaryNumber(String number){
        importProviderRequest.setSecondaryMobile(number);
        return this;
    }

    public ImportProviderRequestBuilder withTertiaryNumber(String number){
        importProviderRequest.setTertiaryMobile(number);
        return this;
    }

    public ImportProviderRequestBuilder withDistrict(String district){
        importProviderRequest.setDistrict(district);
        return this;
    }

    public ImportProviderRequestBuilder withProviderId(String providerId){
        importProviderRequest.setProviderId(providerId);
        return this;
    }
}
