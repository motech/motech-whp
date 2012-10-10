package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.request.UpdateReasonForClosureRequest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ContainerTrackingControllerTest {

    ContainerTrackingController containerTrackingController;

    @Mock
    ContainerService containerService;

    @Mock
    UpdateReasonForClosureRequest updateReasonForClosureRequest;

    @Mock
    Container container;

    @Before
    public void setUp() {
        initMocks(this);
        containerTrackingController = new ContainerTrackingController(containerService);

    }

    @Test
    public void shouldRedirectToDashBoardAfterProcessingRequest() throws Exception {
        standaloneSetup(containerTrackingController).build().perform(post("/sputum-tracking/reasonForClosure")).andExpect(view().name("redirect:/sputum-tracking/pre-treatment"));
    }

    @Test
    public void shouldCallUpdateReasonForClosure() throws Exception {

        String containerId = "containerId";
        String reason = "Reason";

        when(updateReasonForClosureRequest.getContainerId()).thenReturn(containerId);
        when(updateReasonForClosureRequest.getReason()).thenReturn(reason);
        when(containerService.getContainer(containerId)).thenReturn(container);

        containerTrackingController.updateReasonForClosure(updateReasonForClosureRequest);

        verify(container, times(1)).setReasonForClosure(reason);
        verify(containerService, times(1)).getContainer(containerId);
        verify(containerService, times(1)).update(container);

    }


}
