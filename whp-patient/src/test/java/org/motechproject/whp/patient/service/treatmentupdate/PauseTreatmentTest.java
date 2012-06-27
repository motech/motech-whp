package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.command.PauseTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.TreatmentService;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class PauseTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTherapies allTherapies;
    @Mock
    private TreatmentService treatmentService;
    private PauseTreatment pauseTreatment;

    @Before
    public void setUp() {
        initMocks(this);
        pauseTreatment = new PauseTreatment(allPatients, allTherapies, treatmentService);
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfPatientTreatmentIsAlreadyPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_PAUSED);
        pauseTreatment.apply(patientRequest);
        verify(treatmentService, never()).pauseTreatment(patientRequest);
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        pauseTreatment.apply(patientRequest);
        verify(treatmentService, never()).pauseTreatment(patientRequest);
    }

    @Test
    public void shouldNotPauseTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_PAUSED);
        pauseTreatment.apply(patientRequest);
        verify(treatmentService, never()).pauseTreatment(patientRequest);
    }

    @Test
    public void shouldNotPauseTreatmentIfCurrentTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
        pauseTreatment.apply(patientRequest);
        verify(treatmentService, never()).pauseTreatment(patientRequest);
    }

    @Test
    public void shouldNotPauseTreatmentIfAndPatientTreatmentIsPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_CLOSED);
        pauseTreatment.apply(patientRequest);
        verify(treatmentService, never()).pauseTreatment(patientRequest);
    }

    @Test
    public void shouldPauseTreatmentIfPatientTreatmentCanBePaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        pauseTreatment.apply(patientRequest);
        verify(treatmentService).pauseTreatment(patientRequest);
    }
}
