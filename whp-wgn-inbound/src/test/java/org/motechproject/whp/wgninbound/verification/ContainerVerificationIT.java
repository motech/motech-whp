package org.motechproject.whp.wgninbound.verification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.wgninbound.request.ContainerVerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:applicationWHPWgnInputContext.xml")
public class ContainerVerificationIT {

    @Autowired
    @ReplaceWithMock
    ProviderVerification providerVerification;

    @Autowired
    @ReplaceWithMock
    ProviderContainerMappingService mappingService;

    @Autowired
    @ReplaceWithMock
    ContainerService containerService;

    @Autowired
    AllProviders allProviders;

    @Autowired
    ContainerVerification containerVerification;

    @Before
    public void setup() {
        reset(providerVerification, mappingService, containerService);
    }

    @Test
    public void shouldVerifyMSISDN() {
        String mobileNumber = "1234567890";

        when(providerVerification.verifyMobileNumber(mobileNumber)).thenReturn(new WHPError(WHPErrorCode.INVALID_PHONE_NUMBER));
        containerVerification.verify(new ContainerVerificationRequest(mobileNumber, "containerId", "callId"));
        verify(providerVerification).verifyMobileNumber(mobileNumber);
    }

    @Test
    public void shouldReturnFailureWhenMSISDNIsEmpty() {
        String emptyMSISDN = "";
        String containerId = "containerId";

        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(emptyMSISDN, containerId, "callId"));

        assertTrue(result.isError());
    }

    @Test
    public void shouldReturnFailureWhenCallIdIsEmpty() {
        String mobileNumber = "1234567890";
        String containerId = "containerId";
        String emptyCallId = "";

        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(mobileNumber, containerId, emptyCallId));
        assertTrue(result.isError());
    }

    @Test
    public void shouldReturnFailureWhenContainerIdIsEmpty() {
        String mobileNumber = "1234567890";
        String emptyContainerId = "";

        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(mobileNumber, emptyContainerId, "callId"));

        assertTrue(result.isError());
    }

    @Test
    public void shouldRespondWithFailureWhenContainerCannotBeRegisteredByProvider() {
        String msisdn = "1234567890";
        String providerId = "providerId";
        String containerId = "containerId";

        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withPrimaryMobileNumber(msisdn).build();
        allProviders.add(provider);

        when(mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)).thenReturn(false);
        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(msisdn, containerId, "callId"));

        assertEquals(WHPErrorCode.INVALID_CONTAINER_ID, result.getErrors().get(0).getErrorCode());
        assertEquals("The container Id entered  is invalid", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldRespondWithFailureWhenContainerAlreadyRegistered() {
        String msisdn = "1234567890";
        String providerId = "providerId";
        String containerId = "containerId";

        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withPrimaryMobileNumber(msisdn).build();
        allProviders.add(provider);

        when(containerService.exists(containerId)).thenReturn(true);
        when(mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)).thenReturn(true);

        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(msisdn, containerId, "callId"));

        assertEquals(WHPErrorCode.CONTAINER_ALREADY_REGISTERED, result.getErrors().get(0).getErrorCode());
        assertEquals("The container Id is already registered", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldRespondWithFailureWhenContainerAlreadyRegisteredWhenContainerDoesNotFallUnderProviderRange() {
        String msisdn = "1234567890";
        String providerId = "providerId";
        String containerId = "containerId";

        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withPrimaryMobileNumber(msisdn).build();
        allProviders.add(provider);

        when(containerService.exists(containerId)).thenReturn(true);
        when(mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)).thenReturn(false);

        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(msisdn, containerId, "callId"));

        assertEquals(WHPErrorCode.CONTAINER_ALREADY_REGISTERED, result.getErrors().get(0).getErrorCode());
        assertEquals("The container Id is already registered", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldRespondWithSuccessWhenContainerCanBeRegisteredByProvider() {
        String msisdn = "1234567890";
        String providerId = "providerId";
        String containerId = "containerId";

        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withPrimaryMobileNumber(msisdn).build();
        allProviders.add(provider);

        when(containerService.exists(containerId)).thenReturn(false);
        when(mappingService.isValidContainerForProvider(provider.getProviderId(), containerId)).thenReturn(true);

        VerificationResult result = containerVerification.verifyRequest(new ContainerVerificationRequest(msisdn, containerId, "callId"));
        assertTrue(result.isSuccess());
    }

    @After
    public void tearDown() {
        allProviders.removeAll();
    }
}
