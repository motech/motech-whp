package org.motechproject.whp.webservice.service;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.motechproject.casexml.service.exception.CaseError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.webservice.exception.WHPCaseException;

public abstract class BaseWebServiceTest {
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    protected void expectWHPCaseException(final WHPErrorCode errorCode) {
        exceptionThrown.expect(WHPCaseException.class);
        exceptionThrown.expect(new TypeSafeMatcher<WHPCaseException>() {
            @Override
            public boolean matchesSafely(WHPCaseException caseException) {
                for (CaseError caseError : caseException.getErrors()) {
                    if (caseError.getCode().equals(errorCode.name())) {
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
