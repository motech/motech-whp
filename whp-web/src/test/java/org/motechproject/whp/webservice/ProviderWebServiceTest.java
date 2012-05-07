package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class ProviderWebServiceTest extends SpringIntegrationTest {

    @Autowired
    private AllProviders allProviders;
    @Autowired
    private RequestValidator validator;
    @Autowired
    private RegistrationService registrationService;

    ProviderWebService whpProviderWebService;

    @Before
    public void setUp() {
        initMocks(this);
        whpProviderWebService = new ProviderWebService(validator, registrationService);
    }

    @Test
    public void shouldCreateProvider(){
        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().build();
        whpProviderWebService.createOrUpdate(whpProviderWeb);

        Provider provider = allProviders.findByProviderId("providerId");
        assertNotNull(provider);

        markForDeletion(provider);
    }
}
