package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationProviderContext.xml")
public class AllProvidersIT extends SpringIntegrationTest {
    @Autowired
    AllProviders allProviders;

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
    }

    @Test
    public void shouldSaveProviderInfo() {
        Provider provider = new Provider("P00001", "984567876");
        allProviders.add(provider);

        Provider providerReturned = allProviders.findByProviderId("P00001");
        assertNotNull(providerReturned);
        assertEquals("984567876", providerReturned.getPrimaryMobile());
    }

}
