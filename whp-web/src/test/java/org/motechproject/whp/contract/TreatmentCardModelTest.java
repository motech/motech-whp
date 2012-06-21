package org.motechproject.whp.contract;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.uimodel.DailyAdherence;
import org.motechproject.whp.uimodel.MonthlyAdherence;
import org.motechproject.whp.uimodel.TreatmentCardModel;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.builder.AdherenceDataBuilder.createLog;

public class TreatmentCardModelTest {

    private final String externalId = "externalid";

    @Test
    public void shouldReturnExistingMonthAdherenceRequestForDatesInExistingMonth() {
        String provider = "prov_id1";

        LocalDate therapyStartDate = new LocalDate(2011, 2, 3);
        LocalDate therapyEndDate = new LocalDate(2011, 7, 2);
        String therapyDocId = "therapyDocId";
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, therapyDocId);
        patient.getCurrentTreatment().setProviderId(provider);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        Adherence log1 = createLog(new LocalDate(2011, 2, 11), provider, PillStatus.Taken);
        Adherence log2 = createLog(new LocalDate(2011, 2, 16), provider, PillStatus.NotTaken);
        Adherence log3 = createLog(new LocalDate(2011, 3, 14), provider, PillStatus.Unknown);
        Adherence log4 = createLog(new LocalDate(2011, 3, 28), provider, PillStatus.Taken);

        List<Adherence> adherenceData = asList(log1, log2, log3, log4);

        List<Integer> febLogDays = asList(4, 7, 9, 11, 14, 16, 18, 21, 23, 25, 28);
        List<Integer> febTakenDays = asList(11);
        List<Integer> febNotTakenDays = asList(16);

        List<Integer> marchLogDays = asList(2, 4, 7, 9, 11, 14, 16, 18, 21, 23, 25, 28, 30);
        List<Integer> marchTakenDays = asList(28);
        List<Integer> marchNotTakenDays = asList();

        List<Integer> aprilLogDays = asList(1, 4, 6, 8, 11, 13, 15, 18, 20, 22, 25, 27, 29);
        List<Integer> aprilTakenDays = asList();
        List<Integer> aprilNotTakenDays = asList();

        List<Integer> mayLogDays = asList(2, 4, 6, 9, 11, 13, 16, 18, 20, 23, 25, 27, 30);
        List<Integer> mayTakenDays = asList();
        List<Integer> mayNotTakenDays = asList();

        List<Integer> juneLogDays = asList(1, 3, 6, 8, 10, 13, 15, 17, 20, 22, 24, 27, 29);
        List<Integer> juneTakenDays = asList();
        List<Integer> juneNotTakenDays = asList();

