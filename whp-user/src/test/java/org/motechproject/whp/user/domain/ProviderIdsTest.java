package org.motechproject.whp.user.domain;

import org.junit.Test;
import org.motechproject.whp.user.builder.ProviderBuilder;

import java.util.Collections;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class ProviderIdsTest {

    private ProviderIds providerIds = new ProviderIds(asList("providerId1", "providerId2"));

    @Test
    public void shouldBeEmptyWhenSubtractedWithItself() {
        assertEquals(new ProviderIds(Collections.<String>emptyList()), providerIds.subtract(providerIds));
    }

    @Test
    public void shouldBeIdenticalWhenSubtractedWithAnEmptyProviderIds() {
        assertEquals(providerIds, providerIds.subtract(new ProviderIds(Collections.<String>emptyList())));
    }

    @Test
    public void shouldBeASubsetWithElementsUniqueToItselfWhenSubtractedWithProviderIds() {
        assertEquals(new ProviderIds(asList("providerId1")), providerIds.subtract(new ProviderIds(asList("providerId2"))));
    }

    @Test
    public void shouldHaveJsonArrayRepresentation() {
        assertEquals("[\"providerId1\",\"providerId2\"]", providerIds.toJSONString());
    }

    @Test
    public void shouldReturnProviderIdsFromProviders() {
        Provider provider1 = new ProviderBuilder().withDefaults().withProviderId("1234").build();
        Provider provider2 = new ProviderBuilder().withDefaults().withProviderId("5678").build();

        assertEquals(new ProviderIds(asList("1234", "5678")), ProviderIds.ofProviders(asList(provider1, provider2)));
    }
}
