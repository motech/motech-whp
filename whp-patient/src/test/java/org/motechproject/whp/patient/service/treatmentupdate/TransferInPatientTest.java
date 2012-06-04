package org.motechproject.whp.patient.service.treatmentupdate;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.command.TransferInPatient;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.ProviderService;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.refdata.domain.WeightInstance;

import static org.junit.Assert.assertEquals;
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
    private ProviderService providerService;

    private TransferInPatient transferInPatient;
    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        patient = new PatientBuilder().withDefaults().build();
        transferInPatient = new TransferInPatient(allPatients, allTherapies, providerService);
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
                patientRequest.getTb_registration_number(),
                patientRequest.getDate_modified(), patientRequest.getSmearTestResults(), patientRequest.getWeightStatistics());
    }

    @Test
    public void shouldTransferInPatientTreatmentAndUpdatePatientAndReviveLastTreatment_IfNoErrorsFound() {
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, now());
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDiseaseClass(patient.latestTherapy().getDiseaseClass())
                .withTreatmentCategory(patient.latestTherapy().getTreatmentCategory())
                .build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(providerService).transferIn(patientRequest.getProvider_id(),
                patient,
                patientRequest.getTb_id(),
                patientRequest.getTb_registration_number(),
                patientRequest.getDate_modified(), patientRequest.getSmearTestResults(), patientRequest.getWeightStatistics());
        assertNull(patient.latestTherapy().getCloseDate());
        verify(allTherapies).update(patient.latestTherapy());
    }

    @Test
    public void shouldCaptureNewSmearTestResultsAndWeightStatisticsIfSent() {
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, now());
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment().withSmearTestResults(SmearTestSampleInstance.PreTreatment, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive, DateUtil.newDate(2012, 5, 19), SmearTestResult.Positive)
                .withWeightStatistics(WeightInstance.PreTreatment, 30.00, DateUtil.newDate(2012, 5, 19))
                .build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(providerService).transferIn(patientRequest.getProvider_id(),
                patient,
                patientRequest.getTb_id(),
                patientRequest.getTb_registration_number(),
                patientRequest.getDate_modified(), patientRequest.getSmearTestResults(), patientRequest.getWeightStatistics());
        verify(allTherapies).update(patient.latestTherapy());
    }

    @Test
    public void shouldUpdateWithOldTreatmentSmearTestResultsAndWeightStatisticsIfNotSent() {
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, now());
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .build();
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        transferInPatient.apply(patientRequest);
        verify(providerService).transferIn(patientRequest.getProvider_id(),
                patient,
                patientRequest.getTb_id(),
                patientRequest.getTb_registration_number(),
                patientRequest.getDate_modified(), patientRequest.getSmearTestResults(), patientRequest.getWeightStatistics());

        LocalDate today = DateUtil.today();
        assertEquals(1, patient.getCurrentTreatment().getSmearTestResults().size());
        assertEquals(today, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_1());
        assertEquals(today, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_2());
        assertEquals(SmearTestResult.Negative, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_1());
        assertEquals(SmearTestResult.Negative, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_2());
        assertEquals(today, patient.getCurrentTreatment().getWeightStatistics().latestResult().getMeasuringDate());
        assertEquals(Double.valueOf(100.00), patient.getCurrentTreatment().getWeightStatistics().latestResult().getWeight());
        verify(allTherapies).update(patient.latestTherapy());
    }


}
