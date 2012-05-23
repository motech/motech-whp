package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.CloseCurrentTreatment;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CloseCurrentTreatmentTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTreatments allTreatments;

    private CloseCurrentTreatment closeCurrentTreatment;
    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        closeCurrentTreatment = new CloseCurrentTreatment(allPatients, allTreatments);
    }

    @Test
    public void shouldNotCloseCurrentTreatment_OnAnyErrors() {
        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();
        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldCloseCurrentTreatmentAndUpdatePatient_IfNoErrorsFound() {
        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(patientRequest);
        verify(allPatients).update(patient);
    }

}
