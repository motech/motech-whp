package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.RestartTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.TreatmentService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class RestartTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private TreatmentService treatmentService;
    private RestartTreatment restartTreatment;

    private PatientRequest patientRequest;

    @Before
    public void setUp() {
        initMocks(this);
        patientRequest = new PatientRequestBuilder().withDefaults().build();
        restartTreatment = new RestartTreatment(allPatients, treatmentService);
    }

    @Test
    public void shouldNotRestartTreatmentIfPatientTreatmentIsNotPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_IN_PROGRESS);
        restartTreatment.apply(patientRequest);
        verify(treatmentService, never()).restartTreatment(patientRequest);
    }

    @Test
    public void shouldNotRestartTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        restartTreatment.apply(patientRequest);
        verify(treatmentService, never()).restartTreatment(patientRequest);
    }

    @Test
    public void shouldNotRestartTreatmentIfTreatmentIsNotPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        restartTreatment.apply(patientRequest);
        verify(treatmentService, never()).restartTreatment(patientRequest);
    }

    @Test
    public void shouldRestartCurrentTreatmentIfPatientTreatmentCanBeRestarted() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(patientRequest);
        verify(treatmentService).restartTreatment(patientRequest);
    }
}
