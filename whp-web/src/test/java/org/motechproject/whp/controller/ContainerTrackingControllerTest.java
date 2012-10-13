package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.tracking.service.ContainerTrackingService;
import org.motechproject.whp.container.tracking.validation.ReasonForClosureValidator;
import org.motechproject.whp.refdata.domain.AlternateDiagnosis;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.controller.ContainerTrackingController.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ContainerTrackingControllerTest {

    ContainerTrackingController containerTrackingController;

    @Mock
    ContainerService containerService;
    @Mock
    ContainerTrackingService containerTrackingService;

    @Mock
    ContainerClosureRequest containerClosureRequest;

    @Mock
    Container container;

    @Mock
    private ReasonForClosureValidator reasonForClosureValidator;

    @Before
    public void setUp() {
        initMocks(this);
        containerTrackingController = new ContainerTrackingController(containerService, containerTrackingService, reasonForClosureValidator);
    }

    @Test
    public void shouldPopulateReasonsForClosure() throws Exception {
        ArrayList<ReasonForContainerClosure> reasons = new ArrayList<>();
        ArrayList<AlternateDiagnosis> alternateDiagnosises = new ArrayList<>();

        when(containerTrackingService.getAllClosureReasonsForAdmin()).thenReturn(reasons);
        when(containerTrackingService.getAllAlternateDiagnosis()).thenReturn(alternateDiagnosises);

        standaloneSetup(containerTrackingController).build()
                .perform(get("/sputum-tracking/pre-treatment"))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(REASONS, reasons))
                .andExpect(model().attribute(ALTERNATE_DIAGNOSIS_LIST, alternateDiagnosises));

        verify(containerTrackingService).getAllClosureReasonsForAdmin();
        verify(containerTrackingService).getAllAlternateDiagnosis();
    }

    @Test
    public void shouldPopulateErrorsIfReasonForClosureRequestFailsValidation() throws Exception {
        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
        errors.add(new ErrorWithParameters("some code", "some error"));
        when(reasonForClosureValidator.validate(any(ContainerClosureRequest.class))).thenReturn(errors);

        List<ReasonForContainerClosure> reasons = new ArrayList<>();
        List<AlternateDiagnosis> alternateDiagnosis = new ArrayList<>();
        when(containerTrackingService.getAllClosureReasonsForAdmin()).thenReturn(reasons);
        when(containerTrackingService.getAllAlternateDiagnosis()).thenReturn(alternateDiagnosis);

        standaloneSetup(containerTrackingController).build()
                .perform(post("/sputum-tracking/close-container"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ERRORS, errors))
                .andExpect(model().attribute(REASONS, reasons))
                .andExpect(model().attribute(ALTERNATE_DIAGNOSIS_LIST, alternateDiagnosis))
                .andExpect(forwardedUrl("sputum-tracking/pre-treatment"));

        verify(reasonForClosureValidator).validate(any(ContainerClosureRequest.class));
        verify(containerTrackingService).getAllClosureReasonsForAdmin();
        verify(containerTrackingService).getAllAlternateDiagnosis();
    }

    @Test
    public void shouldUpdateReasonForClosure() throws Exception {
        when(reasonForClosureValidator.validate(any(ContainerClosureRequest.class))).thenReturn(new ArrayList<ErrorWithParameters>());

        standaloneSetup(containerTrackingController).build()
                .perform(post("/sputum-tracking/close-container"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/sputum-tracking/pre-treatment"));

        verify(containerService).closeContainer(any(ContainerClosureRequest.class));
    }
}
