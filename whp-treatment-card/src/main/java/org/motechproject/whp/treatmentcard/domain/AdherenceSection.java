package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.WHPConstants;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phase;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.util.WHPDateUtil;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.patient.util.WHPDateUtil.findNumberOfDays;

@Data
public class AdherenceSection {

    private List<MonthlyAdherence> monthlyAdherences = new ArrayList<>();

    private LocalDate sectionStartDate;
    private LocalDate sectionEndDate;
    private List<Phase> phases = new ArrayList<>();

    private List<String> providerIds = new ArrayList<>();
    private boolean isSundayDoseDate;

    public AdherenceSection() {
    }

    public void init(Patient patient, List<Adherence> adherenceData, Therapy therapy, LocalDate startDate, LocalDate endDate, List<Phase> phases) {
        this.sectionStartDate = startDate;
        this.sectionEndDate = endDate;
        this.phases = phases;
        addMonthAdherenceForRange(sectionStartDate, sectionEndDate);
        for (Phase phase : phases) {
            if (phase.hasStarted())
                addAdherenceDataForGivenTherapy(patient, adherenceData, therapy, phase.getStartDate(), phase.getEndDate());
        }
    }

    public void addAdherenceDataForGivenTherapy(Patient patient, List<Adherence> adherenceData, Therapy therapy, LocalDate startDate, LocalDate endDate) {
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();

        List<LocalDate> providedAdherenceDates = getProvidedAdherenceDates(adherenceData);

        LocalDate phaseLastDate = endDate;
        if (phaseLastDate == null) phaseLastDate = today();
        for (LocalDate doseDate = startDate; WHPDateUtil.isOnOrBefore(doseDate, phaseLastDate); doseDate = doseDate.plusDays(1)) {
            boolean doseDateInPausedPeriod = patient.isDoseDateInPausedPeriod(doseDate);
            Treatment treatmentForDateInTherapy = patient.getTreatment(doseDate);
            String providerIdForTreatmentToWhichDoseBelongs = "";
            if (treatmentForDateInTherapy != null) {
                providerIdForTreatmentToWhichDoseBelongs = treatmentForDateInTherapy.getProviderId();
            }
            if (providedAdherenceDates.contains(doseDate)) {
                Adherence adherence = adherenceData.get(providedAdherenceDates.indexOf(doseDate));
                addAdherenceDatum(adherence, doseDateInPausedPeriod);
            } else {
                if (patientPillDays.contains(DayOfWeek.getDayOfWeek(doseDate))) {
                    addAdherenceDatum(doseDate, PillStatus.Unknown, providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
                }
            }
        }
    }

    private List<LocalDate> getProvidedAdherenceDates(List<Adherence> adherenceData) {
        List<LocalDate> adherenceDates = new ArrayList<>();
        for (Adherence datum : adherenceData)
            adherenceDates.add(datum.getPillDate());
        return adherenceDates;
    }

    private void addAdherenceDatum(Adherence adherence, boolean doseDateInPausedPeriod) {
        String providerIdForTreatmentToWhichDoseBelongs = "";
        if (!adherence.getProviderId().equals(WHPConstants.UNKNOWN))
            providerIdForTreatmentToWhichDoseBelongs = adherence.getProviderId();

        addAdherenceDatum(adherence.getPillDate(), adherence.getPillStatus(), providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
    }


    public void addAdherenceDatum(LocalDate doseDate, PillStatus pillStatus, String providerId, boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherence(doseDate);
        if (monthlyAdherence == null) return; // TODO : test to be written
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
