package org.motechproject.whp.contract;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class TreatmentCardModelTest {

    private final String externalId = "externalid";

    @Test
    public void shouldReturnExistingMonthAdherenceRequestForDatesInExistingMonth() {
        LocalDate therapyStartDate = new LocalDate(2012, 2, 3);

        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();
        AdherenceData log1 = new AdherenceData(externalId, "", new LocalDate(2012, 2, 10));
        log1.status(PillStatus.Taken.getStatus());
        AdherenceData log2 = new AdherenceData(externalId, "", new LocalDate(2012, 2, 15));
        log2.status(PillStatus.NotTaken.getStatus());
        AdherenceData log3 = new AdherenceData(externalId, "", new LocalDate(2012, 3, 12));
        log3.status(PillStatus.Unknown.getStatus());
        AdherenceData log4 = new AdherenceData(externalId, "", new LocalDate(2012, 3, 28));
        log4.status(PillStatus.Taken.getStatus());

        List<AdherenceData> adherenceData = Arrays.asList(log1, log2, log3, log4);

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

        treatmentCardModel.addAdherenceData(therapyStartDate, therapyStartDate.plusMonths(5), adherenceData, asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday), new TreatmentInterruptions());

        assertEquals(6, treatmentCardModel.getMonthlyAdherences().size());
        validateMonthLog(treatmentCardModel, "Feb 2012", 0, 5, 29, febLogDays, febTakenDays, febNotTakenDays, 12);
        validateMonthLog(treatmentCardModel, "Mar 2012", 1, 4, 31, marchLogDays, marchTakenDays, marchNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "Apr 2012", 2, 1, 30, aprilLogDays, aprilTakenDays, aprilNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "May 2012", 3, 6, 31, mayLogDays, mayTakenDays, mayNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "Jun 2012", 4, 3, 30, juneLogDays, juneTakenDays, juneNotTakenDays, 13);
        validateMonthLog(treatmentCardModel, "Jul 2012", 5, 1, 31, julyLogDays, julyTakenDays, julyNotTakenDays, 1);
    }

    @Test
    public void shouldBuildIPTreatmentCardModelForPatientOn7DayTreatmentCategory() {
        LocalDate therapyStartDate = new LocalDate(2012, 4, 1);
        TreatmentCardModel treatmentCardModel = new TreatmentCardModel();

        AdherenceData log1 = new AdherenceData(externalId, "", new LocalDate(2012, 4, 10));
        log1.status(PillStatus.Taken.getStatus());

        treatmentCardModel.addAdherenceData(therapyStartDate, therapyStartDate.plusMonths(5), asList(log1), asList(DayOfWeek.values()), new TreatmentInterruptions());

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
}

