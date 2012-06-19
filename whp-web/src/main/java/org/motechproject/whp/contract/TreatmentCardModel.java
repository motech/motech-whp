package org.motechproject.whp.contract;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.domain.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.util.WHPDateUtil.findNumberOfDays;

@Data
public class TreatmentCardModel {

    private List<MonthlyAdherence> monthlyAdherences = new ArrayList<MonthlyAdherence>();
    private List<String> providerColorCodes = asList("blue", "green", "orange", "brown", "purple", "rosyBrown", "olive", "salmon", "cyan", "red");
    private List<String> providerIds = new ArrayList<String>();
    private boolean isSundayDoseDate;
    private String therapyDocId;
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

    public void addAdherenceDatum(LocalDate doseDate, PillStatus pillStatus, String providerId, boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherence(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), pillStatus.getStatus(), providerId, adherenceCapturedDuringPausedPeriod, doseDate.isAfter(today())));

        /* Add to pool of providerIds to color them using pool of providerId colors. */
        if (!StringUtils.isEmpty(providerId) && !providerIds.contains(providerId))
            providerIds.add(providerId);
    }

    public void addAdherenceDataForGivenTherapy(Patient patient, List<Adherence> adherenceData, Therapy therapy, Period period) {
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();
        LocalDate startDate = therapy.getStartDate();
        LocalDate endDate = startDate.plus(period);
        therapyDocId = therapy.getId();
        isSundayDoseDate = patientPillDays.contains(DayOfWeek.Sunday);
        List<LocalDate> adherenceDates = new ArrayList<>();
        for(Adherence datum : adherenceData)
            adherenceDates.add(datum.getPillDate());
        for (LocalDate doseDate = startDate; doseDate.isBefore(endDate); doseDate = doseDate.plusDays(1)) {

            boolean doseDateInPausedPeriod = isDoseDateInPausedPeriod(patient, therapy, doseDate);
            Treatment treatmentForDateInTherapy = patient.getTreatmentForDateInTherapy(doseDate, therapy.getId());
            String providerIdForTreatmentToWhichDoseBelongs = "";
            if (treatmentForDateInTherapy != null) {
                providerIdForTreatmentToWhichDoseBelongs = treatmentForDateInTherapy.getProviderId();
            }
            if (adherenceDates.contains(doseDate)) {
                Adherence log = adherenceData.get(adherenceDates.indexOf(doseDate));
                addAdherenceDatum(doseDate, log.getPillStatus(), providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);

            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown, providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
                }
            }
        }
    }

    private boolean isDoseDateInPausedPeriod(Patient patient, Therapy therapy, LocalDate doseDate) {
        boolean isIt = false;
        Treatment treatmentForDateInTherapy = patient.getTreatmentForDateInTherapy(doseDate, therapy.getId());
        if (treatmentForDateInTherapy != null) {
            TreatmentInterruptions interruptions = treatmentForDateInTherapy.getInterruptions();
            for (TreatmentInterruption interruption : interruptions) {
                if (isDoseDateInInterruptionPeriod(doseDate, interruption, treatmentForDateInTherapy)) {
                    isIt = true;
                    break;
                }
            }
        }
        return isIt;
    }

    private boolean isDoseDateInInterruptionPeriod(LocalDate doseDate, TreatmentInterruption interruption, Treatment treatmentForDateInTherapy) {
        LocalDate pauseDate = interruption.getPauseDate();
        LocalDate resumptionDate = interruption.getResumptionDate();
        if (resumptionDate == null) {
            LocalDate endDateOfTreatmentToWhichDoseBelongs = treatmentForDateInTherapy.getEndDate();
            if (endDateOfTreatmentToWhichDoseBelongs == null) {
                resumptionDate = today();
            } else {
                resumptionDate = endDateOfTreatmentToWhichDoseBelongs;
            }
        }
        return DateUtil.isOnOrAfter(DateUtil.newDateTime(doseDate), DateUtil.newDateTime(pauseDate)) &&
                DateUtil.isOnOrBefore(DateUtil.newDateTime(doseDate), DateUtil.newDateTime(resumptionDate));
    }
}
