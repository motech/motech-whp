package org.motechproject.whp.adherence.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.repository.AllAdherenceLogs;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.mapping.AdherenceMapper;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

public class WHPAdherenceServiceTest {

    public static final String THERAPY_UID = "therapyUid";
    @Mock
    AdherenceLogService adherenceLogService;
    @Mock
    AllPatients allPatients;
    @Mock
    AllAdherenceLogs allAdherenceLogs;
    @Mock
    private AdherenceAuditService adherenceAuditService;
    @Mock
    private PatientService patientService;

    private WHPAdherenceService whpAdherenceService;

    @Before
    public void setup() {
        initMocks(this);
        whpAdherenceService = new WHPAdherenceService(adherenceLogService, adherenceAuditService, allAdherenceLogs);
    }

    public DailyAdherenceRequest createDailyAdherenceRequest(int day, int month, int year, int pillStatus) {
        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest();
        dailyAdherenceRequest.setDay(day);
        dailyAdherenceRequest.setMonth(month);
        dailyAdherenceRequest.setYear(year);
        dailyAdherenceRequest.setPillStatus(pillStatus);
        return dailyAdherenceRequest;
    }

    @Test
    public void shouldSaveLogs() {
        Patient patient = new PatientBuilder().withDefaults().build();

        Treatment treatment0 = new TreatmentBuilder().withDefaults().withProviderId("provider0").withTbId("tb0").build();
        patient.addTreatment(treatment0, datetime(1, 10, 2011));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, datetime(1, 12, 2011));

