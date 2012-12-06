package org.motechproject.whp.adherenceapi.validator;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.AdherenceWindow;
import org.motechproject.whp.adherenceapi.request.AdherenceCaptureFlashingRequest;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceRequestsValidatorTest {
    private AdherenceRequestsValidator adherenceRequestsValidator;
    @Mock
    private ProviderService providerService;
    @Mock
    private AdherenceWindow adherenceWindow;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceRequestsValidator = new AdherenceRequestsValidator(providerService, adherenceWindow);
    }

    @Test
    public void shouldValidateTheProviderId_ForFlashingRequest() {
        String msisdn = "1234567890";
        AdherenceCaptureFlashingRequest flashingRequest = new AdherenceCaptureFlashingRequest();
        flashingRequest.setMsisdn(msisdn);

        LocalDate today = DateUtil.today();
        when(providerService.findByMobileNumber(msisdn)).thenReturn(null);
        when(adherenceWindow.isValidAdherenceDay(today)).thenReturn(true);

        ErrorWithParameters error = adherenceRequestsValidator.validateFlashingRequest(flashingRequest, today);

        assertEquals(AdherenceCaptureError.INVALID_MOBILE_NUMBER.name(), error.getCode());
    }

    @Test
    public void shouldValidateTheAdherenceDay_ForFlashingRequest() {
        String msisdn = "1234567890";
        AdherenceCaptureFlashingRequest flashingRequest = new AdherenceCaptureFlashingRequest();
        flashingRequest.setMsisdn(msisdn);
        when(providerService.findByMobileNumber(msisdn)).thenReturn(new Provider());

        LocalDate today = DateUtil.today();
        when(adherenceWindow.isValidAdherenceDay(today)).thenReturn(false);

        ErrorWithParameters error = adherenceRequestsValidator.validateFlashingRequest(flashingRequest, today);

        assertEquals(AdherenceCaptureError.NON_ADHERENCE_DAY.name(), error.getCode());
    }
}
