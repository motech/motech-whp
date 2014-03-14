package org.motechproject.whp.controller;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.adherenceapi.adherence.AdherenceConfirmationOverIVR;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.ContainerTrackingService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.service.PatientService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientManagementControllerTest {

    PatientManagementController patientManagementController;

    @Mock
    PatientService patientService;
    @Mock
    private AdherenceLogService adherenceLogService;
    @Mock
    private AdherenceAuditService adherenceAuditService;
    @Mock
    private ContainerTrackingService containerTrackingService;
    @Mock
    private ContainerService containerService;
    @Mock
    private AdherenceConfirmationOverIVR adherenceConfirmationOverIVR;

    private String CONTRIB_FLASH_OUT_PREFIX = "flash.out.";
    private String CONTRIB_FLASH_IN_PREFIX = "flash.in.";

    @Before
    public void setUp() {
        initMocks(this);
        patientManagementController = new PatientManagementController(patientService,adherenceLogService,adherenceAuditService,containerTrackingService,containerService,adherenceConfirmationOverIVR);
    }

    @Test
    public void shouldReturnView() throws Exception {
        standaloneSetup(patientManagementController).build()
                .perform(get("/managepatients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/manage"));
    }
    @Test
    public void shouldReturnDeletePatientView() throws Exception {
        standaloneSetup(patientManagementController).build()
                .perform(get("/managepatients/deletePatient"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/deletePatients"));
    }

    @Test
    public void shouldReturnTreatmentViewWithTreatmentWhenPatientFound() throws Exception {
        String patientId = "patientId";

        Patient patient = PatientBuilder.patient();
        String tbId = patient.getCurrentTreatment().getTbId();
        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        standaloneSetup(patientManagementController).build()
                .perform(get("/managepatients/treatment").param("patientId", patientId).param("tbId", tbId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("treatment", patient.getTreatmentBy(tbId)))
                .andExpect(view().name("patient/treatmentDetails"));
    }

    @Test
    public void shouldReturnTreatmentViewWhenNoPatientIsFound() throws Exception {
        String patientId = "patientId";

        Patient patient = PatientBuilder.patient();
        String tbId = patient.getCurrentTreatment().getTbId();
        when(patientService.findByPatientId(patientId)).thenReturn(null);

        standaloneSetup(patientManagementController).build()
                .perform(get("/managepatients/treatment").param("patientId", patientId).param("tbId", tbId))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/treatmentDetails"));
    }

    @Test
    public void shouldRemoveGivenTreatment() throws Exception {
        String patientId = "patientId";

        Patient patient = PatientBuilder.patient();
        String tbId = patient.getCurrentTreatment().getTbId();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, "remarks", DateTime.now());

        Therapy currentTherapy = new TherapyBuilder().withDefaults().build();
        String currentTbId = "currentTbId";
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTbId(currentTbId).build();
        patient.addTreatment(currentTreatment, currentTherapy, now(), now());

        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        standaloneSetup(patientManagementController).build()
                .perform(post("/managepatients/removeTreatment").param("patientId", patientId).param("tbId", tbId))
                .andExpect(status().isOk())
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Treatment with id " + tbId + " removed successfully."))
                .andExpect(redirectedUrl("/managepatients"));

        assertThat(patient.getTherapyHistory().size(), is(0));
        verify(patientService).update(patient);
    }


    @Test
    public void shouldDisplayErrorMessageWhenGivenTbIdCannotBeRemoved() throws Exception {
        String patientId = "patientId";

        Patient patient = PatientBuilder.patient();
        String tbId = patient.getCurrentTreatment().getTbId();
        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        standaloneSetup(patientManagementController).build()
                .perform(post("/managepatients/removeTreatment").param("patientId", patientId).param("tbId", tbId))
                .andExpect(status().isOk())
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Treatment with id " + tbId + " cannot be removed."))
                .andExpect(redirectedUrl("/managepatients"));

        verify(patientService).findByPatientId(patientId);
        verifyNoMoreInteractions(patientService);
    }


    @Test
    public void shouldDisplayTheManagePatientPageWithSuccessfullyRegisteredFlashMessage() throws Exception {
        String message = "Treatment with id tbId removed successfully.";
        standaloneSetup(patientManagementController).build()
                .perform(get("/managepatients").requestAttr(CONTRIB_FLASH_IN_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, message))
                .andExpect(status().isOk())
                .andExpect(model().attribute(WHPConstants.NOTIFICATION_MESSAGE, message))
                .andExpect(view().name("patient/manage"));
    }

    @Test
    public void shouldDisplayThePatientDetailsQueriedForIfFound() throws Exception{
        String patientId = "patientId";
        Patient patient = PatientBuilder.patient();
        when(patientService.findByPatientId(patientId)).thenReturn(patient);
        checkView(patientId);
    }

    @Test
    public void shouldNotDisplayThePatientDetailsQueriedForPatientNotFound() throws Exception{
        String patientId = "patientId";
        when(patientService.findByPatientId(patientId)).thenReturn(null);
        checkView(patientId);
    }
    private void checkView(String patientId) throws Exception{
        standaloneSetup(patientManagementController).build()
                .perform(get("/managepatients/patient").param("patientId", patientId))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patient"));
    }
    @Test
    public void shouldDeletePatient() throws Exception{
        String patientId = "patientId";
        Patient patient = PatientBuilder.patient();
        patient.getCurrentTherapy().setStatus(TherapyStatus.Closed);
        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        standaloneSetup(patientManagementController).build()
                .perform(post("/managepatients/delete").param("patientId", patientId))
                .andExpect(status().isOk())
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Patient with id " + patientId  + " removed successfully."))
                .andExpect(redirectedUrl("/managepatients/deletePatient"));

        verify(patientService).removePatient(patientId);
    }

    @Test
    public void shouldNotDeleteThePatientDetailsQueriedForPatientNotFound() throws Exception{
        String patientId = "patientId";
        when(patientService.findByPatientId(patientId)).thenReturn(null);
        standaloneSetup(patientManagementController).build()
                .perform(post("/managepatients/delete").param("patientId", patientId))
                .andExpect(status().isOk())
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Patient with id " + patientId  + " not found."))
                .andExpect(redirectedUrl("/managepatients/deletePatient"));
    }

    @Test
    public void shouldNotDeletePatientIfCurrentTreatmentIsClosed() throws Exception{
        String patientId = "patientId";
        Patient patient = PatientBuilder.patient();
        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        standaloneSetup(patientManagementController).build()
                .perform(post("/managepatients/delete").param("patientId", patientId))
                .andExpect(status().isOk())
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Patient with id " + patientId  + " has an open treatment. Cannot Delete patient details! Please close the treatment and retry!"))
                .andExpect(redirectedUrl("/managepatients/deletePatient"));

    }

    @Test
    public void shouldDeletePatientIfCurrentTreatmentIsClosed() throws Exception{
        String patientId = "patientId";
        Patient patient = PatientBuilder.patient();
        patient.getCurrentTherapy().setStatus(TherapyStatus.Closed);
        when(patientService.findByPatientId(patientId)).thenReturn(patient);

        standaloneSetup(patientManagementController).build()
                .perform(post("/managepatients/delete").param("patientId", patientId))
                .andExpect(status().isOk())
                .andExpect(request().attribute(CONTRIB_FLASH_OUT_PREFIX + WHPConstants.NOTIFICATION_MESSAGE, "Patient with id " + patientId  + " removed successfully."))
                .andExpect(redirectedUrl("/managepatients/deletePatient"));

        verify(patientService).removePatient(patientId);

    }

}
