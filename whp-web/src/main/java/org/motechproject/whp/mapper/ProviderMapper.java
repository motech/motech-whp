package org.motechproject.whp.mapper;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.request.ProviderRequest;

public class ProviderMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");


    public Provider map(ProviderRequest providerRequest) {
        Provider provider = new Provider(
                providerRequest.getProvider_id(),
                providerRequest.getPrimary_mobile(),
                providerRequest.getDistrict(),
                dateTimeFormatter.parseDateTime(providerRequest.getDate()));
        provider.setSecondaryMobile(providerRequest.getSecondary_mobile());
        provider.setTertiaryMobile(providerRequest.getTertiary_mobile());

        return provider;
    }
}
