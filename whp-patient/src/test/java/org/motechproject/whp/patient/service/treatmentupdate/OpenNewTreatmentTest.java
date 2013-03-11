package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.command.OpenNewTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.TreatmentService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;


public class OpenNewTreatmentTest extends BaseUnitTest {

    @Mock
    private PatientService patientService;
    @Mock
    private TreatmentService treatmentService;

    private OpenNewTreatment openNewTreatment;

    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        openNewTreatment = new OpenNewTreatment(patientService, treatmentService);
    }

    @Test
    public void shouldNotOpenTreatmentIfPatientAlreadyHasACurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);
        openNewTreatment.apply(patientRequest);
        verify(treatmentService, never()).openTreatment(patientRequest);
    }

    @Test
    public void shouldNotOpenTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatment(null).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
        openNewTreatment.apply(patientRequest);
        verify(treatmentService, never()).openTreatment(patientRequest);
    }

    @Test
    public void shouldNotOpenTreatmentIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setCase_id("caseId");
        patientRequest.setTb_id("tbId");
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);
        openNewTreatment.apply(patientRequest);
        verify(treatmentService, never()).openTreatment(patientRequest);
    }

    @Test
    public void shouldOpenTreatmentIfNewTreatmentCanBeOpenedForPatient() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, null, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        openNewTreatment.apply(patientRequest);
        verify(treatmentService).openTreatment(patientRequest);
    }
}
