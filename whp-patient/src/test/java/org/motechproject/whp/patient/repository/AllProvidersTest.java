package org.motechproject.whp.patient.repository;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllProvidersTest extends SpringIntegrationTest {

    @Autowired
    AllProviders allProviders;
    private Provider provider;

    @Before
    public void setUp() {
        provider = new Provider("P00001", "984567876", "district",
                DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss").parseDateTime("12/01/2012 10:10:10"));
    }

    @Test
    public void shouldSaveProviderInfo() {
        allProviders.add(provider);
        Provider providerReturned = allProviders.findByProviderId("P00001");
        assertNotNull(providerReturned);
        assertEquals("984567876", providerReturned.getPrimaryMobile());
    }

    @Test
    public void shouldThrowWHPRuntimeExceptionWhenProviderIdAlreadyExists() {
        allProviders.add(provider);
        exceptionThrown.expect(WHPRuntimeException.class);

        allProviders.addOrReplace(provider);
    }

    @Override
    public void after() {
        markForDeletion(provider);
    }

}
