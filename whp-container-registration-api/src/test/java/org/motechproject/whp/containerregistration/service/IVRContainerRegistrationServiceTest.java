package org.motechproject.whp.containerregistration.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.containerregistration.api.request.ContainerVerificationRequest;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.request.ProviderVerificationRequest;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ContainerVerification;
import org.motechproject.whp.containerregistration.api.verification.ProviderVerification;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class IVRContainerRegistrationServiceTest {

    private IVRContainerRegistrationService ivrContainerRegistrationService;

    @Mock
    private ProviderVerification providerVerification;
    @Mock
    private ContainerVerification containerVerification;
    @Mock
    private ContainerRegistrationVerification containerRegistrationVerification;

    @Before
    public void setUp() {
        initMocks(this);
        ivrContainerRegistrationService = new IVRContainerRegistrationService(providerVerification, containerVerification, containerRegistrationVerification);
    }

    @Test
    public void shouldVerifyProviderVerificationRequest() {
        ProviderVerificationRequest request = new ProviderVerificationRequest();

        ivrContainerRegistrationService.verifyProviderVerificationRequest(request);

        verify(providerVerification).verifyRequest(request);
    }

    @Test
    public void shouldVerifyContainerVerificationRequest() {
        ContainerVerificationRequest request = new ContainerVerificationRequest();

        ivrContainerRegistrationService.verifyContainerVerificationRequest(request);

        verify(containerVerification).verifyRequest(request);
    }

    @Test
    public void shouldVerifyContainerRegistrationVerificationRequest() {
        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest();

        ivrContainerRegistrationService.verifyContainerRegistrationVerificationRequest(request);

        verify(containerRegistrationVerification).verifyRequest(request);
    }
}
