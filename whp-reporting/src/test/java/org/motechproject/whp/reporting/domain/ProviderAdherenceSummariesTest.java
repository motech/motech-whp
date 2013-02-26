package org.motechproject.whp.reporting.domain;

import org.junit.Test;

import java.util.ArrayList;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class ProviderAdherenceSummariesTest {

    @Test
    public void shouldGetProvidersWhoHaveNotGivenAdherence() {
        ProviderAdherenceSummary providerAdherenceSummary1 = new ProviderAdherenceSummary("provider1", "primary", "secondary", "tertiary", false, 6);
        ProviderAdherenceSummary providerAdherenceSummary2 = new ProviderAdherenceSummary("provider2", "primary", "secondary", "tertiary", true, 6);
        ProviderAdherenceSummary providerAdherenceSummary3 = new ProviderAdherenceSummary("provider3", "primary", "secondary", "tertiary", true, 6);

        ProviderAdherenceSummaries providerAdherenceSummaries = new ProviderAdherenceSummaries("district",
                asList(providerAdherenceSummary1, providerAdherenceSummary2, providerAdherenceSummary3));

        assertEquals(asList(providerAdherenceSummary1), providerAdherenceSummaries.getPendingAdherenceSummaryList());
    }

    @Test
    public void shouldGetProvidersWhoHaveGivenAdherence() {
        ProviderAdherenceSummary providerAdherenceSummary1 = new ProviderAdherenceSummary("provider1", "primary", "secondary", "tertiary", false, 6);
        ProviderAdherenceSummary providerAdherenceSummary2 = new ProviderAdherenceSummary("provider2", "primary", "secondary", "tertiary", true, 6);
        ProviderAdherenceSummary providerAdherenceSummary3 = new ProviderAdherenceSummary("provider3", "primary", "secondary", "tertiary", true, 6);

        ProviderAdherenceSummaries providerAdherenceSummaries = new ProviderAdherenceSummaries("district",
                asList(providerAdherenceSummary1, providerAdherenceSummary2, providerAdherenceSummary3));

        assertEquals(asList(providerAdherenceSummary2, providerAdherenceSummary3), providerAdherenceSummaries.getAdherenceGivenSummaryList());
    }
}