        List<Integer> julyLogDays = asList(1);
        List<Integer> julyTakenDays = asList();
        List<Integer> julyNotTakenDays = asList();

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(),therapyStartDate,therapyEndDate);

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        validateMonthLog(treatmentCardModel, 2, 2011, "Feb 2011", 0, 6, 28, febLogDays, febTakenDays, febNotTakenDays, 11);
        validateMonthLog(treatmentCardModel, 3,2011,"Mar 2011", 1, 6, 31, marchLogDays, marchTakenDays, marchNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, 4,2011,"Apr 2011", 2, 3, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 13);
        validateMonthLog(treatmentCardModel,5,2011, "May 2011", 3, 1, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 13);
        validateMonthLog(treatmentCardModel,6,2011, "Jun 2011", 4, 5, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 13);
        validateMonthLog(treatmentCardModel,7,2011, "Jul 2011", 5, 3, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 1);
        assertEquals(asList(provider), treatmentCardModel.getProviderIds());
    }

    @Test
    public void shouldSetTherapyDocId() {
        String provider = "prov_id1";
        LocalDate therapyStartDate = new LocalDate(2012, 2, 3);
        String therapyDocId = "therapyDocId";
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, therapyDocId);
        patient.getCurrentTreatment().setProviderId(provider);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        Adherence log1 = createLog(new LocalDate(2012, 2, 10), provider, PillStatus.Taken);
        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, asList(log1), patient.latestTherapy(), therapyStartDate,therapyStartDate.plusMonths(5).minusDays(1));

        assertEquals(therapyDocId,treatmentCardModel.getTherapyDocId());
    }

    @Test
    public void shouldSetProviderIdFromLogIfExists() {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        String provider1 = "p1";
        String provider2 = "p2";
        String provider3 = "p3";
        String providerInLogOnly = "providerInLogOnly";
        Treatment treatment1 = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 2, 5)).withProviderId(provider1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 2, 14)).withProviderId(provider2).build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 3, 10)).withProviderId(provider3).build();
        patient.setTreatments(asList(treatment1, treatment2));
        patient.setCurrentTreatment(currentTreatment);
        LocalDate therapyStartDate = new LocalDate(2012, 2, 4);
        LocalDate therapyEndDate = new LocalDate(2012, 6, 30);
        patient.startTherapy(therapyStartDate);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        Adherence log1 = createLog(new LocalDate(2012, 2, 13), providerInLogOnly, PillStatus.Unknown);
        Adherence log2 = createLog(new LocalDate(2012, 2, 29), provider3, PillStatus.Taken);
        List<Adherence> adherenceData = Arrays.asList(log1, log2);

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(), therapyStartDate,therapyEndDate);
        List<MonthlyAdherence> monthlyAdherences = treatmentCardModel.getMonthlyAdherences();

        //'p1' is the provider from 6th Feb 2012 to 13th Feb 2012
        assertEquals(provider1, monthlyAdherences.get(0).getLogs().get(0).getProviderId());
        //'p4' is the provider for Adherence log on 13th Feb 2012
        assertEquals(providerInLogOnly, monthlyAdherences.get(0).getLogs().get(3).getProviderId());
        //'providerInLogOnly' is the provider for Adherence log on 29th Feb 2012
        assertEquals(provider3, monthlyAdherences.get(0).getLogs().get(10).getProviderId());


        assertEquals(asList(provider1,providerInLogOnly,provider2,provider3), treatmentCardModel.getProviderIds());
    }


    @Test
    public void shouldSetProviderIdForDailyAdherence() {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        String provider1 = "p1";
        String provider2 = "p2";
        String provider3 = "p3";
        String provider4 = "p4";
        Treatment treatment1 = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 2, 5)).withProviderId(provider1).build();
        Treatment treatment2 = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 2, 14)).withProviderId(provider2).build();
        Treatment treatment3 = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 2, 16)).withProviderId(provider3).build();
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(new LocalDate(2012, 3, 10)).withProviderId(provider4).build();
        patient.setTreatments(asList(treatment1, treatment2, treatment3));
        patient.setCurrentTreatment(currentTreatment);
        LocalDate therapyStartDate = new LocalDate(2012, 2, 4);
        LocalDate therapyEndDate = new LocalDate(2012, 6, 30);
        patient.startTherapy(therapyStartDate);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        Adherence log1 = createLog(new LocalDate(2012, 2, 10), provider1, PillStatus.Taken);
        Adherence log2 = createLog(new LocalDate(2012, 2, 15), provider2, PillStatus.NotTaken);
        Adherence log3 = createLog(new LocalDate(2012, 3, 12), provider4, PillStatus.Unknown);
        Adherence log4 = createLog(new LocalDate(2012, 3, 28), provider4, PillStatus.Taken);
        List<Adherence> adherenceData = Arrays.asList(log1, log2, log3, log4);

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(), therapyStartDate,therapyEndDate);
        List<MonthlyAdherence> monthlyAdherences = treatmentCardModel.getMonthlyAdherences();

        //p1 is the provider from 5th Feb 2012 to 13th Feb 2012
        //getLogs().get(3) returns Adherence log for 13th Feb 2012
        assertEquals("p1", monthlyAdherences.get(0).getLogs().get(3).getProviderId());

        //p2 is the provider from 14th Feb 2012 to 15th Feb 2012
        //getLogs().get(4) returns Adherence log for 15th Feb 2012
        assertEquals("p2", monthlyAdherences.get(0).getLogs().get(4).getProviderId());

        //p3 is the provider from 16th Feb 2012 to 9th March 2012
        //getLogs().get(5) returns Adherence log for 17th Feb 2012
        assertEquals("p3", monthlyAdherences.get(0).getLogs().get(5).getProviderId());

        //getLogs().get(5) returns Adherence log for 9th March 2012
        assertEquals("p3", monthlyAdherences.get(1).getLogs().get(3).getProviderId());

        // p4 is the provider for 10th March 2012 to date
        //getLogs().get(4) returns Adherence log for 12th March 2012
        assertEquals("p4", monthlyAdherences.get(1).getLogs().get(4).getProviderId());
        assertEquals("p4", monthlyAdherences.get(2).getLogs().get(0).getProviderId());
        assertEquals("p4", monthlyAdherences.get(3).getLogs().get(0).getProviderId());
        assertEquals("p4", monthlyAdherences.get(4).getLogs().get(0).getProviderId());

        assertEquals(asList("p1", "p2", "p3", "p4"), treatmentCardModel.getProviderIds());

    }

    @Test
    public void shouldAddMonthAdherenceForFutureWithoutDailyAdherence() {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        String provider  ="providerId";
        LocalDate today = DateUtil.today();
        LocalDate therapyStartDate = today.minusMonths(1);
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(therapyStartDate).withProviderId(provider).build();
        patient.setCurrentTreatment(currentTreatment);
        patient.startTherapy(therapyStartDate);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();
        Adherence log1 = createLog(therapyStartDate.plusDays(10), provider, PillStatus.Taken);
        Adherence log2 = createLog(today.minusDays(1), provider, PillStatus.NotTaken);
        List<Adherence> adherenceData = Arrays.asList(log1, log2);


        LocalDate endDate = today.plusMonths(5);
        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(), therapyStartDate, endDate);

        assertTrue(treatmentCardModel.getMonthlyAdherences().size()>=5);
        //verifying if monthAdherence for fifth month is created without any log
        assertEquals(0,treatmentCardModel.getMonthlyAdherences().get(4).getLogs().size());
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatientOn7DayTreatmentCategory() {
        LocalDate therapyStartDate = new LocalDate(2011, 4, 1);
        LocalDate therapyEndDate = new LocalDate(2011, 8, 31);
        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();
        Patient patient = createPatientOn7DayAWeekTreatmentCategory(externalId, therapyStartDate);

        Adherence log1 = createLog(new LocalDate(2011, 4, 10), "ext_id", PillStatus.Taken);

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, asList(log1), patient.latestTherapy(), therapyStartDate,therapyEndDate);

        List<Integer> aprilLogDays = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        List<Integer> aprilTakenDays = asList(10);
        List<Integer> aprilNotTakenDays = asList();

        List<Integer> mayLogDays = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
        List<Integer> mayTakenDays = asList();
        List<Integer> mayNotTakenDays = asList();

        List<Integer> juneLogDays = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
        List<Integer> juneTakenDays = asList();
        List<Integer> juneNotTakenDays = asList();

        List<Integer> julyLogDays = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
        List<Integer> julyTakenDays = asList();
        List<Integer> julyNotTakenDays = asList();

        List<Integer> augustLogDays = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
        List<Integer> augustTakenDays = asList();
        List<Integer> augustNotTakenDays = asList();

        assertEquals(5, treatmentCardModel.getMonthlyAdherences().size());

        validateMonthLog(treatmentCardModel, 4,2011,"Apr 2011", 0, 3, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 30);
        validateMonthLog(treatmentCardModel, 5,2011,"May 2011", 1, 1, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 31);
        validateMonthLog(treatmentCardModel, 6,2011,"Jun 2011", 2, 5, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 30);
        validateMonthLog(treatmentCardModel, 7,2011,"Jul 2011", 3, 3, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 31);
        validateMonthLog(treatmentCardModel, 8,2011,"Aug 2011", 4, 7, 31, augustLogDays, augustTakenDays, augustNotTakenDays, 31);
    }

    private void validateMonthLog(TreatmentCardModel treatmentCardModel, Integer month, Integer year, String monthAndYear, int pos, int firstSunday, int maxDays, List<Integer> logDays, List<Integer> takenDays, List<Integer> notTakenDays, int totalNumberOfLogsInMonth) {
        MonthlyAdherence monthlyAdherence = treatmentCardModel.getMonthlyAdherences().get(pos);
        assertEquals(monthAndYear, monthlyAdherence.getMonthAndYear());
        assertEquals(month.toString(),monthlyAdherence.getMonth());
        assertEquals(year.toString(),monthlyAdherence.getYear());
        assertEquals(firstSunday, monthlyAdherence.getFirstSunday());
        assertEquals(maxDays, monthlyAdherence.getMaxDays());
        List<DailyAdherence> logs = monthlyAdherence.getLogs();
        assertEquals(totalNumberOfLogsInMonth, monthlyAdherence.getLogs().size());

        for (int i = 0; i < logDays.size(); i++) {
            DailyAdherence dailyAdherence = logs.get(i);
            assertEquals(logDays.get(i), (Integer) dailyAdherence.getDay());
            if (takenDays.contains(dailyAdherence.getDay()))
                assertEquals(PillStatus.Taken.getStatus(), dailyAdherence.getPillStatus());
            else if (notTakenDays.contains(dailyAdherence.getDay()))
                assertEquals(PillStatus.NotTaken.getStatus(), dailyAdherence.getPillStatus());
            else
                assertEquals(PillStatus.Unknown.getStatus(), dailyAdherence.getPillStatus());
        }

    }

    private Patient createPatientOn3DayAWeekTreatmentCategory(String externalId, LocalDate therapyStartDate, String therapyDocId) {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        patient.startTherapy(therapyStartDate);
        patient.latestTherapy().setId(therapyDocId);
        patient.getCurrentTreatment().setTherapyDocId(therapyDocId);
        patient.getCurrentTreatment().getTherapy().setId(therapyDocId);
        patient.getCurrentTreatment().setStartDate(therapyStartDate);
        return patient;
    }

    private Patient createPatientOn7DayAWeekTreatmentCategory(String externalId, LocalDate therapyStartDate) {
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, "1");
        patient.latestTherapy().getTreatmentCategory().setPillDays(asList(DayOfWeek.values()));
        return patient;
    }
}

