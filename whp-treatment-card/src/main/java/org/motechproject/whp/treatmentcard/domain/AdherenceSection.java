package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.util.WHPDateUtil;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.util.WHPDateUtil.findNumberOfDays;

@Data
public class AdherenceSection {

    private List<MonthlyAdherence> monthlyAdherences = new ArrayList<>();

    private List<String> providerIds = new ArrayList<String>();

    private boolean isSundayDoseDate;

    public AdherenceSection() {
    }

    public void init(Patient patient, List<Adherence> adherenceData, Therapy therapy, LocalDate startDate, LocalDate endDate) {
        addAdherenceDataForGivenTherapy(patient, adherenceData, therapy, startDate, endDate);
    }

    public void addAdherenceDataForGivenTherapy(Patient patient, List<Adherence> adherenceData, Therapy therapy, LocalDate startDate, LocalDate endDate) {
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();
        addMonthAdherenceForRange(startDate, endDate);

        List<LocalDate> adherenceDates = new ArrayList<>();
        for (Adherence datum : adherenceData)
            adherenceDates.add(datum.getPillDate());

        LocalDate today = DateUtil.today();
        for (LocalDate doseDate = startDate; WHPDateUtil.isOnOrBefore(doseDate, today) && WHPDateUtil.isOnOrBefore(doseDate, endDate); doseDate = doseDate.plusDays(1)) {
            boolean doseDateInPausedPeriod = patient.isDoseDateInPausedPeriod(doseDate);
            Treatment treatmentForDateInTherapy = patient.getTreatment(doseDate);
            String providerIdForTreatmentToWhichDoseBelongs = "";
            if (treatmentForDateInTherapy != null) {
                providerIdForTreatmentToWhichDoseBelongs = treatmentForDateInTherapy.getProviderId();
            }
            if (adherenceDates.contains(doseDate)) {
                Adherence adherence = adherenceData.get(adherenceDates.indexOf(doseDate));
                addAdherenceDatum(adherence, doseDateInPausedPeriod);
            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown, providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
                }
            }
        }
    }

    private void addAdherenceDatum(Adherence adherence, boolean doseDateInPausedPeriod) {
        String providerIdForTreatmentToWhichDoseBelongs = "";
        if (!adherence.getProviderId().equals(WHPConstants.UNKNOWN))
            providerIdForTreatmentToWhichDoseBelongs = adherence.getProviderId();

        addAdherenceDatum(adherence.getPillDate(), adherence.getPillStatus(), providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
    }


    public void addAdherenceDatum(LocalDate doseDate, PillStatus pillStatus, String providerId, boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherence(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), pillStatus.getStatus(), providerId, adherenceCapturedDuringPausedPeriod, doseDate.isAfter(today())));

        /* Add to pool of providerIds to color them using pool of providerId colors. */
        if (!StringUtils.isEmpty(providerId) && !providerIds.contains(providerId))
            providerIds.add(providerId);
    }

    private MonthlyAdherence getMonthAdherence(LocalDate localDate) {
        String monthAndYear = localDate.toString(MonthlyAdherence.MonthAndYearFormat);

        for (MonthlyAdherence existingMonthlyAdherence : getMonthlyAdherences()) {
            if (monthAndYear.equals(existingMonthlyAdherence.getMonthAndYear())) {
                return existingMonthlyAdherence;
            }
        }
        return null;
    }

    private void addMonthAdherenceForRange(LocalDate startDate, LocalDate endDate) {
        for (LocalDate monthStartDate = new LocalDate(startDate.getYear(), startDate.getMonthOfYear(), 1); WHPDateUtil.isOnOrBefore(monthStartDate, endDate); monthStartDate = monthStartDate.plusMonths(1)) {
            String monthAndYear = monthStartDate.toString(MonthlyAdherence.MonthAndYearFormat);
            int numberOfDays = findNumberOfDays(monthStartDate.getMonthOfYear(), monthStartDate.getYear());

            MonthlyAdherence newlyAddedMonthlyAdherence = new MonthlyAdherence(numberOfDays, monthAndYear, monthStartDate);
            getMonthlyAdherences().add(newlyAddedMonthlyAdherence);
        }
    }

}
