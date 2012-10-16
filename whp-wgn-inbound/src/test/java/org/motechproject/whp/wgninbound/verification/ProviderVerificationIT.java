package org.motechproject.whp.wgninbound.verification;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.wgninbound.request.ProviderVerificationRequest;
import org.motechproject.whp.wgninbound.response.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationWHPWgnInputContext.xml")
public class ProviderVerificationIT {

    @Autowired
    ProviderVerification providerVerification;

    @Autowired
    AllProviders allProviders;

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
        Provider provider = new Provider();
        provider.setPrimaryMobile(msisdn);
        allProviders.add(provider);

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
        Provider provider = new Provider();
        provider.setPrimaryMobile(msisdn);
        allProviders.add(provider);

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
    public void shouldReturnSuccessWhenMSIDNIsThePrimaryMobileNumberOfAnyProvider() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("24/10/1989 12:12:12");

        Provider provider = new Provider();
        provider.setPrimaryMobile(msisdn);
        allProviders.add(provider);

        assertTrue(providerVerification.verifyRequest(request).isSuccess());
    }

    @Test
    public void shouldReturnSuccessWhenMSIDNIsTheSecondaryMobileNumberOfAnyProvider() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("24/10/1989 12:12:12");

        Provider provider = new Provider();
        provider.setSecondaryMobile(msisdn);
        allProviders.add(provider);

        assertTrue(providerVerification.verifyRequest(request).isSuccess());
    }

    @Test
    public void shouldReturnSuccessWhenMSIDNIsTheTertiaryMobileNumberOfAnyProvider() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("24/10/1989 12:12:12");

        Provider provider = new Provider();
        provider.setTertiaryMobile(msisdn);
        allProviders.add(provider);

        assertTrue(providerVerification.verifyRequest(request).isSuccess());
    }

    @Test
    public void shouldReturnErrorWhenMSIDNIsNotThePhoneNumberOfAnyProvider() {
        String msisdn = "1234567890";
        ProviderVerificationRequest request = new ProviderVerificationRequest();
        request.setMsisdn(msisdn);
        request.setCall_id("callId");
        request.setTime("24/10/1989 12:12:12");

        assertEquals(WHPErrorCode.INVALID_PHONE_NUMBER, providerVerification.verifyRequest(request).getErrors().get(0).getErrorCode());
    }

    private boolean errorContains(String expectedMessage, List<WHPError> errors) {
        boolean found = false;
        for (WHPError error : errors) {
            found |= StringUtils.equals(expectedMessage, error.getMessage());
        }
        return found;
    }

    @After
    public void tearDown() {
        allProviders.removeAll();
    }
}
