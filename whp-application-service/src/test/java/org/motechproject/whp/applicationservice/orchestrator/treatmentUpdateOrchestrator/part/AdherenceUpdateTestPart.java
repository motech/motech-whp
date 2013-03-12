package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.mapping.AdherenceListMapper;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.adherence.request.DailyAdherenceRequests;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionRequestBuilder;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;

import java.util.ArrayList;
import java.util.Date;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.adherence.criteria.TherapyStartCriteria.shouldStartOrRestartTreatment;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.patient.builder.PatientBuilder.PROVIDER_ID;

public class AdherenceUpdateTestPart extends TreatmentUpdateOrchestratorTestPart {

    PatientStub patientStub;
    @Mock
    protected PatientService patientService;

    @Mock
    private ReportingPublisherService reportingPublisherService;
    private DailyAdherenceRequests EMPTY_DAILY_ADHERENCE_REQUESTS;

    @Before
    public void setUp() {
        super.setUp();
        initMocks(this);
        treatmentUpdateOrchestrator = new TreatmentUpdateOrchestrator(patientService, whpAdherenceService, reportingPublisherService);

        EMPTY_DAILY_ADHERENCE_REQUESTS = new DailyAdherenceRequests(new ArrayList<DailyAdherenceRequest>());
    }

    @Test
    public void shouldRecomputePillCountAfterCapturingWeeklyAdherence_onWeekdays() {
        LocalDate tuesday = new LocalDate(2012, 8, 7);
        mockCurrentDate(tuesday);
        patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);


        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordWeeklyAdherence(adherence, patient.getPatientId(), auditParams);

        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.IP));
    }

    @Test
    public void shouldRecomputePillCountAfterCapturingWeeklyAdherence_onSunday() {
        LocalDate sunday = new LocalDate(2012, 8, 5);
        mockCurrentDate(sunday);
        patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patient)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordWeeklyAdherence(adherence, patient.getPatientId(), auditParams);

        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.IP));
    }

    @Test
    public void shouldReportAdherenceSubmissionAfterCapturingDailyAdherence() {
        DateTime tuesday = new DateTime(2012, 8, 7, 0, 0);
        mockCurrentDate(tuesday);
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        DailyAdherenceRequest dailyAdherenceRequest1 = new DailyAdherenceRequest(7, 8, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest2 = new DailyAdherenceRequest(1, 8, 2012, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.WEB, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(new DailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2)), patientStub, auditParams);

        AdherenceSubmissionRequest expectedRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(PROVIDER_ID, auditParams.getUser(), tuesday, dailyAdherenceRequest1.getDoseDate());
        verify(reportingPublisherService).reportAdherenceSubmission(expectedRequest);
    }

    @Test
    public void shouldRecomputePillCountAfterCapturingDailyAdherence_onWeekdays() {
        LocalDate tuesday = new LocalDate(2012, 8, 7);
        mockCurrentDate(tuesday);
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest(2, 8, 2012, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.WEB, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(new DailyAdherenceRequests(asList(dailyAdherenceRequest)), patientStub, auditParams);

        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.IP));
    }

    @Test
    public void shouldRecomputePillCountAfterCapturingDailyAdherence_onSunday() {
        LocalDate tuesday = new LocalDate(2012, 8, 5);
        mockCurrentDate(tuesday);
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest(2, 8, 2012, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.WEB, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(new DailyAdherenceRequests(asList(dailyAdherenceRequest)), patientStub, auditParams);

        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.IP));
    }

    @Test
    public void shouldRecordWeeklyAdherence() {
        DateTime validAdherenceWindowDateTime = DateTime.now().withDayOfWeek(DayOfWeek.Sunday.getValue());
        mockCurrentDate(validAdherenceWindowDateTime);
        String providerId = "provider";
        patient = new PatientBuilder().withDefaults().withProviderId(providerId).build();
        patient.startTherapy(today().minusMonths(2));

        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary();
        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");

        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patient);
        when(whpAdherenceService.currentWeekAdherence(patient)).thenReturn(weeklyAdherenceSummary);

        treatmentUpdateOrchestrator.recordWeeklyAdherence(weeklyAdherenceSummary , patient.getPatientId() , auditParams);
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        verify(whpAdherenceService).recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);

        AdherenceSubmissionRequest expectedAdherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequestByProvider(providerId, validAdherenceWindowDateTime);

        verify(reportingPublisherService).reportAdherenceSubmission(expectedAdherenceSubmissionRequest);
    }

    private AdherenceSubmissionRequest createAdherenceSubmissionRequest(String providerId, String submittedBy, Date submissionDate) {
        AdherenceSubmissionRequest expectedAdherenceSubmissionRequest = new AdherenceSubmissionRequest();
        expectedAdherenceSubmissionRequest.setProviderId(providerId);
        expectedAdherenceSubmissionRequest.setWithinAdherenceWindow(true);
        expectedAdherenceSubmissionRequest.setSubmissionDate(submissionDate);
        expectedAdherenceSubmissionRequest.setSubmittedBy(submittedBy);
        return expectedAdherenceSubmissionRequest;
    }

    @Test
    public void shouldNotGenerateAuditLogs_forEmptyDailyAdherence(){
        patientStub = new PatientStub();
        patientStub.startTherapy(new LocalDate(2011, 7, 1));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(EMPTY_DAILY_ADHERENCE_REQUESTS, patientStub, auditParams);

        verifyNoMoreInteractions(whpAdherenceService);
        verifyNoMoreInteractions(reportingPublisherService);
    }

    @Test
    public void shouldNotUpdatePatient_forEmptyDailyAdherence(){
        patientStub = new PatientStub();
        patientStub.startTherapy(new LocalDate(2011, 7, 1));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordDailyAdherence(EMPTY_DAILY_ADHERENCE_REQUESTS, patientStub, auditParams);

        verifyNoMoreInteractions(patientService);
    }

    @Test
    public void shouldAttemptPhaseTransitionAfterCapturingDailyAdherence() {

        patientStub = new PatientStub();
        patientStub.startTherapy(new LocalDate(2011, 7, 1));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);
        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest(20, 7, 2011, 1);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        PhaseRecord previousPhase = patient.getCurrentPhase();
        treatmentUpdateOrchestrator.recordDailyAdherence(new DailyAdherenceRequests(asList(dailyAdherenceRequest)), patientStub, auditParams);

        assertNotNull(patient.getCurrentPhase());
        assertTrue(previousPhase.equals(patient.getCurrentPhase()));
    }

    @Test
    public void shouldAttemptPhaseTransitionAfterCapturingWeeklyAdherence() {

        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patientStub)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        PhaseRecord previousPhase = patient.getCurrentPhase();
        treatmentUpdateOrchestrator.recordWeeklyAdherence(adherence, patientStub.getPatientId(), auditParams);

        assertNotNull(patient.getCurrentPhase());
        assertTrue(previousPhase.equals(patient.getCurrentPhase()));
    }

    @Test
    public void shouldSetLastAdherenceProvidedWeekStartDateToPatientOnCapturingWeeklyAdherence() {
        patientStub = new PatientStub();
        patientStub.startTherapy(today().minusMonths(2));
        when(patientService.findByPatientId(PATIENT_ID)).thenReturn(patientStub);

        WeeklyAdherenceSummary adherence = new WeeklyAdherenceSummary();
        when(whpAdherenceService.currentWeekAdherence(patientStub)).thenReturn(adherence);

        AuditParams auditParams = new AuditParams("admin", AdherenceSource.IVR, "test");
        treatmentUpdateOrchestrator.recordWeeklyAdherence(adherence, patientStub.getPatientId(), auditParams);

        assertThat(patientStub.getLastAdherenceWeekStartDate(), is(currentAdherenceCaptureWeek().startDate()));
        assertEquals(adherence.getWeek().startDate(), patientStub.getLastAdherenceWeekStartDate());
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

        DailyAdherenceRequests dailyAdherenceRequests = new DailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3));
        treatmentUpdateOrchestrator.recordDailyAdherence(dailyAdherenceRequests, patientStub, auditParams);

        assertThat(patientStub.getLastAdherenceWeekStartDate(), is(new LocalDate(2012, 5, 14)));
        TreatmentWeek treatmentWeek = new TreatmentWeek(dailyAdherenceRequest3.getDoseDate());
        assertEquals(treatmentWeek.startDate(), patientStub.getLastAdherenceWeekStartDate());
    }

    class PatientStub extends Patient {

        public PatientStub() {
            this.setPatientId(PATIENT_ID);
            this.setFirstName("firstName");
            this.setLastName("lastName");
            this.setGender(Gender.O);
            this.setPhoneNumber("1234567890");
            this.addTreatment(new PatientBuilder().defaultTreatment(), new PatientBuilder().defaultTherapy(), now(), now());
        }

        @Override
        public void startNextPhase() {
            return;
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
