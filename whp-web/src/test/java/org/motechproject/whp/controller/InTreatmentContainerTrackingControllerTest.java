package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.ContainerTrackingService;
import org.motechproject.whp.container.validation.ReasonForClosureValidator;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.controller.InTreatmentContainerTrackingController.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class InTreatmentContainerTrackingControllerTest {

    InTreatmentContainerTrackingController containerTrackingController;

    @Mock
    ContainerService containerService;
    @Mock
    ContainerTrackingService containerTrackingService;

    @Mock
    AllDistricts allDistricts;

    @Mock
    ContainerClosureRequest containerClosureRequest;

    @Mock
    Container container;

    @Mock
    private ReasonForClosureValidator reasonForClosureValidator;

    @Before
    public void setUp() {
        initMocks(this);
        containerTrackingController = new InTreatmentContainerTrackingController(containerService, reasonForClosureValidator, allDistricts);
    }

    @Test
    public void shouldPopulateUIModelForInTreatmentDashboard() throws Exception {
        ArrayList<ReasonForContainerClosure> reasons = new ArrayList<>();
        List<ReasonForContainerClosure> reasonsForFilter = new ArrayList<>();
        List<District> districts = asList(new District("D1"), new District("D2"));

        when(containerService.getAllInTreatmentClosureReasonsForAdmin()).thenReturn(reasons);
        when(containerService.getAllInTreatmentClosureReasons()).thenReturn(reasonsForFilter);
        when(allDistricts.getAll()).thenReturn(districts);

        standaloneSetup(containerTrackingController).build()
                .perform(get("/sputum-tracking/in-treatment/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().size(6))
                .andExpect(model().attribute(CONTAINER_STATUS_LIST, ContainerStatus.allNames()))
                .andExpect(model().attribute(LAB_RESULTS, SmearTestResult.allNames()))
                .andExpect(model().attribute(REASONS, reasons))
                .andExpect(model().attribute(REASONS_FOR_FILTER, reasonsForFilter))
                .andExpect(model().attribute(DISTRICTS, allDistricts.getAll()))
                .andExpect(model().attribute(INSTANCES, SputumTrackingInstance.IN_TREATMENT_INSTANCES));

        verify(containerService).getAllInTreatmentClosureReasonsForAdmin();
    }

    @Test
    public void shouldPopulateErrorsIfReasonForClosureRequestFailsValidation() throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add("some error");
        when(reasonForClosureValidator.validate(any(ContainerClosureRequest.class))).thenReturn(errors);

        List<ReasonForContainerClosure> reasons = new ArrayList<>();
        when(containerService.getAllInTreatmentClosureReasonsForAdmin()).thenReturn(reasons);

        standaloneSetup(containerTrackingController).build()
                .perform(post("/sputum-tracking/in-treatment/close-container"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("error")));

        verify(reasonForClosureValidator).validate(any(ContainerClosureRequest.class));
    }

    @Test
    public void shouldCloseContainer() throws Exception {
        when(reasonForClosureValidator.validate(any(ContainerClosureRequest.class))).thenReturn(new ArrayList<String>());

        standaloneSetup(containerTrackingController).build()
                .perform(post("/sputum-tracking/in-treatment/close-container"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));

        verify(containerService).closeContainer(any(ContainerClosureRequest.class));
    }

    @Test
    public void shouldOpenContainer() throws Exception {
        String containerId = "1234";
        standaloneSetup(containerTrackingController).build()
                .perform(get("/sputum-tracking/in-treatment/open-container").param("containerId", containerId))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(containerService).openContainer(containerId);
    }
}
