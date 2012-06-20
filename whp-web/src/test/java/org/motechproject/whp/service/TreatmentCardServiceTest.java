package org.motechproject.whp.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.contract.DailyAdherenceRequest;
import org.motechproject.whp.contract.TreatmentCardModel;
import org.motechproject.whp.contract.UpdateAdherenceRequest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.WHPConstants;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.builder.AdherenceDataBuilder.createLog;

public class TreatmentCardServiceTest {


    @Mock
    WHPAdherenceService whpAdherenceService;

    @Mock
    AllPatients allPatients;
    TreatmentCardService treatmentCardService;

    private final String externalId = "externalid";

    @Before
    public void setup() {
        initMocks(this);
        treatmentCardService = new TreatmentCardService(allPatients, whpAdherenceService);
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatient_BasedOnCategory_AndIPStartDate() {
        LocalDate therapyStartDate = new LocalDate(2011, 2, 3);
        LocalDate therapyEndDate = new LocalDate(2011, 7, 2);
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, "1");

        String therapyDocId = patient.latestTherapy().getId();
        Adherence log1 = createLog(new LocalDate(2011, 2, 10), therapyDocId,PillStatus.Taken);
        Adherence log2 = createLog(new LocalDate(2011, 2, 15), therapyDocId,PillStatus.NotTaken);
        Adherence log3 = createLog(new LocalDate(2011, 3, 12), therapyDocId, PillStatus.Unknown);
        Adherence log4 = createLog(new LocalDate(2011, 3, 28), therapyDocId,PillStatus.Taken);
        List<Adherence> adherenceData = Arrays.asList(log1, log2, log3, log4);

        when(whpAdherenceService.findLogsInRange(externalId, therapyDocId, therapyStartDate, therapyEndDate)).thenReturn(adherenceData);

        TreatmentCardModel treatmentCardModel = treatmentCardService.getIntensivePhaseTreatmentCardModel(patient);

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        verify(whpAdherenceService, times(1)).findLogsInRange(externalId, therapyDocId, therapyStartDate, therapyEndDate);
    }

    @Test
    public void shouldBuildIPTreatmentCardTillTodayIfEndDateIsInFuture() {
        LocalDate today = LocalDate.now();
        LocalDate therapyStartDate = today.minusMonths(1);
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, "1");

        String therapyDocId = patient.latestTherapy().getId();
        Adherence log1 = createLog(today.minusDays(10), therapyDocId,PillStatus.Taken);
        List<Adherence> adherenceData = Arrays.asList(log1);

        when(whpAdherenceService.findLogsInRange(externalId, therapyDocId, therapyStartDate, today)).thenReturn(adherenceData);

        treatmentCardService.getIntensivePhaseTreatmentCardModel(patient);

