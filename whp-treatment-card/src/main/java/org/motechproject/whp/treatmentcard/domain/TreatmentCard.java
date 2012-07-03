package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.PhaseName;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;

@Data
public class TreatmentCard {

    public static final int MONTHS_IN_IP_BOX = 5;
    public static final int MONTHS_IN_CP_BOX = 7;

    private Patient patient;

    private List<String> providerIds = new ArrayList<>();

    private boolean isSundayDoseDate;

    private Therapy therapy;

    private AdherenceSection ipAdherenceSection = new AdherenceSection();
    private AdherenceSection cpAdherenceSection = new AdherenceSection();

    public TreatmentCard(Patient patient) {
        this.patient = patient;
        this.therapy = patient.currentTherapy();
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();
        this.isSundayDoseDate = patientPillDays.contains(DayOfWeek.Sunday);
    }

    public TreatmentCard initIPSection(List<Adherence> adherenceData) {
        Therapy currentTherapy = patient.currentTherapy();
        LocalDate ipStartDate = currentTherapy.getStartDate();
        ipAdherenceSection.init(patient, adherenceData, currentTherapy, ipStartDate, ipBoxLastDoseDate());
        return this;
    }

    public TreatmentCard initCPSection(List<Adherence> adherenceData) {
        Therapy currentTherapy = patient.currentTherapy();
        LocalDate cpStartDate = currentTherapy.getPhases().getCPStartDate();
        cpAdherenceSection.init(patient, adherenceData, currentTherapy, cpStartDate, cpBoxLastDoseDate());
        return this;
    }

    public List<MonthlyAdherence> getMonthlyAdherences() {
        return ipAdherenceSection.getMonthlyAdherences();
    }

    public LocalDate ipBoxAdherenceEndDate() {
        Phases phases = therapy.getPhases();
        LocalDate endDate = phases.getIPLastDate();
        if (phases.ipPhaseWasExtended()) {
            endDate = phases.getEIPLastDate();
        }
        return endDate;
    }

    public LocalDate cpBoxAdherenceEndDate() {
        Phases phases = therapy.getPhases();
        if (phases.isOrHasBeenOnCp() && phases.getByPhaseName(PhaseName.CP).getEndDate() != null) {
            return phases.getByPhaseName(PhaseName.CP).getEndDate();

        }
        return today();
    }

    public LocalDate ipBoxLastDoseDate() {
        return therapy.getStartDate().plusMonths(MONTHS_IN_IP_BOX).minusDays(1);
    }

    public LocalDate cpBoxLastDoseDate() {
        Phases phases = therapy.getPhases();
        if(phases.isOrHasBeenOnCp()) {
            return phases.getByPhaseName(PhaseName.CP).getStartDate().plusMonths(MONTHS_IN_CP_BOX).minusDays(1);
        }
        return null;
    }

}
