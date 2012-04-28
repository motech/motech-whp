package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class ProviderWebServiceTest extends SpringIntegrationTest {

    @Autowired
    private AllProviders allProviders;
    @Autowired
    private BeanValidator validator;
    @Autowired
    private RegistrationService registrationService;

    ProviderWebService whpProviderWebService;

    @Before
    public void setUp() {
        initMocks(this);
        whpProviderWebService = new ProviderWebService(validator, registrationService);
    }

    @Test
    public void shouldCreateProvider() throws OpenRosaRegistrationValidationException {
        ProviderWebRequest whpProviderWeb = new ProviderRequestBuilder().withDefaults().build();
        whpProviderWebService.createOrUpdate(whpProviderWeb);

        Provider provider = allProviders.findByProviderId("providerId");
        assertNotNull(provider);

        markForDeletion(provider);
    }

    @Test
    public void shouldThrowAnExceptionIfMandatoryFieldsAreAbsent() {
        try {
            ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withPrimaryMobile("9880000000").build();
            whpProviderWebService.createOrUpdate(providerWebRequest);
            fail("Should have exceptionThrown validation exception");
        } catch (OpenRosaRegistrationValidationException exception) {
            assertTrue(exception.getMessage().contains("field:district:may not be empty"));
            assertTrue(exception.getMessage().contains("field:date:may not be empty"));
        }
    }
}
