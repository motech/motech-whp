package org.motechproject.whp.container.contract;

import org.junit.Test;
import org.motechproject.whp.container.domain.Instance;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class RegistrationRequestTest {
    private RegistrationRequest request;

    @Test
    public void shouldValidateContainerId() {
        request = new RegistrationRequest("P0001", "12345", Instance.IN_TREATMENT.getDisplayText());
        List<String> invalidLengthErrors = request.validate();
        assertEquals(1, invalidLengthErrors.size());
        assertEquals("Container Id must be of 10 digits in length", invalidLengthErrors.get(0));

        request = new RegistrationRequest("P0001", "123456789a", Instance.IN_TREATMENT.getDisplayText());
        List<String> nonNumericErrors = request.validate();
        assertEquals(1, nonNumericErrors.size());
        assertEquals("Container Id must be of 10 digits in length", nonNumericErrors.get(0));
    }

    @Test
    public void shouldValidateInstance() {
        request = new RegistrationRequest("P0001", "1234567890", "invalid_instance");
        List<String> invalidInstanceErrors = request.validate();
        assertEquals(1, invalidInstanceErrors.size());
            assertEquals("Invalid instance : invalid_instance", invalidInstanceErrors.get(0));
    }
}
