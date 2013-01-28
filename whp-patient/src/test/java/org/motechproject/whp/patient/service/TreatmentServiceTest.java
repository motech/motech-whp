package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.motechproject.whp.patient.alerts.scheduler.PatientAlertScheduler;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.user.service.ProviderService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

public class TreatmentServiceTest {

    @Mock
    private PatientService patientService;
    @Mock
    private ProviderService providerService;
    @Mock
    private PatientAlertScheduler patientAlertScheduler;

    private TreatmentService treatmentService;

    @Mock
    private PatientMapper patientMapper;

    @Before
    public void setUp() {
        initMocks(this);
        Patient patient = new PatientBuilder().withDefaults().build();
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);
        treatmentService = new TreatmentService(patientService, patientMapper, patientAlertScheduler);
    }

    @Test
    public void shouldCloseTreatment() {
        Patient patient = mock(Patient.class);
        PatientRequest patientRequest = mock(PatientRequest.class);
        String caseId = "caseId";
        String patientId = "patientId";

        when(patientRequest.getCase_id()).thenReturn(caseId);
        when(patientService.findByPatientId(caseId)).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(patientId);

        treatmentService.closeTreatment(patientRequest);

        InOrder order = inOrder(patient, patientService, patientAlertScheduler);
        order.verify(patient).closeCurrentTreatment(any(TreatmentOutcome.class), any(DateTime.class));
        order.verify(patientService).update(patient);
        order.verify(patientAlertScheduler).unscheduleJob(patientId);
    }

    @Test
    public void shouldOpenTreatment() {
        Patient patient = mock(Patient.class);
        PatientRequest patientRequest = mock(PatientRequest.class);
        String caseId = "caseId";
        String patientId = "patientId";

        when(patientRequest.getCase_id()).thenReturn(caseId);
        when(patientService.findByPatientId(caseId)).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(patientId);

        treatmentService.openTreatment(patientRequest);

        InOrder order = inOrder(patientService, patientAlertScheduler);
        order.verify(patientService).update(patient);
        order.verify(patientAlertScheduler).scheduleJob(patientId);
    }
    @Test
    public void shouldTransferInTreatment() {
        Patient patient = mock(Patient.class);
        PatientRequest patientRequest = mock(PatientRequest.class);
        String caseId = "caseId";
        String patientId = "patientId";

        when(patientRequest.getCase_id()).thenReturn(caseId);
        when(patientService.findByPatientId(caseId)).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(patientId);

        treatmentService.transferInPatient(patientRequest);

        InOrder order = inOrder(patient, patientService, patientAlertScheduler);
        order.verify(patient).reviveLatestTherapy();
        order.verify(patientService).update(patient);
        order.verify(patientAlertScheduler).scheduleJob(patientId);
    }
}
