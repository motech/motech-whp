package org.motechproject.whp.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.contract.DailyAdherenceRequest;
import org.motechproject.whp.contract.TreatmentCardModel;
import org.motechproject.whp.contract.UpdateAdherenceRequest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.builder.AdherenceDataBuilder.createLog;

public class TreatmentCardServiceTest {

    @Mock
    AllAdherenceLogs allAdherenceLogs;

    @Mock
    AdherenceService adherenceService;

    @Mock
    AllPatients allPatients;
    TreatmentCardService treatmentCardService;

    private final String externalId = "externalid";

    @Before
    public void setup() {
        initMocks(this);
        treatmentCardService = new TreatmentCardService(allAdherenceLogs,adherenceService,allPatients);
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatient_BasedOnCategory_AndIPStartDate() {
        LocalDate therapyStartDate = new LocalDate(2012, 2, 3);
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, "1");

        String therapyDocId = patient.latestTherapy().getId();
        AdherenceData log1 = createLog(new LocalDate(2012, 2, 10), therapyDocId,PillStatus.Taken);
        AdherenceData log2 = createLog(new LocalDate(2012, 2, 15), therapyDocId,PillStatus.NotTaken);
        AdherenceData log3 = createLog(new LocalDate(2012, 3, 12), therapyDocId, PillStatus.Unknown);
        AdherenceData log4 = createLog(new LocalDate(2012, 3, 28), therapyDocId,PillStatus.Taken);
        List<AdherenceData> adherenceData = Arrays.asList(log1, log2, log3, log4);

        when(allAdherenceLogs.findLogsInRange(externalId, therapyDocId, therapyStartDate, therapyStartDate.plusMonths(5))).thenReturn(adherenceData);

        TreatmentCardModel treatmentCardModel = treatmentCardService.getIntensivePhaseTreatmentCardModel(patient);

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        verify(allAdherenceLogs, times(1)).findLogsInRange(externalId, therapyDocId, therapyStartDate, therapyStartDate.plusMonths(5));
    }

    @Test
    public void shouldSaveLogs() {
        String patientId = "patientid";
        Patient patient = new PatientBuilder().withPatientId(patientId).build();


        Treatment treatment0 = new TreatmentBuilder().withDefaults().withStartDate(new LocalDate(2011,10,1)).withEndDate(new LocalDate(2011,12,1)).withTherapyDocId("treatment0").build();
        Treatment treatment1 = new TreatmentBuilder().withDefaults().withStartDate(new LocalDate(2012,1,1)).withEndDate(new LocalDate(2012,2,14)).withTherapyDocId("treatment1").build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withStartDate(new LocalDate(2012, 2, 15)).withTherapyDocId("treatment2").build();

        patient.addTreatment(treatment0, DateTime.now());
        patient.addTreatment(treatment1, DateTime.now());
        patient.addTreatment(treatment2, DateTime.now());

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest1 = createDailyAdherenceRequest(1, 10, 2011, PillStatus.NotTaken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest2 = createDailyAdherenceRequest(3, 1, 2012, PillStatus.Taken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest3 = createDailyAdherenceRequest(15, 2, 2012, PillStatus.Unknown.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3));


        treatmentCardService.addLogsForPatient(request, "admin", patient);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(adherenceService,times(1)).addOrUpdateLogsByDosedate(argumentCaptor.capture(),patientIdCaptor.capture());
        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());
        List<AdherenceData> dataToBeStored = argumentCaptor.getValue();

        assertEquals(3, dataToBeStored.size());

        assertEquals("treatment0", dataToBeStored.get(0).treatmentId());
        assertEquals(new LocalDate(2011, 10,1), dataToBeStored.get(0).doseDate());
        assertEquals(patientId,dataToBeStored.get(0).externalId());

        assertEquals("treatment1", dataToBeStored.get(1).treatmentId());
        assertEquals(new LocalDate(2012, 1, 3), dataToBeStored.get(1).doseDate());
        assertEquals(patientId,dataToBeStored.get(1).externalId());

        assertEquals("treatment2", dataToBeStored.get(2).treatmentId());
        assertEquals(new LocalDate(2012, 2, 15), dataToBeStored.get(2).doseDate());
        assertEquals(patientId,dataToBeStored.get(2).externalId());

    }

    @Test
    public void shouldSaveLogWithTbIdAsEmptyIfDoseDateDoesNotBelongToAnyTreatment() {
        String patientId = "patientId";
        Patient patient = new PatientBuilder().withPatientId(patientId).build();

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));

        treatmentCardService.addLogsForPatient(request, "admin", patient);
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(adherenceService,times(1)).addOrUpdateLogsByDosedate(argumentCaptor.capture(),patientIdCaptor.capture());

        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());
        List<AdherenceData> dataToBeStored = argumentCaptor.getValue();
        assertEquals(1,dataToBeStored.size());
        assertEquals("",dataToBeStored.get(0).treatmentId());
    }

    @Test
    public void shouldSaveLogWithUserWhoProvidesLog() {
        String patientId = "patientId";
        Patient patient = new PatientBuilder().withPatientId(patientId).build();

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));

        treatmentCardService.addLogsForPatient(request, "admin", patient);
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(adherenceService,times(1)).addOrUpdateLogsByDosedate(argumentCaptor.capture(),patientIdCaptor.capture());

        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());
        List<AdherenceData> dataToBeStored = argumentCaptor.getValue();

        assertEquals(1,dataToBeStored.size());
        assertEquals("admin",dataToBeStored.get(0).meta().get(AdherenceConstants.PROVIDER_ID));
    }

    @Test
    public void saveLogShouldStoreExternalIdInLowercase() {
        String patientId = "patientId";
        Patient patient = new PatientBuilder().withPatientId(patientId).build();

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));
        when(allPatients.findByPatientId(patientId)).thenReturn(patient);

        treatmentCardService.addLogsForPatient(request, "admin", treatmentCardService.allPatients.findByPatientId(request.getPatientId()));
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(adherenceService,times(1)).addOrUpdateLogsByDosedate(argumentCaptor.capture(),patientIdCaptor.capture());
        List<AdherenceData> dataToBeStored = argumentCaptor.getValue();

        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());

        assertEquals(1,dataToBeStored.size());
        assertEquals("patientid",dataToBeStored.get(0).externalId());
    }


    private Patient createPatientOn3DayAWeekTreatmentCategory(String externalId, LocalDate therapyStartDate, String therapyDocId) {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        patient.startTherapy(therapyStartDate);
        patient.latestTherapy().setId(therapyDocId);
        patient.getCurrentTreatment().setTherapyDocId("1");
        //patient.getCurrentTreatment().getTherapy().setId("1");
        patient.getCurrentTreatment().setStartDate(therapyStartDate);
        return patient;
    }

    public DailyAdherenceRequest createDailyAdherenceRequest(int day, int month, int year, int pillStatus) {

        DailyAdherenceRequest dailyAdherenceRequest = new DailyAdherenceRequest();
        dailyAdherenceRequest.setDay(day);
        dailyAdherenceRequest.setMonth(month);
        dailyAdherenceRequest.setYear(year);
        dailyAdherenceRequest.setPillStatus(pillStatus);
        return dailyAdherenceRequest;
    }

}
