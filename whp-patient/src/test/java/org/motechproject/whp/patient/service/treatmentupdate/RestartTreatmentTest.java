package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;

import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.now;

public class RestartTreatmentTest {

    private AllPatients allPatients;
    private RestartTreatment restartTreatment;
    private TreatmentUpdateRequest treatmentUpdateRequest;

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Before
    public void setUp() {
        allPatients = mock(AllPatients.class);
        treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withDefaults().build();
        restartTreatment = new RestartTreatment();
    }

    @Test
    public void shouldNotRestartCurrentTreatment_OnAnyErrors() {
        Patient patient = new PatientBuilder().withDefaults().build();
        expectWHPDomainException("Cannot restart treatment for this case: [Current treatment is already in progress]");
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(allPatients, null, treatmentUpdateRequest);
    }

    @Test
    public void shouldRestartAndUpdatePatientCurrentTreatment_IfNoErrorsFound() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.pauseCurrentTreatment("paws", now());
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        restartTreatment.apply(allPatients, null, treatmentUpdateRequest);
        verify(allPatients).update(patient);
    }

    protected void expectWHPDomainException(String message) {
        exceptionThrown.expect(WHPDomainException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }
}
