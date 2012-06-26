package org.motechproject.whp.webservice.mapper;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.springframework.stereotype.Component;

@Component
public class ProviderRequestMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT);

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
