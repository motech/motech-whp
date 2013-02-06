package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.builder.TreatmentWeekInstanceBuilder;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.model.AlertDateFilters;
import org.motechproject.whp.patient.model.AlertTypeFilters;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.remarks.ProviderRemarksService;
import org.motechproject.whp.service.PatientPagingService;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
    PatientPagingService patientPagingService;

    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Mock
    WHPAdherenceService whpAdherenceService;
    @Mock
    AllDistricts allDistrictsCache;
    @Mock
    TreatmentCardService treatmentCardService;
    @Mock
    HttpSession session;
    @Mock
    ProviderRemarksService providerRemarksService;
    @Mock
    AllTreatmentCategories allTreatmentCategories;
    @Mock
    List<TreatmentCategory> treatmentCategories;


    AbstractMessageSource messageSource;

    Patient patient;
    Provider provider;
    PatientController patientController;

    List<District> districts = asList(new District("Vaishali"), new District("Begusarai"));
    AlertTypeFilters alertTypes = new AlertTypeFilters();
    AlertDateFilters alertDates = new AlertDateFilters();

    private static final String LOGGED_IN_USER_NAME = "username";
    private List<TherapyRemark> cmfAdminRemarks;
    private List<AuditLog> auditLogs;
    private TreatmentWeekInstance treatmentWeekInstance;


    @Before
    public void setup() {
        initMocks(this);
        String providerId = "providerid";

        setupMessageSource();
        when(request.getSession()).thenReturn(session);
        setupLoggedInUser(session, LOGGED_IN_USER_NAME);

        treatmentWeekInstance = TreatmentWeekInstanceBuilder.build();

        patientController = new PatientController(patientService, treatmentCardService, treatmentUpdateOrchestrator, providerService, messageSource, allDistrictsCache, providerRemarksService, allTreatmentCategories, treatmentWeekInstance);
        patient = new PatientBuilder().withDefaults().withTreatmentUnderProviderId(providerId).build();
        provider = newProviderBuilder().withDefaults().withProviderId(providerId).build();
        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(providerService.findByProviderId(providerId)).thenReturn(provider);
        when(allDistrictsCache.getAll()).thenReturn(districts);
        cmfAdminRemarks = mock(List.class);
        when(patientService.getCmfAdminRemarks(patient)).thenReturn(cmfAdminRemarks);
        auditLogs = mock(List.class);
        when(providerRemarksService.getRemarks(patient)).thenReturn(auditLogs);
        when(allTreatmentCategories.getAll()).thenReturn(treatmentCategories);
    }

    private void setupMessageSource() {
        messageSource = new StaticMessageSource();
        ((StaticMessageSource) messageSource).addMessage("dates.changed.message", Locale.ENGLISH, "message");
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patient);
        PatientInfo patientInfo = new PatientInfo(patient);
        when(patientService.getAllWithActiveTreatmentForProvider(LOGGED_IN_USER_NAME)).thenReturn(patientsForProvider);
        String view = patientController.listByProvider(uiModel, request);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), captor.capture());

        assertEquals(patientInfo, captor.getValue().get(0));
        assertEquals("patient/listByProvider", view);
    }

    @Test
    public void shouldReturnDashBoardView() throws Exception {
        PatientInfo patientInfo = new PatientInfo(patient, provider);
        standaloneSetup(patientController).build()
                .perform(get("/patients/show").param("patientId", patient.getPatientId()))
                .andExpect(status().isOk())
                .andExpect(model().size(6))
                .andExpect(model().attribute("patient", patientInfo))
                .andExpect(model().attribute("cmfAdminRemarks", cmfAdminRemarks))
                .andExpect(model().attribute("providerRemarks", auditLogs))
                .andExpect(model().attribute("phaseStartDates", new PhaseStartDates(patient)))
                .andExpect(model().attributeExists("patientAlerts"))
                .andExpect(forwardedUrl("patient/show"));
    }

    @Test
    public void shouldReturnDashBoardPrintView() throws Exception {
        PatientInfo patientInfo = new PatientInfo(patient, provider);
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
    public void shouldDisplayAdherenceUpdatedMessageIfAdherenceWasUpdated() {
        when(request.getAttribute("flash.in.message")).thenReturn("message");
        patientController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel).addAttribute("message", "message");
    }

    @Test
    public void shouldUpdateDoseInterruptionsOnPatientWhenNavigatedToDashboardPage() {
        patientController.show(patient.getPatientId(), uiModel, request);

        verify(treatmentUpdateOrchestrator, times(1)).updateDoseInterruptions(patient);
    }

    @Test
    public void shouldNotDisplayAdherenceUpdatedMessageIfAdherenceWasNotUpdated() {
        when(request.getAttribute(anyString())).thenReturn(null);
        patientController.show(patient.getPatientId(), uiModel, request);
        verify(uiModel, never()).addAttribute(eq("message"), any());
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
    public void shouldSetUpUiModelForListAllPatients() throws Exception {
        standaloneSetup(patientController).build()
                .perform(get("/patients/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("districts", districts))
                .andExpect(model().attribute("alertTypes", alertTypes))
                .andExpect(model().attribute("alertDates", alertDates))
                .andExpect(model().attribute("treatmentCategories", treatmentCategories))
                .andExpect(view().name("patient/list"));
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
