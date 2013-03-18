package org.motechproject.whp.patient.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.patient.alerts.scheduler.PatientAlertScheduler;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.user.service.ProviderService;

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
        String dateModified = "25/11/1986 00:00:00";
        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment()
                .withCloseTreatmentRemarks("remarks")
                .withCaseId(PATIENT_ID)
                .withDateModified(dateModified).build();

        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(PATIENT_ID);

        treatmentService.closeTreatment(patientRequest);

        InOrder order = inOrder(patient, patientService, patientAlertScheduler);
        order.verify(patient).closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getRemarks(), WHPDateTime.date(dateModified).dateTime());
        order.verify(patientService).update(patient);
        order.verify(patientAlertScheduler).unscheduleJob(PATIENT_ID);
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
