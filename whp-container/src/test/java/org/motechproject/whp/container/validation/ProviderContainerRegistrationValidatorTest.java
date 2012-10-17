package org.motechproject.whp.container.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.user.domain.Provider;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.common.domain.SputumTrackingInstance.InTreatment;

public class ProviderContainerRegistrationValidatorTest {

    @Mock
    private ProviderContainerMappingService providerContainerMappingService;
    @Mock
    private CommonContainerRegistrationValidator containerRegistrationRequestValidator;

    private ProviderContainerRegistrationValidator validator;

    Provider validProvider = new Provider("validProvider", "123", "dist", now());

    @Before
    public void setUp()  {
        initMocks(this);
        validator = new ProviderContainerRegistrationValidator(containerRegistrationRequestValidator, providerContainerMappingService);
    }

    @Test
    public void shouldValidateProviderIdContainerMappingSuccessfully() {
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, SputumTrackingInstance.InTreatment.getDisplayText());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertTrue(validationErrors.isEmpty());
        verify(providerContainerMappingService).isValidContainerForProvider(validProvider.getProviderId(), containerId);
    }

    @Test
    public void shouldValidateProviderIdContainerMappingWithErrors_whenProviderExists() {
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(validProvider.getProviderId(), containerId, SputumTrackingInstance.InTreatment.getDisplayText());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(false);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(new ErrorWithParameters("container.id.invalid.error", containerId)));
        verify(providerContainerMappingService).isValidContainerForProvider(validProvider.getProviderId(), containerId);
    }

    @Test
    public void shouldNotValidateProviderIdContainerMapping_whenContainerRegistrationValidationFailed() {
        String unregisteredProviderId = "UnregisteredProviderId";
        String containerId = "11111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(unregisteredProviderId, containerId, SputumTrackingInstance.InTreatment.getDisplayText());


        ArrayList<ErrorWithParameters> expectedErrors = new ArrayList<>();
        expectedErrors.add(new ErrorWithParameters("provider.not.registered.error", unregisteredProviderId));
        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(expectedErrors);

        List<ErrorWithParameters> errors = validator.validate(registrationRequest);

        assertEquals(expectedErrors, errors);
        verify(providerContainerMappingService, never()).isValidContainerForProvider(unregisteredProviderId, containerId);
    }

    @Test
    public void shouldNotDoContainerMappingValidationsWhenThereAreCommonValidationErrors(){
        String containerId = "11111111111111";
        ContainerRegistrationRequest registrationRequest = new ContainerRegistrationRequest(
                validProvider.getProviderId(),
                containerId,
                InTreatment.getDisplayText());

        ArrayList<ErrorWithParameters> expectedErrors = new ArrayList<>();
        expectedErrors.add(new ErrorWithParameters("container.id.length.error", "11"));
        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(expectedErrors);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        assertEquals(expectedErrors, validationErrors);
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(providerContainerMappingService);
    }
}