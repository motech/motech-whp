package org.motechproject.whp.container.validation;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.ContainerId;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

public class CommonContainerRegistrationValidatorTest {
    @Mock
    private ContainerService containerService;
    @Mock
    private SputumTrackingProperties sputumTrackingProperties;
    @Mock
    private ProviderService providerService;
    @Mock
    private ProviderContainerMappingService providerContainerMappingService;
    private CommonContainerRegistrationValidator registrationRequestValidator;
    private Provider validProvider;

    @Before
    public void setUp() {
        validProvider = new Provider("validProvider", "123", "dist", now());
        initMocks(this);
        when(sputumTrackingProperties.getContainerIdMaxLength()).thenReturn(11);
        when(providerService.findByProviderId(validProvider.getProviderId())).thenReturn(validProvider);
        registrationRequestValidator = new CommonContainerRegistrationValidator(containerService, providerService, sputumTrackingProperties);
    }

    @Test
    public void shouldValidateDuplicateContainerId() {
        String containerID = "12345";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerID, RegistrationInstance.InTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        when(containerService.exists(new ContainerId(validProvider.getProviderId(), containerID, ON_BEHALF_OF_PROVIDER).value())).thenReturn(true);

        List<ErrorWithParameters> errors = registrationRequestValidator.validate(registrationRequest);

        verify(containerService).exists(new ContainerId(validProvider.getProviderId(), containerID, ON_BEHALF_OF_PROVIDER).value());
        assertTrue(errors.contains(new ErrorWithParameters("container.already.registered.error")));
    }

    @Test
    @Ignore("Not doing 11 digit validation anymore")
    public void shouldValidateContainerId_notHavingStipulatedNumberOfDigits() {
        ContainerRegistrationRequest request = new ContainerRegistrationRequest(validProvider.getProviderId(), "12345", RegistrationInstance.InTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        List<ErrorWithParameters> invalidLengthErrors = registrationRequestValidator.validate(request);
        assertTrue(invalidLengthErrors.contains(new ErrorWithParameters("container.id.length.error", "11")));
    }

    @Test
    @Ignore
    public void shouldValidateContainerId_havingNonNumericCharacters() {
        ContainerRegistrationRequest request = new ContainerRegistrationRequest(validProvider.getProviderId(), "1234a", RegistrationInstance.InTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        List<ErrorWithParameters> nonNumericErrors = registrationRequestValidator.validate(request);
        assertTrue(nonNumericErrors.contains(new ErrorWithParameters("container.id.length.error", "11")));
    }

    @Test
    public void shouldValidateInstance() {
        ContainerRegistrationRequest request = new ContainerRegistrationRequest(validProvider.getProviderId(), "12345", "invalid_instance", ChannelId.WEB.name(), null);
        List<ErrorWithParameters> invalidInstanceErrors = registrationRequestValidator.validate(request);
        assertTrue(invalidInstanceErrors.contains(new ErrorWithParameters("invalid.instance.error", "invalid_instance")));
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderIdIsUndefined() {
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest("", "12345", RegistrationInstance.InTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        List<ErrorWithParameters> validationErrors = registrationRequestValidator.validate(registrationRequest);
        assertTrue(validationErrors.contains(new ErrorWithParameters("provider.id.invalid.error", "")));
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderExists() {
        String containerId = "11111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, RegistrationInstance.InTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);

        List<ErrorWithParameters> validationErrors = registrationRequestValidator.validate(registrationRequest);


        verify(providerService).findByProviderId(validProvider.getProviderId());
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void shouldValidatePresenceOfProviderId_whenProviderDoesNotExist() {
        String unregisteredProviderId = "UnregisteredProviderId";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(unregisteredProviderId, "11111", RegistrationInstance.InTreatment.getDisplayText(), ChannelId.WEB.name(), null);
        List<ErrorWithParameters> validationErrors = registrationRequestValidator.validate(registrationRequest);

        verify(providerService).findByProviderId(unregisteredProviderId);
        assertTrue(!validationErrors.isEmpty());
        assertTrue(validationErrors.contains(new ErrorWithParameters("provider.not.registered.error", unregisteredProviderId)));
    }
}
