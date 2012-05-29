package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.OpenNewTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;


public class OpenNewTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTreatments allTreatments;

    private OpenNewTreatment openNewTreatment;
    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        openNewTreatment = new OpenNewTreatment(allPatients, allTreatments);
    }

    @Test
    public void shouldNotOpenNewTreatment_OnAnyErrors() {
        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForOpenNewTreatment().build();
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        openNewTreatment.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldOpenNewTreatmentAndUpdatePatient_IfNoErrorsFound() {
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, now());
        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForOpenNewTreatment().build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        openNewTreatment.apply(patientRequest);
        verify(allPatients).update(patient);
    }

}
