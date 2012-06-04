package org.motechproject.whp.patient.service.treatmentupdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.TransferInPatient;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.TreatmentService;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class TransferInPatientTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private AllTherapies allTherapies;
    @Mock
    private TreatmentService treatmentService;

    private TransferInPatient transferInPatient;

    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        transferInPatient = new TransferInPatient(allPatients, allTherapies, treatmentService);
    }

    @Test
    public void shouldNotTransferInPatientIfCurrentTreatmentIsNotClosed() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.latestTherapy().getDiseaseClass());
        patientRequest.setTreatment_category(patient.latestTherapy().getTreatmentCategory());
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_NOT_CLOSED);
        transferInPatient.apply(patientRequest);
        verify(treatmentService, never()).transferInPatient(patientRequest);
    }

    @Test
    public void shouldNotTransferInPatientIfDiseaseClassInRequestDoesNotMatchCurrentTreatmentDiseaseClass() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(DiseaseClass.E);
        patientRequest.setTreatment_category(patient.latestTherapy().getTreatmentCategory());
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_DETAILS_DO_NOT_MATCH);
        transferInPatient.apply(patientRequest);
        verify(treatmentService, never()).transferInPatient(patientRequest);
    }

    @Test
    public void shouldTransferInPatientTreatmentAndUpdatePatientAndReviveLastTreatment_IfNoErrorsFound() {
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, now());
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDiseaseClass(patient.latestTherapy().getDiseaseClass())
                .withTreatmentCategory(patient.latestTherapy().getTreatmentCategory())
                .build();

        transferInPatient.apply(patientRequest);
        assertNull(patient.latestTherapy().getCloseDate());
        verify(treatmentService).transferInPatient(patientRequest);
    }

    @Test
    public void shouldNotTransferInPatientIfTreatmentCategoryInRequestDoesNotMatchCurrentTreatmentTreatmentCategory() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.latestTherapy().getDiseaseClass());
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        patientRequest.setTreatment_category(new TreatmentCategory("Some Random Category", "11", 3, 8, 18, 24, 54, threeDaysAWeek));
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TREATMENT_DETAILS_DO_NOT_MATCH);
        transferInPatient.apply(patientRequest);
        verify(treatmentService, never()).transferInPatient(patientRequest);
    }

    @Test
    public void shouldTransferInPatientIfPatientTbIdMatchesRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.latestTherapy().getDiseaseClass());
        patientRequest.setTreatment_category(patient.latestTherapy().getTreatmentCategory());
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(treatmentService).transferInPatient(patientRequest);
    }
}
