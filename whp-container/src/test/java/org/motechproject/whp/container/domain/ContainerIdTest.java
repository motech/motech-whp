package org.motechproject.whp.container.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.motechproject.whp.container.InvalidContainerIdException;

import static junit.framework.Assert.assertEquals;

public class ContainerIdTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();


    @Test
    public void shouldCreateContainerIdFromUserIdAndContainerIdSuffix() {
        ContainerId containerId = new ContainerId("providerId", "00001");
        assertEquals(containerId.value(), "sproviderId00001");
    }

    @Test
    public void shouldThrowExceptionWhenContainerIdLengthIsIncorrect() {
        String invalidContainerId_lengthGreaterThan5 = "123456";
        String invalidContainerId_lengthLessThan5 = "1234";

        exceptionThrown.expect(InvalidContainerIdException.class);
        exceptionThrown.expectMessage(String.format("ContainerId %s is invalid", invalidContainerId_lengthGreaterThan5));

        new ContainerId("providerId", invalidContainerId_lengthGreaterThan5);

        exceptionThrown.expect(InvalidContainerIdException.class);
        exceptionThrown.expectMessage(String.format("ContainerId %s is invalid", invalidContainerId_lengthLessThan5));

        new ContainerId("providerId", invalidContainerId_lengthLessThan5);
    }

}
