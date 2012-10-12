package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.tracking.service.ContainerTrackingService;
import org.motechproject.whp.refdata.domain.AlternateDiagnosisList;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;
import org.motechproject.whp.request.UpdateReasonForClosureRequest;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.controller.ContainerTrackingController.ALTERNATE_DIAGNOSIS_LIST;
import static org.motechproject.whp.controller.ContainerTrackingController.REASONS;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ContainerTrackingControllerTest {

    ContainerTrackingController containerTrackingController;

    @Mock
    ContainerService containerService;
    @Mock
    ContainerTrackingService containerTrackingService;

    @Mock
    UpdateReasonForClosureRequest updateReasonForClosureRequest;

    @Mock
    Container container;

    @Before
    public void setUp() {
        initMocks(this);
        containerTrackingController = new ContainerTrackingController(containerService, containerTrackingService);
    }

    @Test
    public void shouldPopulateReasonsForClosure() throws Exception {
        ArrayList<ReasonForContainerClosure> reasons = new ArrayList<>();
        ArrayList<AlternateDiagnosisList> alternateDiagnosisLists = new ArrayList<>();

        when(containerTrackingService.getAllClosureReasons()).thenReturn(reasons);
        when(containerTrackingService.getAllAlternateDiagnosisList()).thenReturn(alternateDiagnosisLists);

        standaloneSetup(containerTrackingController).build()
                .perform(get("/sputum-tracking/pre-treatment"))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(REASONS, reasons))
                .andExpect(model().attribute(ALTERNATE_DIAGNOSIS_LIST, alternateDiagnosisLists));

        verify(containerTrackingService).getAllClosureReasons();
        verify(containerTrackingService).getAllAlternateDiagnosisList();
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
