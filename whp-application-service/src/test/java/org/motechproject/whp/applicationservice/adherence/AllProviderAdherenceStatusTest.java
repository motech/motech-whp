package org.motechproject.whp.applicationservice.adherence;

import org.junit.Test;
import org.motechproject.whp.common.domain.ProviderPatientCount;
import org.motechproject.whp.user.domain.ProviderIds;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AllProviderAdherenceStatusTest {

    @Test
    public void shouldReturnProviderIdsWithoutAdherence() {
        List<ProviderPatientCount> allProviderPatientCounts = asList(new ProviderPatientCount("providerId", 3));
        List<ProviderPatientCount> providerIdPatientWithAdherenceCounts  = asList(new ProviderPatientCount("providerId", 1));
        AllProviderAdherenceStatus allProviderAdherenceStatus = new AllProviderAdherenceStatus(allProviderPatientCounts, providerIdPatientWithAdherenceCounts);

        assertEquals(new ProviderIds(asList("providerId")), allProviderAdherenceStatus.providersWithPendingAdherence());
    }
}
