package org.motechproject.whp.user.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProviderServiceTest  {

    @Mock
    private MotechAuthenticationService motechAuthenticationService;

    @Mock
    private AllProviders allProviders;

    private ProviderService providerService;


    @Before
    public void setUp() {
        providerService = new ProviderService(motechAuthenticationService, allProviders);
    }

    @Test
    public void shouldFetchProvidersByDistrictAndProviderId() {
        providerService.fetchBy("district", "providerId");
        verify(allProviders).findByDistrictAndProviderId("district", "providerId");
    }

    @Test
    public void shouldFetchProvidersByOnlyDistrict_WhenProviderIdIsEmpty() {
        providerService.fetchBy("district", "");
        verify(allProviders).findByDistrict("district");
    }

    @Test
    public void shouldFetchAllProviderWebUsers() {
        MotechUser motechUser = new MotechUser(new MotechWebUser("provider", "password", "externalId", null));
        when(motechAuthenticationService.findByRole(WHPRole.PROVIDER.name())).thenReturn(Arrays.asList(motechUser));

        Map<String,MotechUser> motechUserMap = providerService.fetchAllWebUsers();

        assertNotNull(motechUserMap);
        assertEquals(1, motechUserMap.size());
        assertTrue(motechUserMap.containsKey("provider"));
        assertEquals(motechUser, motechUserMap.get("provider"));

    }
}
