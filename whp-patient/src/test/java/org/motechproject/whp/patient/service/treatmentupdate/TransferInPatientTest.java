package org.motechproject.whp.patient.service.treatmentupdate;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.internal.matchers.Contains;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentUpdateRequestBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.ProviderService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class TransferInPatientTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTreatments allTreatments;
    @Mock
    private ProviderService providerService;

    private TransferInPatient transferInPatient;
    private Patient patient;
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        transferInPatient = new TransferInPatient(allPatients, allTreatments, providerService);
    }

    @Test
    public void shouldNotTransferInPatientTreatment_OnAnyErrors() {
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForTransferInTreatment().withOldTbId("wrongTbId").build();
        expectWHPDomainException("Cannot TransferIn patient: [No such tb id for current treatment]");
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(treatmentUpdateRequest);
        verify(providerService, never()).transferIn(treatmentUpdateRequest.getProvider_id(),
                patient,
                treatmentUpdateRequest.getTb_id(),
                treatmentUpdateRequest.getDate_modified());
    }

    @Test
    public void shouldTransferInPatientTreatmentAndUpdatePatient_IfNoErrorsFound() {
        patient.closeCurrentTreatment("Defaulted", now());
        TreatmentUpdateRequest treatmentUpdateRequest = TreatmentUpdateRequestBuilder.startRecording().withMandatoryFieldsForTransferInTreatment().build();
        when(allPatients.findByPatientId(treatmentUpdateRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(treatmentUpdateRequest);
        verify(providerService).transferIn(treatmentUpdateRequest.getProvider_id(), patient, treatmentUpdateRequest.getTb_id(), treatmentUpdateRequest.getDate_modified());
    }

    protected void expectWHPDomainException(String message) {
        exceptionThrown.expect(WHPDomainException.class);
        exceptionThrown.expectMessage(new Contains(message));
    }
}
