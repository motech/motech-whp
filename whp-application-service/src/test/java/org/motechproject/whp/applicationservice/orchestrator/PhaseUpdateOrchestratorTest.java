package org.motechproject.whp.applicationservice.orchestrator;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseName;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhaseUpdateOrchestratorTest {

    public static final String THERAPY_DOC_ID = "therapyDocId";

    @Mock
    private AllPatients allPatients;
    @Mock
    private WHPAdherenceService whpAdherenceService;
    @Mock
    private PatientService patientService;

    private PhaseUpdateOrchestrator phaseUpdateOrchestrator;
    private Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
        phaseUpdateOrchestrator = new PhaseUpdateOrchestrator(allPatients, patientService, whpAdherenceService);
        patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().getTherapy().setId(THERAPY_DOC_ID);

    }

    @Test
    public void shouldCallAdherenceServiceToFetchPillCount() {
        LocalDate today = DateUtil.today();
        LocalDate startDate = today.minusDays(1);
        patient.startTherapy(startDate);
        int numberOfDosesTakenInIP = 10;

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), THERAPY_DOC_ID, startDate, today)).thenReturn(numberOfDosesTakenInIP);

        phaseUpdateOrchestrator.recomputePillCount(patient.getPatientId());

        verify(patientService).updatePillTakenCount(patient, PhaseName.IP, numberOfDosesTakenInIP);
        verify(whpAdherenceService).countOfDosesTakenBetween(patient.getPatientId(), THERAPY_DOC_ID, startDate, today);

        verifyNoMoreInteractions(whpAdherenceService);
        verifyNoMoreInteractions(patientService);
    }

    @Test
    public void shouldOnlySetNextPhaseWhenPatientIsNotNearingTransition() {
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        phaseUpdateOrchestrator.setNextPhase(patient.getPatientId(), PhaseName.EIP);

        verify(patientService, times(1)).setNextPhaseName(patient.getPatientId(), PhaseName.EIP);
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService, never()).endCurrentPhase(anyString(), any(LocalDate.class));
        verify(patientService, never()).startNextPhase(anyString());
    }

    @Test
    public void shouldSetNextPhaseAndEndCurrentPhase_PatientDoseComplete() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.currentTherapy().getCurrentPhase().setNumberOfDosesTaken(24);
        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_DOC_ID, twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_DOC_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        phaseUpdateOrchestrator.setNextPhase(patient.getPatientId(), PhaseName.EIP);

        verify(patientService, times(1)).setNextPhaseName(patient.getPatientId(), PhaseName.EIP);
        verify(whpAdherenceService).nThTakenDose(patient.getPatientId(), THERAPY_DOC_ID, 24, therapyStartDate);
        verify(patientService).endCurrentPhase(patient.getPatientId(), twentyFourthDoseTakenDate);
    }

    @Test
    public void shouldSetNextPhaseAndStartNextPhase_PatientIsTransitioning() {
        patient.currentTherapy().setNextPhaseName(PhaseName.EIP);
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.currentTherapy().getCurrentPhase().setEndDate(twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        phaseUpdateOrchestrator.setNextPhase(patient.getPatientId(), PhaseName.EIP);

        verify(patientService, times(1)).setNextPhaseName(patient.getPatientId(), PhaseName.EIP);
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService, never()).endCurrentPhase(anyString(), any(LocalDate.class));
        verify(patientService).startNextPhase(patient.getPatientId());
    }

    @Test
    public void shouldOnlyEndCurrentPhaseWhenAttemptingToTransition_PatientDoseComplete_NextPhaseInfoNotSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.currentTherapy().getCurrentPhase().setNumberOfDosesTaken(24);
        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_DOC_ID, twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_DOC_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(whpAdherenceService).nThTakenDose(patient.getPatientId(), THERAPY_DOC_ID, 24, therapyStartDate);
        verify(patientService).endCurrentPhase(patient.getPatientId(), adherenceRecord.doseDate());
        verify(patientService, never()).startNextPhase(anyString());
    }

    @Test
    public void shouldStartNextPhaseWhenAttemptingToTransition_NextPhaseSet() {
        patient.currentTherapy().setNextPhaseName(PhaseName.EIP);
        patient.getCurrentTreatment().getTherapy().setId(THERAPY_DOC_ID);
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.currentTherapy().getCurrentPhase().setEndDate(twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(patientService).startNextPhase(patient.getPatientId());
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService, never()).endCurrentPhase(anyString(), any(LocalDate.class));
    }

}
