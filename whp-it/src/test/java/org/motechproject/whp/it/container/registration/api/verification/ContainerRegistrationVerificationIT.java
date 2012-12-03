package org.motechproject.whp.it.container.registration.api.verification;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.ArgumentCaptor;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.containerregistration.api.request.IvrContainerRegistrationRequest;
import org.motechproject.whp.containerregistration.api.response.VerificationResult;
import org.motechproject.whp.containerregistration.api.verification.ContainerRegistrationVerification;
import org.motechproject.whp.containerregistration.api.verification.ValidatorPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:applicationContainerRegistrationApiContext.xml")
public class ContainerRegistrationVerificationIT {

    @Autowired
    @ReplaceWithMock
    ValidatorPool validatorPool;

    @Autowired
    ContainerRegistrationVerification containerRegistrationVerification;

    @Test
    public void shouldReturnFailureWhenMSISDNIsEmpty() {
        String emptyMSISDN = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest(emptyMSISDN, "10000000000", "callId", "phase"));

        assertTrue(result.isError());
        Assert.assertEquals("field:msisdn:should be atleast 10 digits in length", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldReturnFailureWhenCallIdIsEmpty() {
        String emptyCallId = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", "10000000000", emptyCallId, "phase"));

        assertTrue(result.isError());
        Assert.assertEquals("field:call_id:value should not be null", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldReturnFailureWhenContainerIdIsEmpty() {
        String emptyContainerId = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", emptyContainerId, "callId", "phase"));

        assertTrue(result.isError());
        errorContains("field:container_id:value should not be null", result.getErrors());
        errorContains("field:container_id:must be less than or equal to 99999999999", result.getErrors());
    }

    @Test
    public void shouldReturnFailureWhenContainerIdIsNotOfDesiredNumericAndLength() {
        String numericLessLength = "1234";
        VerificationResult result1 = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", numericLessLength, "callId", "phase"));
        errorContains("field:container_id:must be less than or equal to 99999999999", result1.getErrors());

        String nonNumeric = "1234as";
        VerificationResult result2 = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", nonNumeric, "callId", "phase"));
        errorContains("field:container_id:must be less than or equal to 99999999999", result2.getErrors());
    }

    @Test
    public void shouldReturnFailureWhenPhaseIsEmpty() {
        String emptyPhase = "";

        VerificationResult result = containerRegistrationVerification.verifyRequest(new IvrContainerRegistrationRequest("1234567890", "10000000000", "callId", emptyPhase));

        assertTrue(result.isError());
        Assert.assertEquals("field:phase:value should not be null", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldVerifyRequest() {
        String msisdn = "1234567890";
        String containerId = "10000000000";
        String callId = "callId";
        String phase = "phase";

        ArgumentCaptor<WHPErrors> whpErrors = ArgumentCaptor.forClass(WHPErrors.class);
        IvrContainerRegistrationRequest request = new IvrContainerRegistrationRequest(msisdn, containerId, callId, phase);
        when(validatorPool.verifyMobileNumber(eq(msisdn), whpErrors.capture())).thenReturn(validatorPool);
        when(validatorPool.verifyContainerMapping(eq(msisdn), eq(containerId), whpErrors.capture())).thenReturn(validatorPool);
        when(validatorPool.verifyPhase(eq(phase), whpErrors.capture())).thenReturn(validatorPool);

        WHPErrors errors = containerRegistrationVerification.verify(request);

        assertTrue(errors.isEmpty());
    }

    private boolean errorContains(String expectedMessage, List<WHPError> errors) {
        boolean found = false;
        for (WHPError error : errors) {
            found |= StringUtils.equals(expectedMessage, error.getMessage());
        }
        return found;
    }
}
