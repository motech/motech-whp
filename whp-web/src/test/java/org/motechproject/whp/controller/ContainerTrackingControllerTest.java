package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.tracking.service.ContainerTrackingService;
import org.motechproject.whp.container.tracking.validation.ReasonForClosureValidator;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
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
        containerTrackingController = new ContainerTrackingController(containerService, reasonForClosureValidator, allDistricts);
    }

    @Test
    public void shouldPopulateUIModelForPreTreatmentDashboard() throws Exception {
        ArrayList<ReasonForContainerClosure> reasons = new ArrayList<>();
        ArrayList<AlternateDiagnosis> alternateDiagnosises = new ArrayList<>();
        List<District> districts = asList(new District("D1"), new District("D2"));

        when(containerService.getAllClosureReasonsForAdmin()).thenReturn(reasons);
        when(containerService.getAllAlternateDiagnosis()).thenReturn(alternateDiagnosises);
        when(allDistricts.getAll()).thenReturn(districts);

        standaloneSetup(containerTrackingController).build()
                .perform(get("/sputum-tracking/pre-treatment"))
                .andExpect(status().isOk())
                .andExpect(model().size(6))
                .andExpect(model().attribute("diagnosisList", Diagnosis.allNames()))
                .andExpect(model().attribute("containerStatusList", ContainerStatus.allNames()))
                .andExpect(model().attribute("labResults", SmearTestResult.allNames()))
                .andExpect(model().attribute(REASONS, reasons))
                .andExpect(model().attribute(DISTRICTS, allDistricts.getAll()))
                .andExpect(model().attribute(ALTERNATE_DIAGNOSIS_LIST, alternateDiagnosises));

        verify(containerService).getAllClosureReasonsForAdmin();
        verify(containerService).getAllAlternateDiagnosis();
    }

    @Test
    public void shouldPopulateErrorsIfReasonForClosureRequestFailsValidation() throws Exception {
        ArrayList<ErrorWithParameters> errors = new ArrayList<>();
        errors.add(new ErrorWithParameters("some code", "some error"));
        when(reasonForClosureValidator.validate(any(ContainerClosureRequest.class))).thenReturn(errors);

        List<ReasonForContainerClosure> reasons = new ArrayList<>();
        List<AlternateDiagnosis> alternateDiagnosis = new ArrayList<>();
        when(containerService.getAllClosureReasonsForAdmin()).thenReturn(reasons);
        when(containerService.getAllAlternateDiagnosis()).thenReturn(alternateDiagnosis);

        standaloneSetup(containerTrackingController).build()
                .perform(post("/sputum-tracking/close-container"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ERRORS, errors))
                .andExpect(model().attribute(REASONS, reasons))
                .andExpect(model().attribute(ALTERNATE_DIAGNOSIS_LIST, alternateDiagnosis))
                .andExpect(forwardedUrl("sputumTracking/preTreatment"));

        verify(reasonForClosureValidator).validate(any(ContainerClosureRequest.class));
        verify(containerService).getAllClosureReasonsForAdmin();
        verify(containerService).getAllAlternateDiagnosis();
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
