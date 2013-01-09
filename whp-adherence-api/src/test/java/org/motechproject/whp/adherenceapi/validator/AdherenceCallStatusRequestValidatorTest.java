package org.motechproject.whp.adherenceapi.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.errors.CallStatusRequestErrors;
import org.motechproject.whp.adherenceapi.request.AdherenceCallStatusRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceCallStatusValidationResponse;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceCallStatusRequestValidatorTest {

    @Mock
    private AdherenceRequestValidator adherenceRequestValidator;
    private AdherenceCallStatusRequestValidator adherenceCallStatusRequestValidator;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceCallStatusRequestValidator = new AdherenceCallStatusRequestValidator(adherenceRequestValidator);
    }

    @Test
    public void shouldValidateAndReturnSuccess() {
        String providerId = "providerId";
        AdherenceCallStatusRequest request = new AdherenceCallStatusRequest();
        request.setProviderId(providerId);

        when(adherenceRequestValidator.validateProvider(providerId)).thenReturn(new CallStatusRequestErrors(true));

        AdherenceCallStatusValidationResponse validationResponse = adherenceCallStatusRequestValidator.validate(request);

        assertFalse(validationResponse.failed());
    }

    @Test
    public void shouldValidateAndReturnFailure() {
        String providerId = "providerId";
        AdherenceCallStatusRequest request = new AdherenceCallStatusRequest();
        request.setProviderId(providerId);

        when(adherenceRequestValidator.validateProvider(providerId)).thenReturn(new CallStatusRequestErrors(false));

        AdherenceCallStatusValidationResponse validationResponse = adherenceCallStatusRequestValidator.validate(request);

        assertTrue(validationResponse.failed());
    }
}
