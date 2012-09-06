package org.motechproject.whp.patient.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ProviderTest {
    @Test
    public void shouldSetProviderIdInLowercase() {
        Provider provider = new Provider();
        provider.setProviderId("QWER");
        assertEquals("qwer", provider.getProviderId());
    }

    @Test
    public void shouldCreatProviderWithIdInLowercase() {
        Provider provider = new Provider("QWE", "", "", DateTime.now());
        assertEquals("qwe", provider.getProviderId());
    }

    @Test
    public void shouldHandleNullValuesForId() {
        Provider provider = new Provider("", "", "", DateTime.now());
        provider.setProviderId(null);
        assertEquals(null, provider.getProviderId());

        provider = new Provider(null, "", "", DateTime.now());
        assertEquals(null, provider.getProviderId());

    }

    @Test
    public void shouldCheckIfDistrictIsDifferent() {
        Provider provider = new ProviderBuilder().withProviderId("provider-id").withDistrict("old-district").build();

        assertTrue(provider.hasDifferentDistrict("new-district"));
        assertFalse(provider.hasDifferentDistrict("old-district"));
    }
}
