package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.ChannelId;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.CmfAdminContainerRegistrationRequest;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.container.validation.CmfAdminContainerRegistrationValidator;
import org.motechproject.whp.user.domain.WHPRole;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.container.contract.ContainerRegistrationMode.NEW_CONTAINER;
import static org.motechproject.whp.container.contract.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class CmfAdminContainerRegistrationControllerTest {
    public static final String CONTRIB_FLASH_OUT_PREFIX = "flash.out.";
    public static final String CONTRIB_FLASH_IN_PREFIX = "flash.in.";
    public static final int CONTAINER_ID_MAX_LENGTH = 11;
    private CmfAdminContainerRegistrationController containerRegistrationController;
    @Mock
    private ContainerService containerService;
    @Mock
    private CmfAdminContainerRegistrationValidator containerRegistrationValidator;
    @Mock
    private SputumTrackingProperties sputumTrackingProperties;

    List<String> INSTANCES = new ArrayList<>();

    @Before
    public void setUp() {
        initMocks(this);
        INSTANCES.add(RegistrationInstance.PreTreatment.getDisplayText());
        INSTANCES.add(RegistrationInstance.InTreatment.getDisplayText());
        when(sputumTrackingProperties.getContainerIdMaxLength()).thenReturn(CONTAINER_ID_MAX_LENGTH);
        containerRegistrationController = new CmfAdminContainerRegistrationController(containerService, containerRegistrationValidator, sputumTrackingProperties);
    }

    @Test
    public void shouldConvertRequestParamsToCmfAdminContainerRegistrationRequest() throws Exception {
        containerRegistrationController = spy(containerRegistrationController);

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.CMF_ADMIN.name());

        String containerId = "containerId";
        String providerId = "providerId";
        String instance = RegistrationInstance.InTreatment.getDisplayText();
        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/by_cmfAdmin/register")
                        .param("containerId", containerId)
                        .param("instance", instance)
                        .param("providerId", providerId)
                        .param("containerRegistrationMode", NEW_CONTAINER.name())
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(null, null, null, roles))));

        ArgumentCaptor<CmfAdminContainerRegistrationRequest> argumentCaptor = ArgumentCaptor.forClass(CmfAdminContainerRegistrationRequest.class);
        verify(containerRegistrationController).register(any(Model.class), argumentCaptor.capture(), any(HttpServletRequest.class));

        CmfAdminContainerRegistrationRequest request = argumentCaptor.getValue();
        assertEquals(NEW_CONTAINER, request.getContainerRegistrationMode());
        assertEquals(providerId, request.getProviderId());
        assertEquals(containerId, request.getContainerId());
        assertEquals(instance, request.getInstance());
    }

    @Test
    public void shouldDisplayTheContainerRegistrationPageWithAppropriateControls() throws Exception {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.CMF_ADMIN.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(get("/containerRegistration/by_cmfAdmin/new-container")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(null, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("instances", INSTANCES))
                .andExpect(model().attribute("containerIdMaxLength", CONTAINER_ID_MAX_LENGTH))
                .andExpect(forwardedUrl("containerRegistration/cmfAdminNewContainerRegistration"));
    }

    @Test
    public void shouldDisplayTheContainerRegistrationPageWithSuccessfullyRegisteredFlashMessage() throws Exception {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(get("/containerRegistration/by_cmfAdmin/new-container").requestAttr(CONTRIB_FLASH_IN_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "success")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(null, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute(WHPConstants.NOTIFICATION_MESSAGE, "success"))
                .andExpect(forwardedUrl("containerRegistration/cmfAdminNewContainerRegistration"));
    }

    @Test
    public void shouldValidateRegistrationRequest() throws Exception {
        String containerId = "123456789a";
        String instance = "invalid_instance";

        List<ErrorWithParameters> errors = new ArrayList<>();
        errors.add(new ErrorWithParameters("some error 1"));
        errors.add(new ErrorWithParameters("some error 2"));
        when(containerRegistrationValidator.validate(any(CmfAdminContainerRegistrationRequest.class))).thenReturn(errors);

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.CMF_ADMIN.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/by_cmfAdmin/register").param("containerId", containerId).param("instance", instance).param("containerRegistrationMode", NEW_CONTAINER.name())
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(null, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errors", errors))
                .andExpect(model().attribute("containerIdMaxLength", CONTAINER_ID_MAX_LENGTH))
                .andExpect(model().attribute("instances", INSTANCES))
                .andExpect(forwardedUrl("containerRegistration/cmfAdminNewContainerRegistration"));

        verify(containerRegistrationValidator).validate(any(CmfAdminContainerRegistrationRequest.class));
        verify(containerService, never()).registerContainer(any(ContainerRegistrationRequest.class));
    }

    @Test
    public void shouldRegisterTheContainerGivenTheDetailsForNewContainer() throws Exception {
        String providerId = "P00011";
        String containerId = "1234567890";
        String instance = RegistrationInstance.InTreatment.getDisplayText();

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.CMF_ADMIN.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/by_cmfAdmin/register").param("containerId", containerId).param("instance", instance).param("containerRegistrationMode", NEW_CONTAINER.name())
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(providerId, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Container with id 1234567890 registered successfully."))
                .andExpect(redirectedUrl("/containerRegistration/by_cmfAdmin/new-container"));

        ArgumentCaptor<ContainerRegistrationRequest> captor = ArgumentCaptor.forClass(ContainerRegistrationRequest.class);
        verify(containerService).registerContainer(captor.capture());
        ContainerRegistrationRequest actualRegistrationRequest = captor.getValue();

        assertEquals(containerId, actualRegistrationRequest.getContainerId());
        assertEquals(instance, actualRegistrationRequest.getInstance());
        assertEquals(ChannelId.WEB.name(), actualRegistrationRequest.getChannelId());
    }

    @Test
    public void shouldRegisterTheContainerGivenTheDetailsOnBehalfOfProvider() throws Exception {
        String providerId = "P00011";
        String containerId = "1234567890";
        String instance = RegistrationInstance.InTreatment.getDisplayText();

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.CMF_ADMIN.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/by_cmfAdmin/register").param("containerId", containerId).param("instance", instance).param("providerId", providerId).param("containerRegistrationMode", ON_BEHALF_OF_PROVIDER.name())
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(null, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Container with id 1234567890 registered successfully."))
                .andExpect(redirectedUrl("/containerRegistration/by_cmfAdmin/on-behalf-of-provider"));

        ArgumentCaptor<ContainerRegistrationRequest> captor = ArgumentCaptor.forClass(ContainerRegistrationRequest.class);
        verify(containerService).registerContainer(captor.capture());
        ContainerRegistrationRequest actualRegistrationRequest = captor.getValue();

        assertEquals(providerId, actualRegistrationRequest.getProviderId());
        assertEquals(containerId, actualRegistrationRequest.getContainerId());
        assertEquals(instance, actualRegistrationRequest.getInstance());
        assertEquals(ChannelId.WEB.name(), actualRegistrationRequest.getChannelId());
    }
}