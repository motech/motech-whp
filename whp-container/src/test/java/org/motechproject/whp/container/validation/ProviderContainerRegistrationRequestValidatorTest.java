package org.motechproject.whp.container.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.now;

public class ProviderContainerRegistrationRequestValidatorTest {

    @Mock
    private ProviderContainerMappingService providerContainerMappingService;
    @Mock
    private ContainerRegistrationRequestValidator containerRegistrationRequestValidator;
    @Mock
    private ProviderService providerService;

    private ProviderContainerRegistrationRequestValidator validator;

    Provider validProvider = new Provider("validProvider", "123", "dist", now());

    @Before
    public void setUp()  {
        validator = new ProviderContainerRegistrationRequestValidator(containerRegistrationRequestValidator, providerContainerMappingService);
    }

    @Test
    public void shouldValidateProviderIdContainerMappingSuccessfully_whenProviderExists() {
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);
        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());

        List<String> validationErrors = validator.validate(registrationRequest);

        verify(providerService).findByProviderId(validProvider.getProviderId());
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void shouldValidateProviderIdContainerMappingWithErrors_whenProviderExists() {
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(false);

        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        verify(providerService).findByProviderId(validProvider.getProviderId());
        assertTrue(!validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Invalid container id : " + containerId));
    }

    @Test
    public void shouldNotValidateProviderIdContainerMapping_whenProviderDoesNotExist() {
        String unregisteredProviderId = "UnregisteredProviderId";
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(unregisteredProviderId, containerId, SputumTrackingInstance.IN_TREATMENT.getDisplayText());

        registrationRequestValidator.validate(registrationRequest);

        verify(providerService).findByProviderId(unregisteredProviderId);
        verify(providerContainerMappingService, never()).isValidContainerForProvider(unregisteredProviderId, containerId);
    }

}
