package org.motechproject.whp.contract;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.builder.AdherenceDataBuilder.createLog;

public class TreatmentCardModelTest {

    private final String externalId = "externalid";

    @Test
    public void shouldReturnExistingMonthAdherenceRequestForDatesInExistingMonth() {
        String provider = "prov_id1";

        LocalDate therapyStartDate = new LocalDate(2012, 2, 3);
        LocalDate therapyEndDate = new LocalDate(2012, 7, 2);
        String therapyDocId = "therapyDocId";
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate, therapyDocId);
        patient.getCurrentTreatment().setProviderId(provider);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        Adherence log1 = createLog(new LocalDate(2012, 2, 10), provider, PillStatus.Taken);
        Adherence log2 = createLog(new LocalDate(2012, 2, 15), provider, PillStatus.NotTaken);
        Adherence log3 = createLog(new LocalDate(2012, 3, 12), provider, PillStatus.Unknown);
        Adherence log4 = createLog(new LocalDate(2012, 3, 28), provider, PillStatus.Taken);

        List<Adherence> adherenceData = asList(log1, log2, log3, log4);

        List<Integer> febLogDays = asList(3, 6, 8, 10, 13, 15, 17, 20, 22, 24, 27, 29);
        List<Integer> febTakenDays = asList(10);
        List<Integer> febNotTakenDays = asList(15);

        List<Integer> marchLogDays = asList(2, 5, 7, 9, 12, 14, 16, 19, 21, 23, 26, 28, 30);
        List<Integer> marchTakenDays = asList(28);
        List<Integer> marchNotTakenDays = asList();

        List<Integer> aprilLogDays = asList(2, 4, 6, 9, 11, 13, 16, 18, 20, 23, 25, 27, 30);
        List<Integer> aprilTakenDays = asList();
        List<Integer> aprilNotTakenDays = asList();

        List<Integer> mayLogDays = asList(2, 4, 7, 9, 11, 14, 16, 18, 21, 23, 25, 28, 30);
        List<Integer> mayTakenDays = asList();
        List<Integer> mayNotTakenDays = asList();

        List<Integer> juneLogDays = asList(1, 4, 6, 8, 11, 13, 15, 18, 20, 22, 25, 27, 29);
        List<Integer> juneTakenDays = asList();
        List<Integer> juneNotTakenDays = asList();

        List<Integer> julyLogDays = asList(2);
        List<Integer> julyTakenDays = asList();
        List<Integer> julyNotTakenDays = asList();

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(),therapyStartDate,therapyEndDate);

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        validateMonthLog(treatmentCardModel, 2, 2012, "Feb 2012", 0, 5, 29, febLogDays, febTakenDays, febNotTakenDays, 12);
        validateMonthLog(treatmentCardModel, 3,2012,"Mar 2012", 1, 4, 31, marchLogDays, marchTakenDays, marchNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, 4,2012,"Apr 2012", 2, 1, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 13);
        validateMonthLog(treatmentCardModel,5,2012, "May 2012", 3, 6, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 13);
        validateMonthLog(treatmentCardModel,6,2012, "Jun 2012", 4, 3, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 13);
        validateMonthLog(treatmentCardModel,7,2012, "Jul 2012", 5, 1, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 1);
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
        LocalDate therapyEndDate = new LocalDate(2012, 7, 3);
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
        assertEquals("p4", monthlyAdherences.get(5).getLogs().get(0).getProviderId());

        assertEquals(asList("p1", "p2", "p3", "p4"), treatmentCardModel.getProviderIds());

    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatientOn7DayTreatmentCategory() {
        LocalDate therapyStartDate = new LocalDate(2012, 4, 1);
        LocalDate therapyEndDate = new LocalDate(2012, 8, 31);
        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();
        Patient patient = createPatientOn7DayAWeekTreatmentCategory(externalId, therapyStartDate);

        Adherence log1 = createLog(new LocalDate(2012, 4, 10), "ext_id", PillStatus.Taken);

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

        validateMonthLog(treatmentCardModel, 4,2012,"Apr 2012", 0, 1, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 30);
        validateMonthLog(treatmentCardModel, 5,2012,"May 2012", 1, 6, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 31);
        validateMonthLog(treatmentCardModel, 6,2012,"Jun 2012", 2, 3, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 30);
        validateMonthLog(treatmentCardModel, 7,2012,"Jul 2012", 3, 1, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 31);
        validateMonthLog(treatmentCardModel, 8,2012,"Aug 2012", 4, 5, 31, augustLogDays, augustTakenDays, augustNotTakenDays, 31);
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

