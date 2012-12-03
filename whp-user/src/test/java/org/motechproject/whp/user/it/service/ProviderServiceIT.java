package org.motechproject.whp.user.it.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationUserContext.xml")
public class ProviderServiceIT extends SpringIntegrationTest {

    @Autowired
    ProviderService providerService;

    @Autowired
    AllProviders allProviders;

    @Autowired
    AllMotechWebUsers allMotechWebUsers;

    @Autowired
    private AllDistricts allDistricts;

    private District district;

    @Before
    public void setUp() {
        district = new District("district");
        allDistricts.add(district);
    }

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
        markForDeletion(allMotechWebUsers.getAll().toArray());
        allDistricts.remove(district);
    }

    @Test
    public void shouldCreateProvider() {
        String providerId = "providerId";
        String primaryMobile = "1234567890";
        String secondaryMobile = "0987654321";
        String tertiaryMobile = "1111111111";
        String districtName = "Muzzafarpur";
        DateTime now = now();

        ProviderRequest providerRequest = new ProviderRequest(providerId, districtName, primaryMobile, now);
        providerRequest.setSecondaryMobile(secondaryMobile);
        providerRequest.setTertiaryMobile(tertiaryMobile);
        District district = new District(districtName);
        allDistricts.add(district);

        providerService.registerProvider(providerRequest);

        allDistricts.remove(district);
        Provider provider = allProviders.findByProviderId(providerId);

        assertEquals(providerId.toLowerCase(), provider.getProviderId());
        assertEquals(primaryMobile, provider.getPrimaryMobile());
        assertEquals(secondaryMobile, provider.getSecondaryMobile());
        assertEquals(tertiaryMobile, provider.getTertiaryMobile());
        assertEquals(districtName, provider.getDistrict());
        assertEquals(now, provider.getLastModifiedDate());
    }

    @Test
    public void shouldRegisterUserAsInactiveByDefault() throws WebSecurityException {
        DateTime now = DateUtil.now();
        ProviderRequest providerRequest = new ProviderRequest("providerId", "district", "1111111111", now);
        providerRequest.setSecondaryMobile("secondary");
        providerRequest.setTertiaryMobile("tertiary");

        providerService.registerProvider(providerRequest);
        Provider provider = allProviders.findByProviderId("providerId");

        assertNotNull(provider);
        assertEquals(providerRequest.getProviderId(), provider.getProviderId());
        assertEquals(providerRequest.getPrimaryMobile(), provider.getPrimaryMobile());
        assertEquals(providerRequest.getSecondaryMobile(), provider.getSecondaryMobile());
        assertEquals(providerRequest.getTertiaryMobile(), provider.getTertiaryMobile());
        assertEquals(providerRequest.getDistrict(), provider.getDistrict());
        assertEquals(now, provider.getLastModifiedDate());
    }

    @Test(expected = WHPRuntimeException.class)
    @Ignore // TODO : Fix AuthenticationService to not allow duplicate usernames
    public void shouldThrowWhpRunTimeExceptionIfRegisterThrowsException() throws WebSecurityException {
        ProviderRequest providerRequest = new ProviderRequest("providerId", "district", "1111111111", DateUtil.now());
        providerService.registerProvider(providerRequest);
        providerService.registerProvider(providerRequest);
    }
}
