package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.motechproject.whp.request.ProviderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.Validator;

import static junit.framework.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class ProviderWebServiceIT extends SpringIntegrationTest {

    @Autowired
    private AllProviders allProviders;

    @Autowired
    private Validator validator;

    ProviderWebService whpProviderWebService;

    @Before
    public void setUp() {
        initMocks(this);
        whpProviderWebService = new ProviderWebService(allProviders, validator);
    }

    @Test
    public void shouldCreateProvider() throws OpenRosaRegistrationValidationException {
        ProviderRequest whpProvider = new ProviderRequestBuilder().withDefaults().build();
        whpProviderWebService.createOrUpdate(whpProvider);

        Provider provider = allProviders.findByProviderId("providerId");
        assertNotNull(provider);

        markForDeletion(provider);
    }

    @Test
    public void shouldThrowAnExceptionIfMandatoryFieldsAreAbsent() {
        try {
            ProviderRequest providerRequest = new ProviderRequestBuilder().withProviderId("P00001").withPrimaryMobile("9880000000").build();
            whpProviderWebService.createOrUpdate(providerRequest);
            fail("Should have thrown validation exception");
        } catch (OpenRosaRegistrationValidationException exception) {
            assertTrue(exception.getMessage().contains("district may not be empty"));
            assertTrue(exception.getMessage().contains("date may not be empty"));
        }
    }
}
