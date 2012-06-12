package org.motechproject.whp.contract;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
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
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate);
        patient.getCurrentTreatment().setProviderId(provider);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        AdherenceData log1 = createLog(new LocalDate(2012, 2, 10), provider, PillStatus.Taken);
        AdherenceData log2 = createLog(new LocalDate(2012, 2, 15), provider, PillStatus.NotTaken);
        AdherenceData log3 = createLog(new LocalDate(2012, 3, 12), provider, PillStatus.Unknown);
        AdherenceData log4 = createLog(new LocalDate(2012, 3, 28), provider, PillStatus.Taken);

        List<AdherenceData> adherenceData = asList(log1, log2, log3, log4);

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

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(), new Period().withMonths(5));

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        validateMonthLog(treatmentCardModel, "Feb 2012", 0, 5, 29, febLogDays, febTakenDays, febNotTakenDays, 12);
        validateMonthLog(treatmentCardModel, "Mar 2012", 1, 4, 31, marchLogDays, marchTakenDays, marchNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "Apr 2012", 2, 1, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "May 2012", 3, 6, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "Jun 2012", 4, 3, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "Jul 2012", 5, 1, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 1);
        assertEquals(asList(provider), treatmentCardModel.getProviderIds());

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
        patient.startTherapy(new LocalDate(2012, 2, 4));

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        AdherenceData log1 = createLog(new LocalDate(2012, 2, 10), provider1, PillStatus.Taken);
        AdherenceData log2 = createLog(new LocalDate(2012, 2, 15), provider2, PillStatus.NotTaken);
        AdherenceData log3 = createLog(new LocalDate(2012, 3, 12), provider4, PillStatus.Unknown);
        AdherenceData log4 = createLog(new LocalDate(2012, 3, 28), provider4, PillStatus.Taken);
        List<AdherenceData> adherenceData = Arrays.asList(log1, log2, log3, log4);

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(), new Period().withMonths(5));
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
    public void shouldTakeProviderIdBasedOnTreatmentAndNotAdherenceLog() {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        String provider1 = "p1";
        LocalDate firstDoseTakenDate = new LocalDate(2012, 2, 4);
        Treatment currentTreatment = new TreatmentBuilder().withDefaults().withTherapyDocId("1").withStartDate(firstDoseTakenDate).withProviderId(provider1).build();
        patient.setCurrentTreatment(currentTreatment);
        patient.startTherapy(firstDoseTakenDate);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        AdherenceData log1 = createLog(firstDoseTakenDate, "notp1", PillStatus.Taken);
        List<AdherenceData> adherenceData = asList(log1);

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, adherenceData, patient.latestTherapy(), new Period().withMonths(5));

        List<MonthlyAdherence> monthlyAdherences = treatmentCardModel.getMonthlyAdherences();

        assertEquals("p1", monthlyAdherences.get(0).getLogs().get(0).getProviderId());
        assertEquals(asList("p1"),treatmentCardModel.getProviderIds());
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatientOn7DayTreatmentCategory() {
        LocalDate therapyStartDate = new LocalDate(2012, 4, 1);
        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();
        Patient patient = createPatientOn7DayAWeekTreatmentCategory(externalId, therapyStartDate);

        AdherenceData log1 = createLog(new LocalDate(2012, 4, 10), "ext_id", PillStatus.Taken);

        treatmentCardModel.addAdherenceDataForGivenTherapy(patient, asList(log1), patient.latestTherapy(), new Period().withMonths(5));

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

        validateMonthLog(treatmentCardModel, "Apr 2012", 0, 1, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 30);
        validateMonthLog(treatmentCardModel, "May 2012", 1, 6, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 31);
        validateMonthLog(treatmentCardModel, "Jun 2012", 2, 3, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 30);
        validateMonthLog(treatmentCardModel, "Jul 2012", 3, 1, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 31);
        validateMonthLog(treatmentCardModel, "Aug 2012", 4, 5, 31, augustLogDays, augustTakenDays, augustNotTakenDays, 31);
    }

    private void validateMonthLog(TreatmentCardModel treatmentCardModel, String monthAndYear, int pos, int firstSunday, int maxDays, List<Integer> logDays, List<Integer> takenDays, List<Integer> notTakenDays, int totalNumberOfLogsInMonth) {
        MonthlyAdherence monthlyAdherence = treatmentCardModel.getMonthlyAdherences().get(pos);
        assertEquals(monthAndYear, monthlyAdherence.getMonthAndYear());
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

    private Patient createPatientOn3DayAWeekTreatmentCategory(String externalId, LocalDate therapyStartDate) {
        Patient patient = new PatientBuilder().withDefaults().withPatientId(externalId).build();
        patient.startTherapy(therapyStartDate);
        patient.latestTherapy().setId("1");
        patient.getCurrentTreatment().setTherapyDocId("1");
        patient.getCurrentTreatment().getTherapy().setId("1");
        patient.getCurrentTreatment().setStartDate(therapyStartDate);
        return patient;
    }

    private Patient createPatientOn7DayAWeekTreatmentCategory(String externalId, LocalDate therapyStartDate) {
        Patient patient = createPatientOn3DayAWeekTreatmentCategory(externalId, therapyStartDate);
        patient.latestTherapy().getTreatmentCategory().setPillDays(asList(DayOfWeek.values()));
        return patient;
    }
}

