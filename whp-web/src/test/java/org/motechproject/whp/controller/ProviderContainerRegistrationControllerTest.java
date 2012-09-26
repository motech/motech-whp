package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.validation.ContainerRegistrationRequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.SputumTrackingProperties;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.user.domain.WHPRole;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ProviderContainerRegistrationControllerTest {

    public static final String CONTRIB_FLASH_OUT_PREFIX = "flash.out.";
    public static final String CONTRIB_FLASH_IN_PREFIX = "flash.in.";
    public static final int CONTAINER_ID_MAX_LENGTH = 11;
    private ProviderContainerRegistrationController containerRegistrationController;
    @Mock
    private ContainerService containerService;
    @Mock
    private ContainerRegistrationRequestValidator registrationRequestValidator;
    @Mock
    private SputumTrackingProperties sputumTrackingProperties;
    private String providerId;
    List<String> INSTANCES = new ArrayList<>();

    @Before
    public void setUp() {
        initMocks(this);
        INSTANCES.add(SputumTrackingInstance.PRE_TREATMENT.getDisplayText());
        INSTANCES.add(SputumTrackingInstance.IN_TREATMENT.getDisplayText());
        providerId = "providerId";
        when(sputumTrackingProperties.getContainerIdMaxLength()).thenReturn(CONTAINER_ID_MAX_LENGTH);
        containerRegistrationController = new ProviderContainerRegistrationController(containerService, registrationRequestValidator, sputumTrackingProperties);
    }

    @Test
    public void shouldDisplayTheContainerRegistrationPageWithAppropriateControls() throws Exception {

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(get("/containerRegistration/by_provider")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(providerId, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("instances", INSTANCES))
                .andExpect(model().attribute("containerIdMaxLength", CONTAINER_ID_MAX_LENGTH))
                .andExpect(forwardedUrl("containerRegistration/showForProvider"));
    }

    @Test
    public void shouldDisplayTheContainerRegistrationPageWithSuccessfullyRegisteredFlashMessage() throws Exception {
        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(get("/containerRegistration/by_provider").requestAttr(CONTRIB_FLASH_IN_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "success")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(providerId, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute(WHPConstants.NOTIFICATION_MESSAGE, "success"))
                .andExpect(forwardedUrl("containerRegistration/showForProvider"));
    }

    @Test
    public void shouldValidateRegistrationRequest() throws Exception {
        String providerId = "P0001";
        String containerId = "123456789a";
        String instance = "invalid_instance";

        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
        errors.add(new ErrorWithParameters("a", "b"));
        errors.add(new ErrorWithParameters("a", "b"));
        when(registrationRequestValidator.validate(any(ContainerRegistrationRequest.class))).thenReturn(errors);

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/by_provider/register").param("containerId", containerId).param("instance", instance)
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(providerId, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errors", errors))
                .andExpect(model().attribute("containerIdMaxLength", CONTAINER_ID_MAX_LENGTH))
                .andExpect(model().attribute("instances", INSTANCES))
                .andExpect(forwardedUrl("containerRegistration/showForProvider"));

        ArgumentCaptor<ContainerRegistrationRequest> captor = ArgumentCaptor.forClass(ContainerRegistrationRequest.class);
        verify(registrationRequestValidator).validate(captor.capture());
        ContainerRegistrationRequest request = captor.getValue();
        assertEquals(providerId.toLowerCase(), request.getProviderId());

        verify(containerService, never()).registerContainer(any(ContainerRegistrationRequest.class));
    }

    @Test
    public void shouldRegisterTheContainerGivenTheDetails() throws Exception {
        String providerId = "P00011";
        String containerId = "1234567890";
        String instance = SputumTrackingInstance.IN_TREATMENT.getDisplayText();

        ArrayList<String> roles = new ArrayList<>();
        roles.add(WHPRole.PROVIDER.name());

        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/by_provider/register").param("containerId", containerId).param("instance", instance)
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(providerId, null, null, roles))))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Container with id 1234567890 registered successfully."))
                .andExpect(redirectedUrl("/containerRegistration/by_provider"));

        ArgumentCaptor<ContainerRegistrationRequest> captor = ArgumentCaptor.forClass(ContainerRegistrationRequest.class);
        verify(containerService).registerContainer(captor.capture());
        ContainerRegistrationRequest actualRegistrationRequest = captor.getValue();

        assertEquals(providerId.toLowerCase(), actualRegistrationRequest.getProviderId());
        assertEquals(containerId, actualRegistrationRequest.getContainerId());
        assertEquals(instance, actualRegistrationRequest.getInstance());
    }
}
