package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.service.ContainerDashboardService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class SputumTrackingControllerTest {

    @Mock
    private ContainerDashboardService containerDashboardService;
    private SputumTrackingController sputumTrackingController;

    @Before
    public void setUp() {
        initMocks(this);
        sputumTrackingController = new SputumTrackingController(containerDashboardService);
    }

    @Test
    public void shouldReturnPreTreatmentView() throws Exception {
        standaloneSetup(sputumTrackingController).build()
                .perform(get("/sputumTracking/preTreatment"))
                .andExpect(status().isOk())
                .andExpect(view().name("sputumTracking/preTreatment"));
    }

    @Test
    public void shouldAddAllPretreatmentContainersToModel() throws Exception {
        List<ContainerDashboardRow> containerRows = Collections.emptyList();
        when(containerDashboardService.allContainerDashboardRows()).thenReturn(containerRows);

        standaloneSetup(sputumTrackingController).build()
                .perform(get("/sputumTracking/preTreatment"))
                .andExpect(model().attribute("sputumContainers", containerRows));

    }
}
