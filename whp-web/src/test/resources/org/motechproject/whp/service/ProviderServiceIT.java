package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.Validator;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class ProviderServiceIT extends SpringIntegrationTest {

    @Autowired
    private AllProviders allProviders;

    @Autowired
    private Validator validator;

    ProviderService whpProviderService;

    @Before
    public void setUp() {
        initMocks(this);
        whpProviderService = new ProviderService(allProviders, validator);
    }

    @Test
    public void shouldCreateProvider() {
        Provider whpProvider = new Provider("providerId", "9880123456", "district");
        whpProviderService.createOrUpdate(whpProvider);
        markForDeletion(whpProvider);

        assertNotNull(allProviders.get(whpProvider.getId()));
    }

    @Test
    public void shouldThrowAnExceptionIfMandatoryFieldsAreAbsent() {
        try {
            Provider provider = new Provider("P00001", "9880000000", null);
            whpProviderService.createOrUpdate(provider);
            fail("Should have thrown validation exception");
        } catch (WHPValidationException exception) {
            assertTrue(exception.getMessage().toString().contains("district may not be empty"));
        }
    }
}
