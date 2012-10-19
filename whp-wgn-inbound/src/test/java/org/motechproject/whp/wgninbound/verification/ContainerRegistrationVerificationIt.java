package org.motechproject.whp.wgninbound.verification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.wgninbound.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.wgninbound.request.ValidatorPool;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath:applicationWHPWgnInputContext.xml")
public class ContainerRegistrationVerificationIt {

    @Autowired
    @ReplaceWithMock
    ValidatorPool validatorPool;

    @Captor
    ArgumentCaptor<WHPErrors> whpErrors;

    @Autowired
    ContainerRegistrationVerification containerRegistrationVerification;

    @Before
    public void setUp() {
        reset(validatorPool);
        when(validatorPool.verifyMobileNumber(anyString(), any(WHPErrors.class))).thenReturn(validatorPool);
        when(validatorPool.verifyContainerMapping(anyString(), anyString(), any(WHPErrors.class))).thenReturn(validatorPool);
        when(validatorPool.verifyPhase(anyString(), any(WHPErrors.class))).thenReturn(validatorPool);
        initMocks(this);
    }

    @Test
    public void shouldReturnFailureWhenMSISDNIsEmpty() {
        String emptyMSISDN = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest(emptyMSISDN, "containerId", "callId", "phase"));

        assertTrue(result.isError());
        assertEquals("field:msisdn:should be atleast 10 digits in length", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldReturnFailureWhenCallIdIsEmpty() {
        String emptyCallId = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", "containerId", emptyCallId, "phase"));

        assertTrue(result.isError());
        assertEquals("field:call_id:value should not be null", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldReturnFailureWhenContainerIdIsEmpty() {
        String emptyContainerId = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", emptyContainerId, "callId", "phase"));

        assertTrue(result.isError());
        assertEquals("field:container_id:value should not be null", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldReturnFailureWhenPhaseIsEmpty() {
        String emptyPhase = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", "containerId", "callId", emptyPhase));

        assertTrue(result.isError());
        assertEquals("field:phase:value should not be null", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldVerifyRequest() {
        String msisdn = "1234567890";
        String containerId = "containerId";
        String callId = "callId";
        String phase = "phase";
        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest(msisdn, containerId, callId, phase);
        when(validatorPool.verifyMobileNumber(eq(msisdn), whpErrors.capture())).thenReturn(validatorPool);
        when(validatorPool.verifyContainerMapping(eq(msisdn), eq(containerId), whpErrors.capture())).thenReturn(validatorPool);
        when(validatorPool.verifyPhase(eq(phase), whpErrors.capture())).thenReturn(validatorPool);

        WHPErrors errors = containerRegistrationVerification.verify(request);

        assertTrue(errors.isEmpty());
    }
}
