package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Instance;
import org.motechproject.whp.container.service.ContainerService;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ContainerRegistrationControllerTest {
    public static final String CONTRIB_FLASH_OUT_PREFIX = "flash.out.";
    public static final String CONTRIB_FLASH_IN_PREFIX = "flash.in.";
    private ContainerRegistrationController containerRegistrationController;
    @Mock
    private ContainerService containerService;

    @Before
    public void setUp() {
        initMocks(this);
        containerRegistrationController = new ContainerRegistrationController(containerService);
    }

    @Test
    public void shouldDisplayTheContainerRegistrationPageWithAppropriateControls() throws Exception {
        ArrayList<String> instances = new ArrayList<>();
        instances.add(Instance.PRE_TREATMENT.getDisplayText());
        instances.add(Instance.IN_TREATMENT.getDisplayText());

        standaloneSetup(containerRegistrationController).build()
                .perform(get("/containerRegistration"))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attribute("instances", instances))
                .andExpect(forwardedUrl("containerRegistration/show"));
    }

    @Test
    public void shouldDisplayTheContainerRegistrationPageWithSuccessfullyRegisteredFlashMessage() throws Exception {
        standaloneSetup(containerRegistrationController).build()
                .perform(get("/containerRegistration").requestAttr(CONTRIB_FLASH_IN_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "success"))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(WHPConstants.NOTIFICATION_MESSAGE, "success"))
                .andExpect(forwardedUrl("containerRegistration/show"));
    }

    @Test
    public void shouldRegisterTheContainerGivenTheDetails() throws Exception {
        String providerId = "P00011";
        String containerId = "1234567890";
        String instance = Instance.IN_TREATMENT.getDisplayText();

        standaloneSetup(containerRegistrationController).build()
                .perform(post("/containerRegistration/register").param("containerId", containerId).param("instance", instance)
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, new MotechUser(new MotechWebUser(null, null, providerId, null))))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Container registered successfully."))
                .andExpect(redirectedUrl("/containerRegistration"));

        ArgumentCaptor<RegistrationRequest> captor = ArgumentCaptor.forClass(RegistrationRequest.class);
        verify(containerService).registerContainer(captor.capture());
        RegistrationRequest actualRegistrationRequest = captor.getValue();

        assertEquals(providerId, actualRegistrationRequest.getProviderId());
        assertEquals(containerId, actualRegistrationRequest.getContainerId());
        assertEquals(instance, actualRegistrationRequest.getInstance());
    }
}