        verify(whpAdherenceService, times(1)).findLogsInRange(externalId, therapyDocId, therapyStartDate, today);
    }

    @Test
    public void shouldSaveLogs() {
        String patientId = "patientid";
        Patient patient = new PatientBuilder().withPatientId(patientId).build();


        String therapydocId = "therapydocId";
        Treatment treatment0 = new TreatmentBuilder().withDefaults().withTherapyDocId(therapydocId).withProviderId("provider0").withStartDate(new LocalDate(2011,10,1)).withEndDate(new LocalDate(2011,12,1)).withTbId("tb0").build();
        Treatment treatment1 = new TreatmentBuilder().withDefaults().withTherapyDocId(therapydocId).withProviderId("provider1").withStartDate(new LocalDate(2012,1,1)).withEndDate(new LocalDate(2012,2,14)).withTbId("tb1").build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withTherapyDocId(therapydocId).withProviderId("provider2").withStartDate(new LocalDate(2012, 2, 15)).withTbId("tb2").build();

        patient.addTreatment(treatment0, DateTime.now());
        patient.addTreatment(treatment1, DateTime.now());
        patient.addTreatment(treatment2, DateTime.now());

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setTherapy(therapydocId);
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest1 = createDailyAdherenceRequest(1, 10, 2011, PillStatus.NotTaken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest2 = createDailyAdherenceRequest(3, 1, 2012, PillStatus.Taken.getStatus());
        DailyAdherenceRequest dailyAdherenceRequest3 = createDailyAdherenceRequest(15, 2, 2012, PillStatus.Unknown.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2, dailyAdherenceRequest3));


        treatmentCardService.addLogsForPatient(request, patient);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(whpAdherenceService,times(1)).addOrUpdateLogsByDoseDate(argumentCaptor.capture(), patientIdCaptor.capture());
        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());
        List<Adherence> dataToBeStored = argumentCaptor.getValue();

        assertEquals(3, dataToBeStored.size());

        assertLog(dataToBeStored.get(0), patientId, new LocalDate(2011, 10, 1), therapydocId, "provider0", "tb0");
        assertLog(dataToBeStored.get(1), patientId, new LocalDate(2012, 1, 3), therapydocId, "provider1", "tb1");
        assertLog(dataToBeStored.get(2), patientId, new LocalDate(2012, 2, 15), therapydocId, "provider2", "tb2");
    }

    private void assertLog(Adherence adherenceData, String patientId, LocalDate doseDate, String therapydocId, String providerId, String tbId) {
        assertEquals(therapydocId, adherenceData.getTreatmentId());
        assertEquals(tbId, adherenceData.getTbId());
        assertEquals(providerId, adherenceData.getProviderId());
        assertEquals(doseDate, adherenceData.getPillDate());
        assertEquals(patientId, adherenceData.getPatientId());
    }

    @Test
    public void shouldSaveLogWithTbIdAndProviderAsUnknownIfDoseDateDoesNotBelongToAnyTreatment() {
        String patientId = "patientId";
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId).build();
        String therapyDocId = "therapyDocId";
        patient.latestTherapy().setId(therapyDocId);
        patient.getCurrentTreatment().setTherapyDocId(therapyDocId);
        patient.getCurrentTreatment().setStartDate(new LocalDate(2010,11,1));

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));

        treatmentCardService.addLogsForPatient(request, patient);
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(whpAdherenceService,times(1)).addOrUpdateLogsByDoseDate(argumentCaptor.capture(), patientIdCaptor.capture());

        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());
        List<Adherence> dataToBeStored = argumentCaptor.getValue();
        assertEquals(1,dataToBeStored.size());
        assertEquals(WHPConstants.UNKNOWN,dataToBeStored.get(0).getProviderId());
        assertEquals(WHPConstants.UNKNOWN,dataToBeStored.get(0).getTbId());
    }

    @Test
    public void saveLogShouldStoreExternalIdInLowercase() {
        String patientId = "patientId";
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId).build();
        String therapyDocId = "therapyDocId";
        patient.latestTherapy().setId(therapyDocId);
        patient.getCurrentTreatment().setTherapyDocId(therapyDocId);
        patient.getCurrentTreatment().setStartDate(new LocalDate(2010,9,1));

        UpdateAdherenceRequest request = new UpdateAdherenceRequest();
        request.setPatientId(patientId);
        request.setTherapy(patient.latestTherapy().getId());
        DailyAdherenceRequest dailyAdherenceRequest = createDailyAdherenceRequest(1, 10, 2010, PillStatus.NotTaken.getStatus());
        request.setDailyAdherenceRequests(asList(dailyAdherenceRequest));
        when(allPatients.findByPatientId(patientId)).thenReturn(patient);

        treatmentCardService.addLogsForPatient(request, treatmentCardService.allPatients.findByPatientId(request.getPatientId()));
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> patientIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(whpAdherenceService,times(1)).addOrUpdateLogsByDoseDate(argumentCaptor.capture(), patientIdCaptor.capture());
        List<Adherence> dataToBeStored = argumentCaptor.getValue();

        assertEquals(patient.getPatientId(),patientIdCaptor.getValue());

        assertEquals(1,dataToBeStored.size());
        assertEquals("patientid",dataToBeStored.get(0).getPatientId());
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
