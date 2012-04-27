package org.motechproject.whp.mapper;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.contract.ProviderRequest;
import org.motechproject.whp.request.ProviderWebRequest;

public class ProviderRequestMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");


    public ProviderRequest map(ProviderWebRequest providerWebRequest) {
        ProviderRequest providerRequest = new ProviderRequest(
                providerWebRequest.getProvider_id(),
                providerWebRequest.getDistrict(),
                providerWebRequest.getPrimary_mobile(),
                dateTimeFormatter.parseDateTime(providerWebRequest.getDate()));
        providerRequest.setSecondaryMobile(providerWebRequest.getSecondary_mobile());
        providerRequest.setTertiaryMobile(providerWebRequest.getTertiary_mobile());

        return providerRequest;
    }
}
