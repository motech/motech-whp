package org.motechproject.whp.it.verification;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrors;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.motechproject.whp.wgninbound.verification.ProviderVerification;
import org.motechproject.whp.wgninbound.verification.ValidatorPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:applicationWHPWgnInputContext.xml")
public class ProviderVerificationIT {

    @Autowired
    @ReplaceWithMock
    ValidatorPool validatorPool;

    @Autowired
    ProviderVerification providerVerification;

    @Test
    public void shouldReturnErrorOnEmptyMSIDN() {
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setCall_id("callId");
        request.setTime("24/10/1989 12:12:12");

        VerificationResult result = providerVerification.verifyRequest(request);
        assertTrue(errorContains("field:msisdn:value should not be null", result.getErrors()));
    }

    @Test
    public void shouldReturnErrorOnMSIDNGreaterThanTenDigitsInLength() {
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn("1234");
        request.setCall_id("callId");
        request.setTime("24/10/1989 12:12:12");

        VerificationResult result = providerVerification.verifyRequest(request);
        assertTrue(errorContains("field:msisdn:should be atleast 10 digits in length", result.getErrors()));
    }

    @Test
    public void shouldReturnErrorOnEmptyDateTime() {
        String msisdn = "1234567890";

        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setTime(null);
        request.setCall_id("callId");

        VerificationResult result = providerVerification.verifyRequest(request);
        assertTrue(errorContains("field:time:value should not be null", result.getErrors()));
    }

    @Test
    public void shouldReturnErrorOnInvalidDateTime() {
        String msisdn = "1234567890";

        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setTime("invalidTime");
        request.setCall_id("callId");

        VerificationResult result = providerVerification.verifyRequest(request);

        assertTrue(errorContains("field:time:Invalid format: \"invalidTime\"", result.getErrors()));
    }

    @Test
    public void shouldReturnErrorOnEmptyCallID() {
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn("1234567890");
        request.setTime("24/10/1989 12:12:12");

        VerificationResult result = providerVerification.verifyRequest(request);
        assertEquals("field:call_id:value should not be null", result.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldVerifyRequest() {
        String msisdn = "1234567890";
        String callId = "callId";
        ProviderVerificationRequest request = new ProviderVerificationRequest(msisdn, "time", callId);

        ArgumentCaptor<WHPErrors> whpErrors = ArgumentCaptor.forClass(WHPErrors.class);

        when(validatorPool.verifyMobileNumber(eq(msisdn), whpErrors.capture())).thenReturn(validatorPool);

        List<WHPError> errors = providerVerification.verify(request);

        verify(validatorPool, times(1)).verifyMobileNumber(eq(msisdn), whpErrors.capture());
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
