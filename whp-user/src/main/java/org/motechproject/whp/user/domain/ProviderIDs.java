package org.motechproject.whp.user.domain;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import lombok.EqualsAndHashCode;

import java.util.*;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

@EqualsAndHashCode
public class ProviderIds {

    private Set<String> providerIds;

    public ProviderIds() {
        providerIds = new TreeSet<>();
    }

    public ProviderIds(List<String> providerIds) {
        this.providerIds = new TreeSet<>(providerIds);
    }

    public static ProviderIds ofProviders(List<Provider> providers) {
        return new ProviderIds(extract(providers, on(Provider.class).getProviderId()));
    }

    public ProviderIds subtract(ProviderIds providerIds) {
        ProviderIds difference = new ProviderIds();
        for (String providerId : this.providerIds) {
            if (!providerIds.has(providerId))
                difference.add(providerId);
        }
        return difference;
    }

    public String toJSONString() {
        JsonArray result = new JsonArray();
        for (String providerId : providerIds) {
            result.add(new JsonPrimitive(providerId));
        }
        return result.toString();
    }

    public void add(String providerId) {
        this.providerIds.add(providerId);
    }

    public Collection<String> asList() {
        return new ArrayList<>(providerIds);
    }

    private boolean has(String providerId) {
        return this.providerIds.contains(providerId);
    }
}
