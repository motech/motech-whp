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
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().withTbId("wrongTbId").build();
        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(treatmentUpdateRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldCloseCurrentTreatmentAndUpdatePatient_IfNoErrorsFound() {
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForCloseTreatment().build();
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        closeCurrentTreatment.apply(treatmentUpdateRequest);
        verify(allPatients).update(patient);
    }
}
