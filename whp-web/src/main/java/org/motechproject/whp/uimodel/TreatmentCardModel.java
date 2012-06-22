package org.motechproject.whp.uimodel;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.util.WHPDateUtil;
import org.motechproject.whp.refdata.domain.WHPConstants;

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

    private MonthlyAdherence getMonthAdherence(LocalDate localDate) {
        String monthAndYear = localDate.toString(MonthlyAdherence.MonthAndYearFormat);

        for (MonthlyAdherence existingMonthlyAdherence : monthlyAdherences) {
            if (monthAndYear.equals(existingMonthlyAdherence.getMonthAndYear())) {
                return existingMonthlyAdherence;
            }
        }
        return null;
    }

    public void addAdherenceDatum(LocalDate doseDate, PillStatus pillStatus, String providerId, boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherence(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), pillStatus.getStatus(), providerId, adherenceCapturedDuringPausedPeriod, doseDate.isAfter(today())));

        /* Add to pool of providerIds to color them using pool of providerId colors. */
        if (!StringUtils.isEmpty(providerId) && !providerIds.contains(providerId))
            providerIds.add(providerId);
    }

    public void addAdherenceDataForGivenTherapy(Patient patient, List<Adherence> adherenceData, Therapy therapy, LocalDate startDate, LocalDate endDate) {
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();
        therapyDocId = therapy.getId();
        isSundayDoseDate = patientPillDays.contains(DayOfWeek.Sunday);

        addMonthAdherenceForRange(startDate, endDate);

        List<LocalDate> adherenceDates = new ArrayList<>();
        for (Adherence datum : adherenceData)
            adherenceDates.add(datum.getPillDate());

        LocalDate today = DateUtil.today();
        for (LocalDate doseDate = startDate; WHPDateUtil.isOnOrBefore(doseDate, today) && WHPDateUtil.isOnOrBefore(doseDate, endDate); doseDate = doseDate.plusDays(1)) {

            boolean doseDateInPausedPeriod = isDoseDateInPausedPeriod(patient, doseDate);
            Treatment treatmentForDateInTherapy = patient.getTreatment(doseDate);
            String providerIdForTreatmentToWhichDoseBelongs = "";
            if (treatmentForDateInTherapy != null) {
                providerIdForTreatmentToWhichDoseBelongs = treatmentForDateInTherapy.getProviderId();
            }
            if (adherenceDates.contains(doseDate)) {
                Adherence log = adherenceData.get(adherenceDates.indexOf(doseDate));
                addAdherenceDatum(log, doseDateInPausedPeriod);
            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown, providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
                }
            }
        }
    }

    private void addAdherenceDatum(Adherence log, boolean doseDateInPausedPeriod) {
        String providerIdForTreatmentToWhichDoseBelongs = "";
        if (!log.getProviderId().equals(WHPConstants.UNKNOWN))
            providerIdForTreatmentToWhichDoseBelongs = log.getProviderId();

        addAdherenceDatum(log.getPillDate(), log.getPillStatus(), providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
    }

    private void addMonthAdherenceForRange(LocalDate startDate, LocalDate endDate) {
        for (LocalDate monthStartDate = new LocalDate(startDate.getYear(), startDate.getMonthOfYear(), 1); WHPDateUtil.isOnOrBefore(monthStartDate, endDate); monthStartDate = monthStartDate.plusMonths(1)) {
            String monthAndYear = monthStartDate.toString(MonthlyAdherence.MonthAndYearFormat);
            int numberOfDays = findNumberOfDays(monthStartDate.getMonthOfYear(), monthStartDate.getYear());

            MonthlyAdherence newlyAddedMonthlyAdherence = new MonthlyAdherence(numberOfDays, monthAndYear, monthStartDate);
            monthlyAdherences.add(newlyAddedMonthlyAdherence);
        }
    }

    private boolean isDoseDateInPausedPeriod(Patient patient, LocalDate doseDate) {
        boolean isIt = false;
        Treatment treatment = patient.getTreatment(doseDate);
        if (treatment != null) {
            TreatmentInterruptions interruptions = treatment.getInterruptions();
            for (TreatmentInterruption interruption : interruptions) {
                if (isDoseDateInInterruptionPeriod(doseDate, interruption, treatment)) {
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
