package org.motechproject.whp.webservice.service;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.common.utils.SpringIntegrationTest;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:applicationWebServiceContext.xml")
public class ProviderWebServiceIT extends SpringIntegrationTest {

    @Autowired
    private AllProviders allProviders;
    @Autowired
    private RequestValidator validator;
    @Autowired
    private ProviderService providerServices;

    ProviderWebService providerWebService;

    @Before
    public void setUp() {
        initMocks(this);
        providerWebService = new ProviderWebService(validator, providerServices);
    }

    @Test
    public void shouldCreateProvider() {
        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().build();
        providerWebService.createOrUpdate(whpProviderWeb);

        Provider provider = allProviders.findByProviderId("providerId");
        assertNotNull(provider);

        markForDeletion(provider);
    }

    @Test
    public void shouldThrowExceptionWhenProviderDataIsInvalid() {
        exceptionThrown.expect(OpenRosaRegistrationValidationException.class);
        ProviderWebRequest invalidProviderRequest = new ProviderRequestBuilder()
                .withDefaults()
                .withProviderId(null)
                .build();

        providerWebService.createOrUpdate(invalidProviderRequest);
    }

}
