package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;


import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.Phase;

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
        initMocks(this);
        treatmentUpdateOrchestrator = new TreatmentUpdateOrchestrator(patientService, whpAdherenceService);

    }

    @Test
    public void shouldRecomputePillCountAfterCapturingAdherence() {
        patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);


        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordAdherence(PATIENT_ID, adherence, auditParams);

        int dosesTaken =0;
        LocalDate endDate = new LocalDate();
        verify(patientService,times(2)).updatePillTakenCount(patient, Phase.IP, dosesTaken, endDate);

    }

    @Test
    public void shouldAttemptPhaseTransitionAfterCapturingAdherence() {

        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patientStub)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordAdherence(PATIENT_ID, adherence, auditParams);

        verify(patientService).startNextPhase(patientStub);

    }

    @Test
    public void shouldSetLastAdherenceProvidedWeekStartDateToPatientOnCaptureAdherence() {
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patientStub)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordAdherence(PATIENT_ID, adherence, auditParams);

        assertThat(patientStub.getLastAdherenceWeekStartDate(),is(currentWeekInstance().startDate()));
        verify(patientService).update(patientStub);

    }

    class PatientStub extends Patient{

        public   PatientStub(){
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