        Treatment treatment1 = new TreatmentBuilder().withDefaults().withProviderId("provider1").withTbId("tb1").build();
        patient.addTreatment(treatment1, datetime(1, 1, 2012));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, datetime(1, 2, 2012));

        Treatment treatment2 = new TreatmentBuilder().withDefaults().withProviderId("provider2").withTbId("tb2").build();
        patient.addTreatment(treatment2, datetime(15, 2, 2012));

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(PATIENT_ID);
        DailyAdherenceRequest dailyAdherenceRequest1 = createDailyAdherenceRequest(1, 10, 2011, PillStatus.NotTaken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest2 = createDailyAdherenceRequest(3, 1, 2012, PillStatus.Taken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest3 = createDailyAdherenceRequest(15, 2, 2012, PillStatus.Unknown.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3));

        whpAdherenceService.recordDailyAdherence(request.getDailyAdherenceRequests(), patient, null);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(adherenceLogService, times(1)).addOrUpdateLogsByDoseDate(argumentCaptor.capture(), eq(patient.getPatientId()));

        List dataToBeStored = argumentCaptor.getValue();
        assertEquals(3, dataToBeStored.size());

        assertLog((AdherenceRecord) dataToBeStored.get(0), PATIENT_ID, date(1, 10, 2011), THERAPY_UID, "provider0", "tb0");
        assertLog((AdherenceRecord) dataToBeStored.get(1), PATIENT_ID, date(3, 1, 2012), THERAPY_UID, "provider1", "tb1");
        assertLog((AdherenceRecord) dataToBeStored.get(2), PATIENT_ID, date(15, 2, 2012), THERAPY_UID, "provider2", "tb2");
    }

    @Test
    public void shouldSaveDailyAdherenceAuditLogs() {
        Patient patient = new PatientBuilder().withDefaults().build();

        Treatment treatment0 = new TreatmentBuilder().withDefaults().withProviderId("provider0").withTbId("tb0").build();
        patient.addTreatment(treatment0, datetime(1, 10, 2011));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, datetime(1, 12, 2011));

        Treatment treatment1 = new TreatmentBuilder().withDefaults().withProviderId("provider1").withTbId("tb1").build();
        patient.addTreatment(treatment1, datetime(1, 1, 2012));
        patient.closeCurrentTreatment(TreatmentOutcome.Defaulted, datetime(1, 2, 2012));

        Treatment treatment2 = new TreatmentBuilder().withDefaults().withProviderId("provider2").withTbId("tb2").build();
        patient.addTreatment(treatment2, datetime(15, 2, 2012));

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(PATIENT_ID);
        DailyAdherenceRequest dailyAdherenceRequest1 = createDailyAdherenceRequest(1, 10, 2011, PillStatus.NotTaken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest2 = createDailyAdherenceRequest(3, 1, 2012, PillStatus.Taken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest3 = createDailyAdherenceRequest(15, 2, 2012, PillStatus.Unknown.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3));

        AuditParams auditParams = new AuditParams("user", AdherenceSource.WEB, "");

        whpAdherenceService.recordDailyAdherence(request.getDailyAdherenceRequests(), patient, auditParams);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(adherenceAuditService, times(1)).auditDailyAdherence(eq(patient), argumentCaptor.capture(), eq(auditParams));

        List dataToBeStored = argumentCaptor.getValue();
        assertEquals(3, dataToBeStored.size());

        assertLog((Adherence) dataToBeStored.get(0), PATIENT_ID, date(1, 10, 2011), THERAPY_UID, "provider0", "tb0");
        assertLog((Adherence) dataToBeStored.get(1), PATIENT_ID, date(3, 1, 2012), THERAPY_UID, "provider1", "tb1");
        assertLog((Adherence) dataToBeStored.get(2), PATIENT_ID, date(15, 2, 2012), THERAPY_UID, "provider2", "tb2");
    }

    @Test
    public void shouldSaveLogWithTbIdAndProviderAsUnknownIfDoseDateDoesNotBelongToAnyTreatment() {
        String patientId = "patientId";
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId).build();
        patient.getCurrentTreatment().setStartDate(new LocalDate(2010, 11, 1));

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));

        whpAdherenceService.recordDailyAdherence(request.getDailyAdherenceRequests(), patient, null);
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(adherenceLogService, times(1)).addOrUpdateLogsByDoseDate(argumentCaptor.capture(), eq(patient.getPatientId()));

        List<AdherenceRecord> dataToBeStored = argumentCaptor.getValue();
        assertEquals(1, dataToBeStored.size());
        Adherence adherence = new AdherenceMapper().map(dataToBeStored.get(0));
        assertEquals(WHPConstants.UNKNOWN, adherence.getProviderId());
        assertEquals(WHPConstants.UNKNOWN, adherence.getTbId());
    }

    @Test
    public void saveLogShouldStoreExternalIdInLowercase() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.getCurrentTreatment().setStartDate(new LocalDate(2010, 9, 1));

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(PATIENT_ID);
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);

        whpAdherenceService.recordDailyAdherence(request.getDailyAdherenceRequests(), patient, null);
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(adherenceLogService, times(1)).addOrUpdateLogsByDoseDate(argumentCaptor.capture(), eq(patient.getPatientId()));

        assertEquals(1, argumentCaptor.getValue().size());
        Adherence adherence = new AdherenceMapper().map((AdherenceRecord) argumentCaptor.getValue().get(0));
        assertEquals("patientid", adherence.getPatientId());
    }

    @Test
    public void shouldReturnZeroAsTheNumberOfDosesTakenIfEndDateIsBeforeStartDate() {
        int result = whpAdherenceService.countOfDosesTakenBetween(PATIENT_ID, THERAPY_UID, today(), today().minusDays(1));
        assertEquals(0, result);
        verifyZeroInteractions(adherenceLogService);
    }

    @Test
    public void shouldReturnEmptyListOfAdherenceRecordsIfEndDateIsBeforeStartDate() {
        AdherenceList result = whpAdherenceService.findLogsInRange(PATIENT_ID, THERAPY_UID, today(), today().minusDays(1));
        assertEquals(0, result.size());
        verifyZeroInteractions(adherenceLogService);
    }

    @Test
    public void shouldReturnEmptyListOfAdherenceRecordsIfStartDateIsNull() {
        AdherenceList result = whpAdherenceService.findLogsInRange(PATIENT_ID, THERAPY_UID, null, today().minusDays(1));
        assertEquals(0, result.size());
        verifyZeroInteractions(adherenceLogService);
    }

    private void assertLog(AdherenceRecord adherenceRecord, String patientId, LocalDate doseDate, String therapyUid, String providerId, String tbId) {
        Adherence adherence = new AdherenceMapper().map(adherenceRecord);

        assertLog(adherence, patientId, doseDate, therapyUid, providerId, tbId);
    }

    private void assertLog(Adherence adherence, String patientId, LocalDate doseDate, String therapyUid, String providerId, String tbId) {
        assertEquals(therapyUid, adherence.getTreatmentId());
        assertEquals(tbId, adherence.getTbId());
        assertEquals(providerId, adherence.getProviderId());
        assertEquals(doseDate, adherence.getPillDate());
        assertEquals(patientId, adherence.getPatientId());
    }

    private LocalDate date(int dayOfMonth, int monthOfYear, int year) {
        return new LocalDate(year, monthOfYear, dayOfMonth);
    }

    private DateTime datetime(int dayOfMonth, int monthOfYear, int year) {
        return new LocalDate(year, monthOfYear, dayOfMonth).toDateTimeAtCurrentTime();
    }

}
