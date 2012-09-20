package org.motechproject.whp.container.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class RegistrationRequestValidatorTest {
    @Mock
    private ContainerService containerService;
    @Mock
    private SputumTrackingProperties sputumTrackingProperties;
    @Mock
    private ProviderService providerService;
    private RegistrationRequestValidator registrationRequestValidator;
    private Provider validProvider;

    @Before
    public void setUp() {
        validProvider = new Provider("validProvider", "123", "dist", now());
        initMocks(this);
        when(sputumTrackingProperties.getContainerIdMaxLength()).thenReturn(11);
        when(providerService.findByProviderId(validProvider.getProviderId())).thenReturn(validProvider);
        registrationRequestValidator = new RegistrationRequestValidator(containerService, providerService, sputumTrackingProperties);
    }

    @Test
    public void shouldValidateDuplicateContainerId() {
        String containerID = "12345678910";
        RegistrationRequest registrationRequest = new RegistrationRequest(validProvider.getProviderId(), containerID, SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        when(containerService.exists(containerID)).thenReturn(true);

        List<String> errors = registrationRequestValidator.validate(registrationRequest);

        verify(containerService).exists(containerID);
        assertTrue(errors.contains("Container Id already exists."));
    }

    @Test
    public void shouldValidateContainerId_notHavingStipulatedNumberOfDigits() {
        RegistrationRequest request = new RegistrationRequest(validProvider.getProviderId(), "12345", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> invalidLengthErrors = registrationRequestValidator.validate(request);
        assertTrue(invalidLengthErrors.contains("Container Id must be of 11 digits in length"));
    }

    @Test
    public void shouldValidateContainerId_havingNonNumericCharacters() {
        RegistrationRequest request = new RegistrationRequest(validProvider.getProviderId(), "123456789a", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> nonNumericErrors = registrationRequestValidator.validate(request);
        assertTrue(nonNumericErrors.contains("Container Id must be of 11 digits in length"));
    }

    @Test
    public void shouldValidateInstance() {
        RegistrationRequest request = new RegistrationRequest(validProvider.getProviderId(), "12345678910", "invalid_instance");
        List<String> invalidInstanceErrors = registrationRequestValidator.validate(request);
        assertTrue(invalidInstanceErrors.contains("Invalid instance : invalid_instance"));
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderIdIsUndefined() {
        RegistrationRequest registrationRequest = new RegistrationRequest("", "12345678910", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        assertTrue(validationErrors.contains("Invalid provider id : "));
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderExists() {
        RegistrationRequest registrationRequest = new RegistrationRequest(validProvider.getProviderId(), "11111111111", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        verify(providerService).findByProviderId(validProvider.getProviderId());
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderDoesNotExist() {
        String unregisteredProviderId = "UnregisteredProviderId";
        RegistrationRequest registrationRequest = new RegistrationRequest(unregisteredProviderId, "11111111111", SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        verify(providerService).findByProviderId(unregisteredProviderId);
        assertTrue(!validationErrors.isEmpty());
        assertTrue(validationErrors.contains("Provider not registered : UnregisteredProviderId"));
    }
}
