package org.motechproject.whp.service;

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
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by IntelliJ IDEA.
 * User: preethi
 * Date: 16/4/12
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProviderServiceTest extends TestCase {

    @Mock
    private AllProviders allProviders;
    ProviderService whpProviderService;
    
    @Before
    public void setUp() {
        whpProviderService = new ProviderService(allProviders);
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

    @Test
    public void shouldThrowAnExceptionIfMandatoryFieldsAreAbsent() {
        try {
            Provider provider = new Provider("P00001", "984567876");
            whpProviderService.createOrUpdate(provider);
            fail();
        } catch (WHPValidationException exception) {

        }
        verifyNoMoreInteractions(allProviders);

    }
}
