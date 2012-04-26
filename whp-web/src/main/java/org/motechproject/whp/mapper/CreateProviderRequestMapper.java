package org.motechproject.whp.mapper;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.contract.CreateProviderRequest;
import org.motechproject.whp.request.ProviderRequest;

public class CreateProviderRequestMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");


    public CreateProviderRequest map(ProviderRequest providerRequest) {
        CreateProviderRequest createProviderRequest = new CreateProviderRequest(
                providerRequest.getProvider_id(),
                providerRequest.getDistrict(),
                providerRequest.getPrimary_mobile(),
                dateTimeFormatter.parseDateTime(providerRequest.getDate()));
        createProviderRequest.setSecondaryMobile(providerRequest.getSecondary_mobile());
        createProviderRequest.setTertiaryMobile(providerRequest.getTertiary_mobile());

        return createProviderRequest;
    }
}
