package org.motechproject.whp.providerreminder.ivr;

import org.motechproject.whp.common.collections.PaginatedList;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderRequest;
import org.motechproject.whp.providerreminder.util.UUIDGenerator;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

@Component
public class ProviderAlertService {

    private UUIDGenerator uuidGenerator;
    private ProviderReminderRequestProperties providerReminderProperties;
    private WGNGateway gateway;

    @Autowired
    public ProviderAlertService(UUIDGenerator uuidGenerator, ProviderReminderRequestProperties providerReminderProperties, WGNGateway gateway) {
        this.uuidGenerator = uuidGenerator;
        this.providerReminderProperties = providerReminderProperties;
        this.gateway = gateway;
    }

    public void raiseIVRRequest(List<Provider> providers, ProviderReminderType event) {
        String uuid = uuidGenerator.uuid();
        for (List<Provider> someProviders : new PaginatedList<>(providers, providerReminderProperties.getBatchSize())) {
            ProviderReminderRequest providerReminderRequest = new ProviderReminderRequest(event, extractPhoneNumbers(someProviders), uuid);
            gateway.post(providerReminderProperties.getProviderReminderUrl(), providerReminderRequest);
        }
    }

    private List<String> extractPhoneNumbers(List<Provider> providers) {
        return extract(providers, on(Provider.class).getPrimaryMobile());
    }
}
