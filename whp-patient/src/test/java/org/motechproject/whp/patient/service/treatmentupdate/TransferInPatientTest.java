package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.TransferInPatient;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.TreatmentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class TransferInPatientTest extends BaseUnitTest {

    @Mock
    private PatientService patientService;
    @Mock
    private TreatmentService treatmentService;

    private TransferInPatient transferInPatient;

    @Before
    public void setUp() {
        initMocks(this);
        transferInPatient = new TransferInPatient(patientService, treatmentService);
    }

    @Test
    public void shouldNotTransferInPatientIfCurrentTreatmentIsNotClosed() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.getCurrentTherapy().getDiseaseClass());
        patientRequest.setTreatment_category(patient.getCurrentTherapy().getTreatmentCategory());
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);
        transferInPatient.apply(patientRequest);
        verify(treatmentService, never()).transferInPatient(patientRequest);
    }

    @Test
    public void shouldTransferInPatientTreatmentAndUpdatePatientAndReviveLastTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, null, now());

        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDiseaseClass(patient.getCurrentTherapy().getDiseaseClass())
                .withTreatmentCategory(patient.getCurrentTherapy().getTreatmentCategory())
                .build();
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(treatmentService).transferInPatient(patientRequest);
    }

    @Test
    public void shouldNotTransferInPatientIfTreatmentCategoryInRequestDoesNotMatchCurrentTreatmentTreatmentCategory() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.getCurrentTherapy().getDiseaseClass());
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        patientRequest.setTreatment_category(new TreatmentCategory("Some Random Category", "11", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, null, now());
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_DETAILS_DO_NOT_MATCH);
        transferInPatient.apply(patientRequest);
        verify(treatmentService, never()).transferInPatient(patientRequest);
    }

    @Test
    public void shouldTransferInPatientIfPatientTbIdMatchesRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.getCurrentTherapy().getDiseaseClass());
        patientRequest.setTreatment_category(patient.getCurrentTherapy().getTreatmentCategory());
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, null, now());
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(treatmentService).transferInPatient(patientRequest);
    }
}
