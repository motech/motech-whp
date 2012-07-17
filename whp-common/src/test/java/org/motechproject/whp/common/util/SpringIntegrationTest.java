package org.motechproject.whp.common.util;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Qualifier("whpDbConnector")
    @Autowired
    protected CouchDbConnector whpDbConnector;

    protected ArrayList<BulkDeleteDocument> toDelete;

    @Before
    public void before() {
        toDelete = new ArrayList<BulkDeleteDocument>();
    }

    @After
    public void after() {
        deleteAll();
    }

    protected void deleteAll() {
        if (toDelete.size() > 0)
            whpDbConnector.executeBulk(toDelete);
        toDelete.clear();
    }

    protected void markForDeletion(Object... documents) {
        for (Object document : documents)
            markForDeletion(document);
    }

    protected void markForDeletion(List documents) {
        markForDeletion(documents.toArray());
    }

    protected void markForDeletion(Object document) {
        toDelete.add(BulkDeleteDocument.of(document));
    }

    protected String unique(String name) {
        return name + DateUtil.now().toInstant().getMillis();
    }

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
