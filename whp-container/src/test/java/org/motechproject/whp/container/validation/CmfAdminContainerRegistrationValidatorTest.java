package org.motechproject.whp.container.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.CmfAdminContainerRegistrationRequest;
import org.motechproject.whp.container.domain.ContainerRegistrationMode;
import org.motechproject.whp.containermapping.service.AdminContainerMappingService;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.domain.Provider;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.common.domain.RegistrationInstance.InTreatment;
import static org.motechproject.whp.container.domain.ContainerRegistrationMode.NEW_CONTAINER;
import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

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
    public void setup() {
        initMocks(this);
        validator = new CmfAdminContainerRegistrationValidator(containerRegistrationRequestValidator, adminContainerMappingService, providerContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdAsNewContainer_forValidContainerId() {

        ContainerRegistrationMode containerRegistrationMode = NEW_CONTAINER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(validProvider.getProviderId(), CONTAINER_ID, InTreatment.getDisplayText(), containerRegistrationMode, ChannelId.WEB, null);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(adminContainerMappingService.isValidContainer(Long.parseLong(CONTAINER_ID))).thenReturn(true);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertTrue(validationErrors.isEmpty());
        verify(adminContainerMappingService).isValidContainer(Long.parseLong(CONTAINER_ID));
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdAsNewContainer_forInvalidContainerId() {

        ContainerRegistrationMode containerRegistrationMode = NEW_CONTAINER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(validProvider.getProviderId(), CONTAINER_ID, InTreatment.getDisplayText(), containerRegistrationMode, ChannelId.WEB, null);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(adminContainerMappingService.isValidContainer(Long.parseLong(CONTAINER_ID))).thenReturn(false);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        verify(adminContainerMappingService).isValidContainer(Long.parseLong(CONTAINER_ID));
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdOnBehalfOfProvider_forValidContainerId() {
        String containerId = "12345678911";

        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                containerId, InTreatment.getDisplayText(),
                containerRegistrationMode, ChannelId.WEB, null);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(containerRegistrationRequestValidator.validatePatientDetails(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), containerId)).thenReturn(true);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertTrue(validationErrors.isEmpty());
        verify(providerContainerMappingService).isValidContainerForProvider(validProvider.getProviderId(), containerId);
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verify(containerRegistrationRequestValidator).validatePatientDetails(registrationRequest);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @Test
    public void shouldValidateWhenCmfAdminEntersContainerIdOnBehalfOfProvider_forInvalidContainerId() {
        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode, ChannelId.WEB, null);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(containerRegistrationRequestValidator.validatePatientDetails(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());
        when(providerContainerMappingService.isValidContainerForProvider(validProvider.getProviderId(), CONTAINER_ID)).thenReturn(false);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        verify(providerContainerMappingService).isValidContainerForProvider(validProvider.getProviderId(), CONTAINER_ID);
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verify(containerRegistrationRequestValidator).validatePatientDetails(registrationRequest);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @Test
    public void shouldNotDoContainerMappingValidationsWhenThereAreCommonValidationErrors() {
        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode, ChannelId.WEB, null);

        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
        errors.add(new ErrorWithParameters("container.id.length.error", "11"));
        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(errors);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(providerContainerMappingService);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @Test
    public void shouldNotRegisterContainer_whenRegistrationModeIsUndefined() {
        ContainerRegistrationMode containerRegistrationMode = null;
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                validProvider.getProviderId(),
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode, ChannelId.WEB, null);

        when(containerRegistrationRequestValidator.validate(registrationRequest)).thenReturn(new ArrayList<ErrorWithParameters>());

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertEquals(1, validationErrors.size());

        verifyZeroInteractions(providerContainerMappingService);
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verifyZeroInteractions(adminContainerMappingService);
    }

    @Test
    public void shouldValidatePatientDetailsWhenCMFAdminRegistersContainerOnBehalfOfProvider() {
        ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;
        String providerId = validProvider.getProviderId();
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                providerId,
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode, ChannelId.WEB, null);
        when(containerRegistrationRequestValidator.validatePatientDetails(registrationRequest)).thenReturn(asList(new ErrorWithParameters("errorCode")));

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertThat(validationErrors.size(), is(1));
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verify(containerRegistrationRequestValidator).validatePatientDetails(registrationRequest);
        verify(providerContainerMappingService, never()).isValidContainerForProvider(providerId, CONTAINER_ID);
    }

    @Test
    public void shouldNotValidatePatientDetailsWhenCMFAdminRegistersNewContainer() {
        ContainerRegistrationMode containerRegistrationMode = NEW_CONTAINER;
        String providerId = validProvider.getProviderId();
        CmfAdminContainerRegistrationRequest registrationRequest = new CmfAdminContainerRegistrationRequest(
                providerId,
                CONTAINER_ID,
                InTreatment.getDisplayText(),
                containerRegistrationMode, ChannelId.WEB, null);
        registrationRequest.setPatientName("patientName");
        when(adminContainerMappingService.isValidContainer(Long.parseLong(CONTAINER_ID))).thenReturn(true);

        List<ErrorWithParameters> validationErrors = validator.validate(registrationRequest);

        assertThat(validationErrors.size(), is(0));
        verify(containerRegistrationRequestValidator).validate(registrationRequest);
        verify(containerRegistrationRequestValidator, never()).validatePatientDetails(registrationRequest);
        verify(adminContainerMappingService).isValidContainer(Long.parseLong(CONTAINER_ID));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(adminContainerMappingService);
        verifyNoMoreInteractions(containerRegistrationRequestValidator);
        verifyNoMoreInteractions(providerContainerMappingService);
    }
}
