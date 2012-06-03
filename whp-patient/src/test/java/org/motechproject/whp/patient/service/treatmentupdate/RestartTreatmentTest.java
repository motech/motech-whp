package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.RestartTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class RestartTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTherapies allTherapies;

    private RestartTreatment restartTreatment;
    private PatientRequest patientRequest;

    @Before
    public void setUp() {
        initMocks(this);
        patientRequest = new PatientRequestBuilder().withDefaults().build();
        restartTreatment = new RestartTreatment(allPatients, allTherapies);
    }

    @Test
    public void shouldNotRestartCurrentTreatment_OnAnyErrors() {
        Patient patient = new PatientBuilder().withDefaults().build();
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_IN_PROGRESS);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldRestartAndUpdatePatientCurrentTreatment_IfNoErrorsFound() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.pauseCurrentTreatment("paws", now());
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(patientRequest);
        verify(allPatients).update(patient);
    }

}
