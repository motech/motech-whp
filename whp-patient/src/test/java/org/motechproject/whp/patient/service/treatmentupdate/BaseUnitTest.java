package org.motechproject.whp.patient.service.treatmentupdate;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;

public class BaseUnitTest {
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    protected void expectWHPDomainException(final WHPDomainErrorCode errorCode) {
        exceptionThrown.expect(WHPDomainException.class);
        exceptionThrown.expect(new ArgumentMatcher<Object>() {

            WHPDomainException e;

            @Override
            public boolean matches(Object o) {
                e = (WHPDomainException) o;
                return e.getErrorCodes().contains(errorCode);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(errorCode);
            }
        });
    }
}
