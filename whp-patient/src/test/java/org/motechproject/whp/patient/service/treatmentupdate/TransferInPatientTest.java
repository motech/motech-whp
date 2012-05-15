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
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.now;

public class TransferInPatientTest {

    private AllPatients allPatients;
    private AllTreatments allTreatments;
    private TransferInPatient transferInPatient;
    private Patient patient;
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Before
    public void setUp() {
        allPatients = mock(AllPatients.class);
        allTreatments = mock(AllTreatments.class);
        patient = new PatientBuilder().withDefaults().build();
        transferInPatient = new TransferInPatient();
    }

    @Test
    public void shouldNotTransferInPatientTreatment_OnAnyErrors() {
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForTransferInTreatment().withOldTbId("wrongTbId").build();
        expectWHPDomainException("Cannot TransferIn patient: [No such tb id for current treatment]");
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(allPatients, allTreatments, treatmentUpdateRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldTransferInPatientTreatmentAndUpdatePatient_IfNoErrorsFound() {
        patient.closeCurrentTreatment("Defaulted", now());
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForTransferInTreatment().build();
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(allPatients, allTreatments, treatmentUpdateRequest);
        verify(allPatients).update(patient);
    }

    protected void expectWHPDomainException(String message) {
        exceptionThrown.expect(WHPDomainException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }
}
