package org.motechproject.whp.containerregistration.api.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.containerregistration.api.verification.ValidatorPool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ValidatorPoolTest {
    @Mock
    private ProviderService providerService;
    @Mock
    private ContainerService containerService;
    @Mock
    private ProviderContainerMappingService mappingService;

    ValidatorPool validatorPool;

    @Before
    public void setUp() {
        initMocks(this);
        validatorPool = new ValidatorPool(providerService, containerService, mappingService);
    }

    @Test
    public void shouldVerifyInvalidMobileNumber() {
        String mobileNumber = "1234567890";
        WHPErrors whpErrors = new WHPErrors();
        when(providerService.findByMobileNumber(mobileNumber)).thenReturn(null);

        validatorPool.verifyMobileNumber(mobileNumber, whpErrors);

        verify(providerService, times(1)).findByMobileNumber(mobileNumber);
        assertFalse(whpErrors.isEmpty());
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER, whpErrors.get(0).getErrorCode());
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER.getMessage(), whpErrors.get(0).getMessage());
    }

    @Test
    public void shouldVerifyContainerMappingForInvalidMobileNumber() {
        String mobileNumber = "1234567890";
        String containerId = "containerId";
        WHPErrors whpErrors = new WHPErrors();
        when(providerService.findByMobileNumber(mobileNumber)).thenReturn(null);

        validatorPool.verifyContainerMapping(mobileNumber, containerId, whpErrors);

        verify(providerService, times(1)).findByMobileNumber(mobileNumber);
        assertFalse(whpErrors.isEmpty());
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER, whpErrors.get(0).getErrorCode());
        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER.getMessage(), whpErrors.get(0).getMessage());
    }

    @Test
    public void shouldVerifyContainerMappingForAlreadyRegisteredContainer() {
        String mobileNumber = "1234567890";
        String containerId = "containerId";
        WHPErrors whpErrors = new WHPErrors();
        when(providerService.findByMobileNumber(mobileNumber)).thenReturn(new Provider());
        when(containerService.exists(containerId)).thenReturn(true);

        validatorPool.verifyContainerMapping(mobileNumber, containerId, whpErrors);

        verify(providerService, times(1)).findByMobileNumber(mobileNumber);
        verify(containerService, times(1)).exists(containerId);
        assertFalse(whpErrors.isEmpty());
        assertEquals(WHPErrorCode.CONTAINER_ALREADY_REGISTERED, whpErrors.get(0).getErrorCode());
        assertEquals(WHPErrorCode.CONTAINER_ALREADY_REGISTERED.getMessage(), whpErrors.get(0).getMessage());
    }

    @Test
    public void shouldVerifyContainerMappingForInvalidContainer() {
        String mobileNumber = "1234567890";
        String containerId = "containerId";
        String providerId = "providerId";
        WHPErrors whpErrors = new WHPErrors();
        Provider provider = new Provider();
        provider.setProviderId(providerId);
        when(providerService.findByMobileNumber(mobileNumber)).thenReturn(provider);
        when(containerService.exists(containerId)).thenReturn(false);
        when(mappingService.isValidContainerForProvider(providerId.toLowerCase(), containerId)).thenReturn(false);

        validatorPool.verifyContainerMapping(mobileNumber, containerId, whpErrors);

        verify(providerService, times(1)).findByMobileNumber(mobileNumber);
        verify(containerService, times(1)).exists(containerId);
        verify(mappingService, times(1)).isValidContainerForProvider(providerId.toLowerCase(), containerId);
        assertFalse(whpErrors.isEmpty());
        assertEquals(WHPErrorCode.INVALID_CONTAINER_ID, whpErrors.get(0).getErrorCode());
        assertEquals("The container Id entered  is invalid", whpErrors.get(0).getMessage());
    }

    @Test
    public void shouldVerifyPhaseForInvalidInstance() {
        WHPErrors whpErrors = new WHPErrors();

        validatorPool.verifyPhase(SputumTrackingInstance.PreTreatment.getDisplayText(), whpErrors);

        assertFalse(whpErrors.isEmpty());
        assertEquals(WHPErrorCode.INVALID_PHASE, whpErrors.get(0).getErrorCode());
        assertEquals(WHPErrorCode.INVALID_PHASE.getMessage(), whpErrors.get(0).getMessage());
    }
}
