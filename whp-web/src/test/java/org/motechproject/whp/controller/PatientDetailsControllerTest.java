package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.mapper.PatientInfoMapper;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.remarks.ProviderRemarksService;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientDetailsControllerTest {
    Patient patient;
    Provider provider;
    private List<TherapyRemark> cmfAdminRemarks;
    private List<AuditLog> auditLogs;
    PatientDetailsController patientDetailsController;

    @Mock
    PatientService patientService;
    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    ProviderService providerService;
    @Mock
    ProviderRemarksService providerRemarksService;
    @Mock
    HttpServletRequest request;
    @Mock
    Model uiModel;
    @Mock
    HttpSession session;
    @Mock
    AlertColorConfiguration alertColorConfiguration;
    @Mock
    PatientInfoMapper patientInfoMapper;

    @Before
    public void setup() {
        initMocks(this);
        String providerId = "providerid";

        patientDetailsController = new PatientDetailsController(patientService, treatmentUpdateOrchestrator, providerService, providerRemarksService, patientInfoMapper);
        patient = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).build();
        provider = newProviderBuilder().withDefaults().withProviderId(providerId).build();
        cmfAdminRemarks = mock(List.class);
        auditLogs = mock(List.class);

        when(request.getSession()).thenReturn(session);
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(providerService.findByProviderId(providerId)).thenReturn(provider);
        when(providerRemarksService.getRemarks(patient)).thenReturn(auditLogs);
        when(patientService.getCmfAdminRemarks(patient)).thenReturn(cmfAdminRemarks);
    }


    @Test
    public void shouldReturnUIModelToShowPatientDetails() throws Exception {
        PatientInfo patientInfo = new PatientInfo();

        when(patientInfoMapper.map(patient, provider)).thenReturn(patientInfo);

        standaloneSetup(patientDetailsController).build()
                .perform(get("/patients/show").param("patientId", patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(5))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("cmfAdminRemarks", cmfAdminRemarks))
                .andExpect(model().attribute("providerRemarks", auditLogs))
                .andExpect(model().attribute("phaseStartDates", new PhaseStartDates(patient)))
                .andExpect(forwardedUrl("patient/show"));

        verify(patientInfoMapper, times(1)).map(patient, provider);
    }


    @Test
    public void shouldDisplayAdherenceUpdatedMessageIfAdherenceWasUpdated() {
        when(request.getAttribute("flash.in.message")).thenReturn("message");
        patientDetailsController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel).addAttribute("message", "message");
    }

    @Test
    public void shouldUpdateDoseInterruptionsOnPatientWhenNavigatedToDashboardPage() {
        patientDetailsController.show(patient.getPatientId(), uiModel, request);

        verify(treatmentUpdateOrchestrator, times(1)).updateDoseInterruptions(patient);
    }

    @Test
    public void shouldNotDisplayAdherenceUpdatedMessageIfAdherenceWasNotUpdated() {
        when(request.getAttribute(anyString())).thenReturn(null);
        patientDetailsController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel, never()).addAttribute(eq("message"), any());
    }

}
