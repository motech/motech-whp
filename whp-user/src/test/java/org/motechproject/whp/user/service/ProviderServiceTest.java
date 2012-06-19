package org.motechproject.whp.user.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.common.exception.WHPRuntimeException;
import org.motechproject.common.utils.SpringIntegrationTest;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationUserContext.xml")
public class ProviderServiceTest extends SpringIntegrationTest {

    @Autowired
    ProviderService providerService;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllMotechWebUsers allMotechWebUsers;

    @After
    public void tearDown() {
        markForDeletion(allProviders.getAll().toArray());
        markForDeletion(allMotechWebUsers.getAll().toArray());
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
