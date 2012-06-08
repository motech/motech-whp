package org.motechproject.whp.contract;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.motechproject.whp.patient.util.WHPDateUtil.findNumberOfDays;

@Data
public class TreatmentCardModel {

    private List<MonthlyAdherence> monthlyAdherences = new ArrayList<MonthlyAdherence>();

    public TreatmentCardModel() {
    }

    public MonthlyAdherence getMonthAdherenceRequest(LocalDate localDate) {
        String monthAndYear = localDate.toString("MMM YYYY");

        for (MonthlyAdherence existingMonthlyAdherence : monthlyAdherences) {
            if (monthAndYear.equals(existingMonthlyAdherence.getMonthAndYear())) {
                return existingMonthlyAdherence;
            }
        }

        int numberOfDays = findNumberOfDays(localDate.getMonthOfYear(), localDate.getYear());

        MonthlyAdherence newlyAddedMonthlyAdherence = new MonthlyAdherence(numberOfDays, monthAndYear, new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), 1));
        monthlyAdherences.add(newlyAddedMonthlyAdherence);
        return newlyAddedMonthlyAdherence;
    }

    public void addAdherenceDatum(LocalDate doseDate, int status) {
        MonthlyAdherence monthlyAdherence = getMonthAdherenceRequest(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), status));
    }

    public void addAdherenceData(LocalDate startDate, LocalDate endDate, List<AdherenceData> adherenceData, List<DayOfWeek> patientPillDays) {
        List<LocalDate> adherenceDates = extract(adherenceData, on(AdherenceData.class).doseDate());

        for (LocalDate doseDate = startDate; doseDate.isBefore(endDate); doseDate = doseDate.plusDays(1)) {
            if (adherenceDates.contains(doseDate)) {
                AdherenceData log = adherenceData.get(adherenceDates.indexOf(doseDate));
                addAdherenceDatum(doseDate, log.status());
            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown.getStatus());
                }
            }
        }
    }
}
