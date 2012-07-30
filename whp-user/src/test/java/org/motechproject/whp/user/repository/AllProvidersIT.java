package org.motechproject.whp.user.repository;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ContextConfiguration(locations = "classpath*:applicationUserContext.xml")
public class AllProvidersIT extends SpringIntegrationTest {

    public static final String DATE_TIME_FORMAT = "dd/MM/YYYY HH:mm:ss";

    @Autowired
    AllProviders allProviders;
    private Provider provider;

    @Before
    public void setUp() {
        provider = new Provider("P00001", "984567876", "district",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    }

    private void addAndMarkForDeletion(Provider provider) {
        allProviders.add(provider);
        markForDeletion(provider);
    }

    @Test
    public void shouldSaveProviderInfo() {
        addAndMarkForDeletion(provider);
        Provider providerReturned = allProviders.findByProviderId("P00001");
        assertNotNull(providerReturned);
        assertEquals("984567876", providerReturned.getPrimaryMobile());
    }

    @Test
    public void shouldUpdateProviderIfAlreadyExists() {
        int sizeBeforeAdding = allProviders.getAll().size();

        addAndMarkForDeletion(provider);
        provider.setPrimaryMobile("modifiedNumber");
        allProviders.addOrReplace(provider);
        markForDeletion(provider);

        List<Provider> providers = allProviders.getAll();
        assertEquals(sizeBeforeAdding + 1, providers.size());
        assertEquals("modifiedNumber", allProviders.findByProviderId("P00001").getPrimaryMobile());
    }

    @Test
    public void findByProviderIdShouldReturnNullIfKeywordIsNull() {
        assertEquals(null, allProviders.findByProviderId(null));
    }

    @Test
    public void shouldListProviders_ByDistrict() {
        Provider provider1 = new Provider("ab", "984567876", "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider2 = new Provider("aa", "984567876", "districtB",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider3 = new Provider("aa", "984567876", "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

        addAndMarkForDeletion(provider1);
        addAndMarkForDeletion(provider2);
        addAndMarkForDeletion(provider3);

        List<Provider> providers = allProviders.findByDistrict("districtA");
        assertEquals(2, providers.size());
        assertEquals(provider3, providers.get(0));
        assertEquals(provider1, providers.get(1));
    }

    @Test
    public void shouldListProviders_ByDistrictAndProviderId() {
        Provider provider1 = new Provider("ab", "984567876", "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider2 = new Provider("aa", "984567876", "districtB",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider3 = new Provider("aa", "111111111", "districtB",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

        addAndMarkForDeletion(provider1);
        addAndMarkForDeletion(provider2);
        addAndMarkForDeletion(provider3);

        List<Provider> providers = allProviders.findByDistrictAndProviderId("districtB", "aa");
        assertEquals(2, providers.size());
        assertTrue(providers.contains(provider2));
        assertTrue(providers.contains(provider3));
    }

    @Test
    public void providerSearchShouldBeCaseInsensitive() {
        Provider provider1 = new Provider("ab", "984567876", "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider2 = new Provider("aa", "984567876", "districtB",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

        addAndMarkForDeletion(provider1);
        addAndMarkForDeletion(provider2);

        assertEquals(provider2, allProviders.findByProviderId("Aa"));
        assertEquals(provider2, allProviders.findByProviderId("aa"));
    }

    @Test
    public void providerSearchShouldBeCaseInsensitive_WhenBothDistrictAndProviderIdAreGiven() {
        Provider provider1 = new Provider("ab", "984567876", "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider2 = new Provider("aa", "984567876", "districtB",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        Provider provider3 = new Provider("aa", "111111111", "districtB",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

        addAndMarkForDeletion(provider1);
        addAndMarkForDeletion(provider2);
        addAndMarkForDeletion(provider3);

        List<Provider> providers = allProviders.findByDistrictAndProviderId("districtB", "Aa");
        assertEquals(2, providers.size());
        assertTrue(providers.contains(provider2));
        assertTrue(providers.contains(provider3));
    }

    @Test
    public void shouldFindProviderByPrimaryMobileNumber(){
        String primaryMobile = "984567876";
        Provider savedProvider = new Provider("ab", primaryMobile, "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        addAndMarkForDeletion(savedProvider);

        Provider actualProvider = allProviders.findByMobileNumber(primaryMobile);

        assertThat(actualProvider, is(savedProvider));
    }

    @Test
    public void shouldFindProviderBySecondaryMobileNumber(){
        String primaryMobile = "primary";
        String secondaryMobile = "secondary";
        String tertiaryMobile = "tertiary";
        Provider savedProvider = new Provider("ab", primaryMobile, "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        savedProvider.setSecondaryMobile(secondaryMobile);
        savedProvider.setTertiaryMobile(tertiaryMobile);

        addAndMarkForDeletion(savedProvider);

        Provider actualProvider = allProviders.findByMobileNumber(secondaryMobile);
        assertThat(actualProvider, is(savedProvider));
    }

    @Test
    public void shouldFindProviderByTertiaryMobileNumber(){
        String primaryMobile = "primary";
        String secondaryMobile = "secondary";
        String tertiaryMobile = "tertiary";
        Provider savedProvider = new Provider("ab", primaryMobile, "districtA",
                DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
        savedProvider.setSecondaryMobile(secondaryMobile);
        savedProvider.setTertiaryMobile(tertiaryMobile);

        addAndMarkForDeletion(savedProvider);

        Provider actualProvider = allProviders.findByMobileNumber(tertiaryMobile);
        assertThat(actualProvider, is(savedProvider));
    }
}

