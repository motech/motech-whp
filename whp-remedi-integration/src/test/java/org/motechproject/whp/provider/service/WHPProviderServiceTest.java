package org.motechproject.whp.provider.service;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;

import static org.mockito.Mockito.verify;

/**
 * User: preethi
 * Date: 16/4/12
 */
@RunWith(MockitoJUnitRunner.class)
public class WHPProviderServiceTest extends TestCase {

    @Mock
    private AllProviders allProviders;
    WHPProviderService whpProviderService;

    @Before
    public void setUp() {
        whpProviderService = new WHPProviderService(allProviders);
    }

    @Test
    public void testCreateOrUpdate() throws Exception {
        String providerId = "providerId";
        Provider whpProvider = new Provider();
        whpProvider.setProviderId(providerId);
        whpProvider.setDistrict("district");
        whpProvider.setPrimaryMobile("9880123456");
        whpProviderService.createOrUpdate(whpProvider);

        verify(allProviders).addOrReplace(whpProvider);
    }

    @Test(expected = WHPValidationException.class)
    public void shouldThrowAnExceptionIfMandatoryFieldsAreAbsent() {
        Provider provider = new Provider("P00001", "984567876");
        whpProviderService.createOrUpdate(provider);
    }
}
