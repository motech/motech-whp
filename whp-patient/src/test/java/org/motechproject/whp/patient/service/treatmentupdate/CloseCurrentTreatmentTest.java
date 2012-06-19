package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.command.CloseCurrentTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.TreatmentService;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class CloseCurrentTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTherapies allTherapies;
    @Mock
    private TreatmentService treatmentService;

    private CloseCurrentTreatment closeCurrentTreatment;

    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        closeCurrentTreatment = new CloseCurrentTreatment(allPatients, allTherapies, treatmentService);
    }


    @Test(expected = WHPRuntimeException.class)
    public void shouldNotCloseCurrentTreatmentIfPatientTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(treatmentService, never()).closeTreatment(patientRequest);
    }

    @Test(expected = WHPRuntimeException.class)
    public void shouldNotCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbId() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(someOtherTbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(treatmentService, never()).closeTreatment(patientRequest);
    }

    @Test(expected = WHPRuntimeException.class)
    public void shouldNotCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbIdAndTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(someOtherTbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(treatmentService, never()).closeTreatment(patientRequest);
    }

    @Test(expected = WHPRuntimeException.class)
    public void shouldNotCloseCurrentTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatment(null).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(treatmentService, never()).closeTreatment(patientRequest);
    }

    @Test(expected = WHPRuntimeException.class)
    public void shouldNotCloseCurrentTreatmentIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setCase_id("caseId");
        patientRequest.setTb_id("tbId");
        when(allPatients.findByPatientId(anyString())).thenReturn(null);

        closeCurrentTreatment.apply(patientRequest);
        verify(treatmentService, never()).closeTreatment(patientRequest);
    }

    @Test
    public void shouldCloseCurrentTreatmentIfPatientTreatmentCanBeClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(treatmentService).closeTreatment(patientRequest);
    }
}
