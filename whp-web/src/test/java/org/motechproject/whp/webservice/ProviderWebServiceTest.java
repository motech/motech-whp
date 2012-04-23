package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.provider.domain.Provider;
import org.motechproject.whp.provider.repository.AllProviders;
import org.motechproject.whp.request.ProviderRequest;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.validation.Errors;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class ProviderWebServiceTest {

    @Mock
    AllProviders allProviders;
    @Mock
    BeanValidator validator;

    ProviderWebService providerWebService;

    @Before
    public void setUp() {
        initMocks(this);
        providerWebService = new ProviderWebService(allProviders, validator);
    }

    @Test
    public void shouldCreateOrUpdateProvider() throws OpenRosaRegistrationValidationException {
        ProviderRequest providerRequest = new ProviderRequestBuilder().withDefaults().build();
        providerWebService.createOrUpdate(providerRequest);

        ArgumentCaptor<Provider> providerArgumentCaptor = ArgumentCaptor.forClass(Provider.class);

        verify(validator).validate(eq(providerRequest), eq(ValidationScope.create), Matchers.<Errors>any());
        verify(allProviders).addOrReplace(providerArgumentCaptor.capture());

        Provider provider = providerArgumentCaptor.getValue();
        assertEquals("providerId", provider.getProviderId());
        assertEquals("9880123456", provider.getPrimaryMobile());
        assertEquals("9880123457", provider.getSecondaryMobile());
        assertEquals("9880123458", provider.getTertiaryMobile());
        assertEquals("Patna", provider.getDistrict());
        assertEquals("12/01/2012 10:10:10", provider.getLastModifiedDate().toString("dd/MM/YYYY HH:mm:ss"));
    }

}
