package org.motechproject.whp.contract;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.domain.TreatmentInterruption;
import org.motechproject.whp.patient.domain.TreatmentInterruptions;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.motechproject.whp.patient.util.WHPDateUtil.findNumberOfDays;

@Data
public class TreatmentCardModel {

    private List<MonthlyAdherence> monthlyAdherences = new ArrayList<MonthlyAdherence>();
    private List<String> providerColorCodes = asList("red", "blue", "green", "orange", "purple", "brown", "black", "yellow", "olive", "cyan");
    private List<String> providerIds = new ArrayList<String>();
    public TreatmentCardModel() {
    }

    public MonthlyAdherence getMonthAdherence(LocalDate localDate) {
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

    public void addAdherenceDatum(LocalDate doseDate, int status,String providerId,boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherence(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), status,providerId,adherenceCapturedDuringPausedPeriod));
    }

    public void addAdherenceData(LocalDate startDate, LocalDate endDate, List<AdherenceData> adherenceData, List<DayOfWeek> patientPillDays, TreatmentInterruptions interruptions) {
        List<LocalDate> adherenceDates = extract(adherenceData, on(AdherenceData.class).doseDate());

        for (LocalDate doseDate = startDate; doseDate.isBefore(endDate); doseDate = doseDate.plusDays(1)) {
            boolean doseDateInPausedPeriod = isDoseDateInPausedPeriod(doseDate, interruptions);
            if (adherenceDates.contains(doseDate)) {
                AdherenceData log = adherenceData.get(adherenceDates.indexOf(doseDate));
                String providerId = log.meta().get(AdherenceConstants.PROVIDER_ID).toString();
                addAdherenceDatum(doseDate, log.status(), providerId,doseDateInPausedPeriod);
                if(!providerIds.contains(providerId))
                    providerIds.add(providerId);

            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown.getStatus(),"",doseDateInPausedPeriod);
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
