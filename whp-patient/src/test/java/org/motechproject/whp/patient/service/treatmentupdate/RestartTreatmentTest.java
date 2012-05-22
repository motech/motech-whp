package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class RestartTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTreatments allTreatments;

    private RestartTreatment restartTreatment;
    private TreatmentUpdateRequest treatmentUpdateRequest;

    @Before
    public void setUp() {
        initMocks(this);
        treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withDefaults().build();
        restartTreatment = new RestartTreatment(allPatients, allTreatments);
    }

    @Test
    public void shouldNotRestartCurrentTreatment_OnAnyErrors() {
        Patient patient = new PatientBuilder().withDefaults().build();
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_ALREADY_IN_PROGRESS);
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(treatmentUpdateRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldRestartAndUpdatePatientCurrentTreatment_IfNoErrorsFound() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.pauseCurrentTreatment("paws", now());
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(treatmentUpdateRequest);
        verify(allPatients).update(patient);
    }

}
