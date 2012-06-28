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
public class TreatmentCard {

    private Patient patient;

    private List<String> providerIds = new ArrayList<String>();

    private boolean isSundayDoseDate;

    private String therapyDocId;

    private AdherenceSection ipAdherenceSection = new AdherenceSection();

    public TreatmentCard(Patient patient) {
        this.patient = patient;
    }

    public TreatmentCard initIPSection(List<Adherence> adherenceData) {
        if (patient != null && patient.currentTherapy() != null && patient.currentTherapy().getStartDate() != null) {
            Therapy latestTherapy = patient.currentTherapy();
            LocalDate ipStartDate = latestTherapy.getStartDate();
            addAdherenceDataForGivenTherapy(patient, adherenceData, latestTherapy, ipStartDate, ipBoxLastDoseDate());
            return this;
        }
        return null;
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

    public void addAdherenceDatum(LocalDate doseDate, PillStatus pillStatus, String providerId, boolean adherenceCapturedDuringPausedPeriod) {
        MonthlyAdherence monthlyAdherence = getMonthAdherence(doseDate);
        monthlyAdherence.getLogs().add(new DailyAdherence(doseDate.getDayOfMonth(), pillStatus.getStatus(), providerId, adherenceCapturedDuringPausedPeriod, doseDate.isAfter(today())));

        /* Add to pool of providerIds to color them using pool of providerId colors. */
        if (!StringUtils.isEmpty(providerId) && !providerIds.contains(providerId))
            providerIds.add(providerId);
    }

    private void addAdherenceDatum(Adherence log, boolean doseDateInPausedPeriod) {
        String providerIdForTreatmentToWhichDoseBelongs = "";
        if (!log.getProviderId().equals(WHPConstants.UNKNOWN))
            providerIdForTreatmentToWhichDoseBelongs = log.getProviderId();

        addAdherenceDatum(log.getPillDate(), log.getPillStatus(), providerIdForTreatmentToWhichDoseBelongs, doseDateInPausedPeriod);
    }

    private MonthlyAdherence getMonthAdherence(LocalDate localDate) {
        String monthAndYear = localDate.toString(MonthlyAdherence.MonthAndYearFormat);

        for (MonthlyAdherence existingMonthlyAdherence : ipAdherenceSection.getMonthlyAdherences()) {
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
            ipAdherenceSection.getMonthlyAdherences().add(newlyAddedMonthlyAdherence);
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


    public LocalDate ipBoxLastDoseDate() {
        return patient.currentTherapy().getStartDate().plusMonths(5).minusDays(1);
    }

    public List<MonthlyAdherence> getMonthlyAdherences() {
        return ipAdherenceSection.getMonthlyAdherences();
    }
}
