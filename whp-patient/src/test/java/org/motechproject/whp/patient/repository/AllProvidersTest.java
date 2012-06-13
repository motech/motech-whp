package org.motechproject.whp.patient.repository;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllProvidersTest extends SpringIntegrationTest {

    @Autowired
    AllProviders allProviders;
    private Provider provider;

    @Before
    public void setUp() {
        provider = new Provider("P00001", "984567876", "district",
                DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
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

    @Test
    public void findByProviderIdShouldReturnNullIfKeywordIsNull() {
        assertEquals(null, allProviders.findByProviderId(null));
    }

    @Test
    public void shouldListAllProviders_alphabeticallySortedByDistrict() {
        List<Provider> testProviders = new ArrayList<Provider>();

        provider = new Provider("ac", "984567876", "c",
                DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        testProviders.add(provider);
        allProviders.add(provider);

        provider = new Provider("ab", "984567876", "b",
                DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        testProviders.add(provider);
        allProviders.add(provider);

        provider = new Provider("aa", "984567876", "a",
                DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        testProviders.add(provider);
        allProviders.add(provider);

        List<Provider> providers = allProviders.list();
        assertNotNull(providers);
        assertTrue(providers.containsAll(testProviders));
        assertTrue(providers.indexOf(testProviders.get(0)) > providers.indexOf(testProviders.get(1)));
        assertTrue(providers.indexOf(testProviders.get(1)) > providers.indexOf(testProviders.get(2)));
        assertTrue(providers.indexOf(testProviders.get(0)) > providers.indexOf(testProviders.get(2)));
    }

    @Override
    public void after() {
        markForDeletion(provider);
    }

}
