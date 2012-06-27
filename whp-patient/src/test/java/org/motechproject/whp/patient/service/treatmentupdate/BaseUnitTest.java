package org.motechproject.whp.patient.service.treatmentupdate;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;

public class BaseUnitTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    protected void expectWHPRuntimeException(final WHPErrorCode errorCode) {
        expectWHPRuntimeException(errorCode, errorCode.getMessage());
    }

    protected void expectFieldValidationRuntimeException(final String message) {
        expectWHPRuntimeException(WHPErrorCode.FIELD_VALIDATION_FAILED, message);
    }

    private void expectWHPRuntimeException(final WHPErrorCode errorCode, final String message) {
        exceptionThrown.expect(WHPRuntimeException.class);
        exceptionThrown.expect(new TypeSafeMatcher<WHPRuntimeException>() {
            @Override
            public boolean matchesSafely(WHPRuntimeException e) {
                for (WHPError whpError : e.getErrors()) {
                    if (whpError.getErrorCode().equals(errorCode) && whpError.getMessage().contains(message)) {
                        return true;
                    }
                }
                return false;

            }

            @Override
            public void describeTo(Description description) {
            }
        });
    }
}
