package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PauseTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTreatments allTreatments;

    private PauseTreatment pauseTreatment;
    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        pauseTreatment = new PauseTreatment(allPatients, allTreatments);
    }

    @Test
    public void shouldNotPauseCurrentTreatment_OnAnyErrors() {
        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withTbId("wrongTbId").build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        pauseTreatment.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldPauseAndUpdatePatientCurrentTreatment_IfNoErrorsFound() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        pauseTreatment.apply(patientRequest);
        verify(allPatients).update(patient);
    }

}
