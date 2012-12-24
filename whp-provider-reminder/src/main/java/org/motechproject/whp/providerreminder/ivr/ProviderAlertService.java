package org.motechproject.whp.providerreminder.ivr;

import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.collections.PaginatedList;
import org.motechproject.whp.common.service.IvrConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

@Component
public class ProviderAlertService {

    private HttpClientService httpClientService;
    private UUIDGenerator uuidGenerator;
    private ProviderReminderRequestProperties requestProperties;
    private final String ivrUrl;

    @Autowired
    public ProviderAlertService(HttpClientService httpClientService, UUIDGenerator uuidGenerator, ProviderReminderRequestProperties requestProperties, IvrConfiguration ivrConfiguration) {
        this.httpClientService = httpClientService;
        this.uuidGenerator = uuidGenerator;
        this.requestProperties = requestProperties;
        this.ivrUrl = ivrConfiguration.getProviderReminderUrl();
    }

    public void raiseIVRRequest(List<Provider> providers, ProviderReminderType event) {
        String uuid = uuidGenerator.uuid();
        for (List<Provider> someProviders : new PaginatedList<>(providers, requestProperties.getBatchSize())) {
            ProviderReminderRequest providerReminderRequest = new ProviderReminderRequest(event, extractPhoneNumbers(someProviders), uuid);
            httpClientService.post(ivrUrl, providerReminderRequest.toXML());
        }
    }

    private List<String> extractPhoneNumbers(List<Provider> providers) {
        return extract(providers, on(Provider.class).getPrimaryMobile());
    }
}
