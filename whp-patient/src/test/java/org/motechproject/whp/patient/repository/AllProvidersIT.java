package org.motechproject.whp.patient.repository;

import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllProvidersIT extends SpringIntegrationTest {

    @Autowired
    AllProviders allProviders;

    @Test
    public void shouldSaveProviderInfo() {
        Provider provider = new Provider("P00001", "984567876", "district",
                DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss").parseDateTime("12/01/2012 10:10:10"));
        allProviders.add(provider);
        markForDeletion(provider);

        Provider providerReturned = allProviders.findByProviderId("P00001");
        assertNotNull(providerReturned);
        assertEquals("984567876", providerReturned.getPrimaryMobile());
    }

}
