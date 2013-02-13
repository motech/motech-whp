package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.mapper.PatientInfoMapper;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.remarks.ProviderRemarksService;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;
import org.motechproject.whp.treatmentcard.service.TreatmentCardService;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.PhaseStartDates;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientControllerTest extends BaseControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    PatientService patientService;
    @Mock
    ProviderService providerService;
    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    WHPAdherenceService whpAdherenceService;
    @Mock
    TreatmentCardService treatmentCardService;
    @Mock
    HttpSession session;
    @Mock
    ProviderRemarksService providerRemarksService;

    AbstractMessageSource messageSource;
    Patient patient;
    Provider provider;

    PatientController patientController;

    private static final String LOGGED_IN_USER_NAME = "username";
    private List<TherapyRemark> cmfAdminRemarks;
    private List<AuditLog> auditLogs;


    @Before
    public void setup() {
        initMocks(this);
        String providerId = "providerid";

        setupMessageSource();
        when(request.getSession()).thenReturn(session);
        setupLoggedInUser(session, LOGGED_IN_USER_NAME);

        patientController = new PatientController(patientService, treatmentCardService, treatmentUpdateOrchestrator, providerService, messageSource, providerRemarksService);
        patient = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).build();
        provider = newProviderBuilder().withDefaults().withProviderId(providerId).build();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(providerService.findByProviderId(providerId)).thenReturn(provider);
        cmfAdminRemarks = mock(List.class);
        when(patientService.getCmfAdminRemarks(patient)).thenReturn(cmfAdminRemarks);
        auditLogs = mock(List.class);
        when(providerRemarksService.getRemarks(patient)).thenReturn(auditLogs);
    }

    private void setupMessageSource() {
        messageSource = new StaticMessageSource();
        ((StaticMessageSource) messageSource).addMessage("dates.changed.message", Locale.ENGLISH, "message");
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patient);
        PatientInfoMapper patientInfoMapper = new PatientInfoMapper();

        PatientInfo patientInfo = patientInfoMapper.map(patient);
        when(patientService.getAllWithActiveTreatmentForProvider(LOGGED_IN_USER_NAME)).thenReturn(patientsForProvider);
        String view = patientController.listByProvider(uiModel, request);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), captor.capture());

        assertEquals(patientInfo, captor.getValue().get(0));
        assertEquals("patient/listByProvider", view);
    }

    @Test
    public void shouldReturnDashBoardPrintView() throws Exception {
        PatientInfoMapper patientInfoMapper = new PatientInfoMapper();
        PatientInfo patientInfo = patientInfoMapper.map(patient, provider);

        TreatmentCard treatmentCard = new TreatmentCard(patient);
        when(treatmentCardService.treatmentCard(patient)).thenReturn(treatmentCard);
        standaloneSetup(patientController).build()
                .perform(get("/patients/print/" + patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("treatmentCard", treatmentCard))
                .andExpect(forwardedUrl("patient/print"));
    }

    @Test
    public void shouldUpdatePatientPhaseStartDatesAndShowPatient() {
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("21/05/2012");

        String view = patientController.adjustPhaseStartDates(patient.getPatientId(), phaseStartDates, request);
        verify(treatmentUpdateOrchestrator).adjustPhaseStartDates(patient.getPatientId(), WHPDate.date(phaseStartDates.getIpStartDate()).date(), null, null);
        assertEquals("redirect:/patients/show?patientId=" + patient.getPatientId(), view);
    }

    @Test
    public void shouldAddRemarkToPatientsTherapyAndShowPatient() {
        String remark = "remark";
        List<TherapyRemark> cmfAdminRemarks = mock(List.class);
        when(patientService.getCmfAdminRemarks(patient)).thenReturn(cmfAdminRemarks);
        List<AuditLog> providerRemarks = mock(List.class);
        when(providerRemarksService.getRemarks(patient)).thenReturn(providerRemarks);

        String view = patientController.addRemark(uiModel,patient.getPatientId(), remark, request);

        verify(patientService).addRemark(patient, remark, LOGGED_IN_USER_NAME);
        verify(uiModel).addAttribute("cmfAdminRemarks", cmfAdminRemarks);
        verify(uiModel).addAttribute("providerRemarks", providerRemarks);
        assertEquals("patient/remarks", view);
    }


    @Test
    public void shouldUpdateFlagOnGivenPatientId() throws Exception {
        String patientId = "12345";
        when(patientService.updateFlag(patientId, true)).thenReturn(true);

        standaloneSetup(patientController).build()
                .perform(get("/patients/" + patientId + "/updateFlag").param("value", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(patientService).updateFlag(patientId, true);
    }
}
