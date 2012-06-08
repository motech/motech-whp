package org.motechproject.whp.contract;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.domain.TreatmentInterruption;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

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

    public void addAdherenceDatum(LocalDate doseDate, int status, boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherenceRequest(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), status, adherenceCapturedDuringPausedPeriod));
    }

    public void addAdherenceData(LocalDate startDate, LocalDate endDate, List<AdherenceData> adherenceData, List<DayOfWeek> patientPillDays, TreatmentInterruptions interruptions) {
        List<LocalDate> adherenceDates = extract(adherenceData, on(AdherenceData.class).doseDate());

        for (LocalDate doseDate = startDate; doseDate.isBefore(endDate); doseDate = doseDate.plusDays(1)) {
            boolean doseDateInPausedPeriod = isDoseDateInPausedPeriod(doseDate, interruptions);
            if (adherenceDates.contains(doseDate)) {
                AdherenceData log = adherenceData.get(adherenceDates.indexOf(doseDate));
                addAdherenceDatum(doseDate, log.status(), doseDateInPausedPeriod);
            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown.getStatus(), doseDateInPausedPeriod);
                }
            }
        }
    }

    private boolean isDoseDateInPausedPeriod(LocalDate doseDate, TreatmentInterruptions interruptions) {
        boolean isIt = false;
        for (TreatmentInterruption interruption : interruptions) {
            if (isDoseDateInInterruptionPeriod(doseDate, interruption)){
                isIt = true;
                break;
            }
        }
        return isIt;
    }

    private boolean isDoseDateInInterruptionPeriod(LocalDate doseDate, TreatmentInterruption interruption) {
        return DateUtil.isOnOrAfter(DateUtil.newDateTime(doseDate), DateUtil.newDateTime(interruption.getPauseDate())) &&
                DateUtil.isOnOrBefore(DateUtil.newDateTime(doseDate), DateUtil.newDateTime(interruption.getResumptionDate()));
    }
}
