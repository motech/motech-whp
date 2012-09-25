package org.motechproject.whp.container.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class RegistrationRequestValidatorTest {
    @Mock
    private ContainerService containerService;
    @Mock
    private SputumTrackingProperties sputumTrackingProperties;
    @Mock
    private ProviderService providerService;
    @Mock
    private ProviderContainerMappingService providerContainerMappingService;
    private RegistrationRequestValidator registrationRequestValidator;
    private Provider validProvider;

    @Before
    public void setUp() {
        validProvider = new Provider("validProvider", "123", "dist", now());
        initMocks(this);
        when(sputumTrackingProperties.getContainerIdMaxLength()).thenReturn(11);
        when(providerService.findByProviderId(validProvider.getProviderId())).thenReturn(validProvider);
        registrationRequestValidator = new RegistrationRequestValidator(containerService, providerService, providerContainerMappingService, sputumTrackingProperties);
    }

    @Test
    public void shouldValidateDuplicateContainerId() {
        String containerID = "12345678910";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerID, SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        when(containerService.exists(containerID)).thenReturn(true);

        List<String> errors = registrationRequestValidator.validate(registrationRequest);

        verify(containerService).exists(containerID);
        assertTrue(errors.contains("Container Id already exists."));
    }

    @Test
    public void shouldValidateContainerId_notHavingStipulatedNumberOfDigits() {
        ContainerRegistrationRequest request = new ContainerRegistrationRequest(validProvider.getProviderId(), "12345", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> invalidLengthErrors = registrationRequestValidator.validate(request);
        assertTrue(invalidLengthErrors.contains("Container Id must be of 11 digits in length"));
    }

    @Test
    public void shouldValidateContainerId_havingNonNumericCharacters() {
        ContainerRegistrationRequest request = new ContainerRegistrationRequest(validProvider.getProviderId(), "123456789a", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> nonNumericErrors = registrationRequestValidator.validate(request);
        assertTrue(nonNumericErrors.contains("Container Id must be of 11 digits in length"));
    }

    @Test
    public void shouldValidateInstance() {
        ContainerRegistrationRequest request = new ContainerRegistrationRequest(validProvider.getProviderId(), "12345678910", "invalid_instance");
        List<String> invalidInstanceErrors = registrationRequestValidator.validate(request);
        assertTrue(invalidInstanceErrors.contains("Invalid instance : invalid_instance"));
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderIdIsUndefined() {
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest("", "12345678910", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        assertTrue(validationErrors.contains("Invalid provider id : "));
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderExists() {
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);

        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);


        verify(providerService).findByProviderId(validProvider.getProviderId());
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderDoesNotExist() {
        String unregisteredProviderId = "UnregisteredProviderId";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(unregisteredProviderId, "11111111111", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        verify(providerService).findByProviderId(unregisteredProviderId);
        assertTrue(!validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Provider not registered : UnregisteredProviderId"));
    }

    @Test
    public void shouldValidateProviderIdContainerMappingSuccessfully_whenProviderExists() {
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);

        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

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
