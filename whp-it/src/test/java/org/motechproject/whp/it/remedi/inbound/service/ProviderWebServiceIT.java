package org.motechproject.whp.it.remedi.inbound.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;
import org.motechproject.whp.webservice.service.ProviderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class ProviderWebServiceIT extends SpringIntegrationTest {

    @Autowired
    private AllProviders allProviders;
    @Autowired
    private RequestValidator validator;
    @Autowired
    private ProviderService providerServices;

    ProviderWebService providerWebService;
    @Autowired
    private AllDistricts allDistricts;
    private District district;

    @Before
    public void setUp() {
        initMocks(this);
        district = new District("Patna");
        allDistricts.add(district);
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

    @After
    public void tearDown() {
        allDistricts.remove(district);
    }
}
