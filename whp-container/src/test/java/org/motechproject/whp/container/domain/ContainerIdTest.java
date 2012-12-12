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
    public void shouldCreateContainerIdFromUserIdAndContainerIdSuffixForProviderMode() {
        ContainerId containerId = new ContainerId("providerId", "00001", ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
        assertEquals(containerId.value(), "sproviderId00001");
    }

    @Test
    public void shouldUseGivenContainerIdForNewContainerMode() {
        ContainerId containerId = new ContainerId("providerId", "12345678901", ContainerRegistrationMode.NEW_CONTAINER);
        assertEquals(containerId.value(), "12345678901");
    }

    @Test
    public void shouldThrowExceptionWhenContainerIdLengthIsIncorrect() {
        String invalidContainerId_lengthGreaterThan5 = "123456";

        exceptionThrown.expect(InvalidContainerIdException.class);
        exceptionThrown.expectMessage(String.format("ContainerId %s is invalid", invalidContainerId_lengthGreaterThan5));

        new ContainerId("providerId", invalidContainerId_lengthGreaterThan5, ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
    }

    @Test
    public void shouldThrowExceptionWhenContainerIdLengthIsGreaterThan5_newContainer() {
        String invalidContainerId_lengthGreaterThan5 = "123456";

        exceptionThrown.expect(InvalidContainerIdException.class);
        exceptionThrown.expectMessage(String.format("ContainerId %s is invalid", invalidContainerId_lengthGreaterThan5));

        new ContainerId("providerId", invalidContainerId_lengthGreaterThan5, ContainerRegistrationMode.NEW_CONTAINER);
    }

    @Test
    public void showThrowExceptionWhenContainerIdLengthIsLessThan5_behalfOfProvider() {
        String invalidContainerId_lengthLessThan5 = "1234";

        exceptionThrown.expect(InvalidContainerIdException.class);
        exceptionThrown.expectMessage(String.format("ContainerId %s is invalid", invalidContainerId_lengthLessThan5));

        new ContainerId("providerId", invalidContainerId_lengthLessThan5, ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER);
    }

    @Test
    public void should() {
        String invalidContainerId_lengthLessThan5 = "1234";
        exceptionThrown.expect(InvalidContainerIdException.class);
        exceptionThrown.expectMessage(String.format("ContainerId %s is invalid", invalidContainerId_lengthLessThan5));

        new ContainerId("providerId", invalidContainerId_lengthLessThan5, ContainerRegistrationMode.NEW_CONTAINER);

    }
}
