package org.motechproject.whp.container.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.CmfAdminContainerRegistrationRequest;
import org.motechproject.whp.container.contract.ContainerRegistrationMode;
import org.motechproject.whp.container.mapping.service.AdminContainerMappingService;
import org.motechproject.whp.container.mapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.domain.Provider;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.container.contract.ContainerRegistrationMode.NEW_CONTAINER;
import static org.motechproject.whp.container.contract.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;
import static org.motechproject.whp.refdata.domain.SputumTrackingInstance.InTreatment;

public class CmfAdminContainerRegistrationValidatorTest {

    @Mock
    private AdminContainerMappingService adminContainerMappingService;

    @Mock
    private ProviderContainerMappingService providerContainerMappingService;

    @Mock
    private CommonContainerRegistrationValidator containerRegistrationRequestValidator;

    private CmfAdminContainerRegistrationValidator validator;

    private Provider validProvider = new Provider("validProvider", "123", "dist", now());
    private final String CONTAINER_ID = "12345678911";

    @Before
    public void setup(){
        initMocks(this);
        validator = new CmfAdminContainerRegistrationValidator(containerRegistrationRequestValidator, adminContainerMappingService, providerContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdAsNewContainer_forValidContainerId(){

        ContainerRegistrationMode containerRegistrationMode = NEW_CONTAINER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(validProvider.getProviderId(), CONTAINER_ID, InTreatment.getDisplayText(),containerRegistrationMode);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(adminContainerMappingService.isValidContainer(Long.parseLong(CONTAINER_ID))).thenReturn(true);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertTrue(validationErrors.isEmpty());
        verify(adminContainerMappingService).isValidContainer(Long.parseLong(CONTAINER_ID));
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdAsNewContainer_forInvalidContainerId(){

        ContainerRegistrationMode containerRegistrationMode = NEW_CONTAINER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(validProvider.getProviderId(), CONTAINER_ID, InTreatment.getDisplayText(),containerRegistrationMode);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(adminContainerMappingService.isValidContainer(Long.parseLong(CONTAINER_ID))).thenReturn(false);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        verify(adminContainerMappingService).isValidContainer(Long.parseLong(CONTAINER_ID));
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdOnBehalfOfProvider_forValidContainerId(){
        String containerId = "12345678911";

        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                containerId, InTreatment.getDisplayText(),
                containerRegistrationMode);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertTrue(validationErrors.isEmpty());
        verify(providerContainerMappingService).isValidContainerForProvider(validProvider.getProviderId(), containerId);
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdOnBehalfOfProvider_forInvalidContainerId(){
        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), CONTAINER_ID)).thenReturn(false);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        verify(providerContainerMappingService).isValidContainerForProvider(validProvider.getProviderId(), CONTAINER_ID);
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @Test
    public void shouldNotDoContainerMappingValidationsWhenThereAreCommonValidationErrors(){
        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode);

        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
        errors.add(new ErrorWithParameters("container.id.length.error", "11"));
        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(errors);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(adminContainerMappingService);
        verifyNoMoreInteractions(containerRegistrationRequestValidator);
        verifyNoMoreInteractions(providerContainerMappingService);
    }
}
