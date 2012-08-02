package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.Phase;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.joda.time.DateTime.now;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

public class AdherenceUpdateTestPart extends PhaseUpdateOrchestratorTestPart {

    PatientStub patientStub;
    @Mock
    protected PatientService patientService;


    @Before
    public void setUp() {
        super.setUp();
        initMocks(this);
        treatmentUpdateOrchestrator = new TreatmentUpdateOrchestrator(patientService, whpAdherenceService);

    }

    @Test
    public void shouldRecomputePillCountAfterCapturingWeeklyAdherence() {
        patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);


        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordWeeklyAdherence(PATIENT_ID, adherence, auditParams);

        int dosesTaken = 0;
        LocalDate endDate = new LocalDate();
        verify(patientService, times(2)).updatePillTakenCount(patient, Phase.IP, dosesTaken, endDate);

    }

    @Test
    public void shouldRecomputePillCountAfterCapturingDailyAdherence() {
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);


        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest(2, 8, 2012, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.WEB, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(asList(dailyAdherenceRequest), patientStub, auditParams);

        int dosesTaken = 0;
        LocalDate endDate = new LocalDate();
        verify(patientService, times(2)).updatePillTakenCount(patientStub, Phase.IP, dosesTaken, endDate);
    }

    @Test
    public void shouldAttemptPhaseTransitionAfterCapturingDailyAdherence() {

        patientStub = new PatientStub();
        patientStub.startTherapy(new LocalDate(2011, 7, 1));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);
        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest(20, 7, 2011, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(asList(dailyAdherenceRequest), patientStub, auditParams);

        verify(patientService).startNextPhase(patientStub);
    }

    @Test
    public void shouldAttemptPhaseTransitionAfterCapturingWeeklyAdherence() {

        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patientStub)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordWeeklyAdherence(PATIENT_ID, adherence, auditParams);

        verify(patientService).startNextPhase(patientStub);

    }

    @Test
    public void shouldSetLastAdherenceProvidedWeekStartDateToPatientOnCapturingWeeklyAdherence() {
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patientStub)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordWeeklyAdherence(PATIENT_ID, adherence, auditParams);

        assertThat(patientStub.getLastAdherenceWeekStartDate(), is(currentWeekInstance().startDate()));
        verify(patientService).update(patientStub);

    }

    @Test
    public void shouldSetLastAdherenceProvidedWeekStartDateToPatientOnCapturingDailyAdherence() {
        patientStub = new PatientStub();
        patientStub.startTherapy(new LocalDate(2012, 7, 1));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);
        DailyAdherenceRequest dailyAdherenceRequest1 = new DailyAdherenceRequest(9, 5, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest2 = new DailyAdherenceRequest(12, 5, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest3 = new DailyAdherenceRequest(17, 5, 2012, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");

        treatmentUpdateOrchestrator.recordDailyAdherence(asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3), patientStub, auditParams);

        assertThat(patientStub.getLastAdherenceWeekStartDate(), is(new LocalDate(2012, 5, 14)));
        verify(patientService).update(patientStub);

    }

    class PatientStub extends Patient {

        public PatientStub() {
            this.setPatientId(PATIENT_ID);
            this.setFirstName("firstName");
            this.setLastName("lastName");
            this.setGender(Gender.O);
            this.setPhoneNumber("1234567890");
            this.addTreatment(new PatientBuilder().defaultTreatment(), new PatientBuilder().defaultTherapy(), now());
        }


        @Override
        public boolean isTransitioning() {
            return true;
        }

        @Override
        public boolean isOrHasBeenOnCp() {
            return false;
        }

        @Override
        public boolean hasPhaseToTransitionTo() {
            return true;
        }

        @Override
        public int getRemainingDosesInLastCompletedPhase() {
            return 0;
        }


    }


}
