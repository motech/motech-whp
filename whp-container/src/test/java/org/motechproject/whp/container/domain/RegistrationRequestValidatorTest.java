package org.motechproject.whp.container.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegistrationRequestValidatorTest {
    @Mock
    private ContainerService containerService;
    @Mock
    private SputumTrackingProperties sputumTrackingProperties;
    private RegistrationRequestValidator registrationRequestValidator;

    @Before
    public void setUp() {
        initMocks(this);
        when(sputumTrackingProperties.getContainerIdMaxLength()).thenReturn(11);
        registrationRequestValidator = new RegistrationRequestValidator(containerService, sputumTrackingProperties);
    }

    @Test
    public void shouldValidateDuplicateContainerId() {
        String containerID = "12345678910";
        RegistrationRequest registrationRequest = new RegistrationRequest("P0001", containerID, Instance.IN_TREATMENT.getDisplayText());
        when(containerService.exists(containerID)).thenReturn(true);

        List<String> errors = registrationRequestValidator.validate(registrationRequest);

        verify(containerService).exists(containerID);
        assertEquals(1, errors.size());
        assertEquals("Container Id already exists.", errors.get(0));
    }

    @Test
    public void shouldValidateContainerId() {
        RegistrationRequest request = new RegistrationRequest("P0001", "12345", Instance.IN_TREATMENT.getDisplayText());
        List<String> invalidLengthErrors = registrationRequestValidator.validate(request);
        assertEquals(1, invalidLengthErrors.size());
        assertEquals("Container Id must be of 11 digits in length", invalidLengthErrors.get(0));

        request = new RegistrationRequest("P0001", "123456789a", Instance.IN_TREATMENT.getDisplayText());
        List<String> nonNumericErrors = registrationRequestValidator.validate(request);
        assertEquals(1, nonNumericErrors.size());
        assertEquals("Container Id must be of 11 digits in length", nonNumericErrors.get(0));
    }

    @Test
    public void shouldValidateInstance() {
        RegistrationRequest request = new RegistrationRequest("P0001", "12345678910", "invalid_instance");
        List<String> invalidInstanceErrors = registrationRequestValidator.validate(request);
        assertEquals(1, invalidInstanceErrors.size());
        assertEquals("Invalid instance : invalid_instance", invalidInstanceErrors.get(0));
    }

    @Test
    public void shouldValidatePresenceOfProviderId() {
        RegistrationRequest registrationRequest = new RegistrationRequest("", "12345678910", Instance.IN_TREATMENT.getDisplayText());
        List<String> validationErrors = registrationRequestValidator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        assertEquals("Invalid provider id : ", validationErrors.get(0));
    }
}
