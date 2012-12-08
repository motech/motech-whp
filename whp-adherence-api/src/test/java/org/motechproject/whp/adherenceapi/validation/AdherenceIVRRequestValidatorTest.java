package org.motechproject.whp.adherenceapi.validation;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherenceapi.request.AdherenceFlashingRequest;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceIVRRequestValidatorTest {

    @Mock
    private RequestValidator requestValidator;
    private AdherenceIVRRequestValidator validator;

    @Before
    public void setup() {
        initMocks(this);
        validator = new AdherenceIVRRequestValidator(requestValidator);
    }

    @Test
    public void shouldBeValidWhenRequestHasNoErrors() {
        AdherenceFlashingRequest request = new AdherenceFlashingRequest();
        assertFalse(validator.isValid(request).hasErrors());
    }

    @Test
    public void shouldBeInvalidWhenRequestHasOneError() {
        AdherenceFlashingRequest request = new AdherenceFlashingRequest();
        WHPRuntimeException exception = new WHPRuntimeException(WHPErrorCode.FIELD_VALIDATION_FAILED);

        when(requestValidator.validate(request, "")).thenThrow(exception);
        assertTrue(validator.isValid(request).hasErrors());
    }
}
