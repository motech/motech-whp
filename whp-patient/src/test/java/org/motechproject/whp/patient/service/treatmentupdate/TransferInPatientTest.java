package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.TransferInPatient;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.ProviderService;
import org.motechproject.whp.refdata.domain.DiseaseClass;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class TransferInPatientTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTreatments allTreatments;
    @Mock
    private ProviderService providerService;

    private TransferInPatient transferInPatient;
    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        transferInPatient = new TransferInPatient(allPatients, allTreatments, providerService);
    }

    @Test
    public void shouldNotTransferInPatientTreatment_OnAnyErrors() {
        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);

        PatientRequest patientRequest = new PatientRequestBuilder().withMandatoryFieldsForTransferInTreatment().build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(providerService, never()).transferIn(patientRequest.getProvider_id(),
                patient,
                patientRequest.getTb_id(),
                patientRequest.getDate_modified());
    }

    @Test
    public void shouldTransferInPatientTreatmentAndUpdatePatientAndReviveLastTreatment_IfNoErrorsFound() {
        patient.closeCurrentTreatment("Defaulted", now());
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDiseaseClass(patient.latestTreatment().getDiseaseClass())
                .withTreatmentCategory(patient.latestTreatment().getTreatmentCategory())
                .build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(providerService).transferIn(patientRequest.getProvider_id(), patient, patientRequest.getTb_id(), patientRequest.getDate_modified());
        assertNull(patient.latestTreatment().getCloseDate());
        verify(allTreatments).update(patient.latestTreatment());
    }

}
