package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.motechproject.util.DateUtil.today;

@Data
public class TreatmentCard {

    public static final int MONTHS_IN_IP_BOX = 5;
    public static final int MONTHS_IN_CP_BOX = 7;

    private Patient patient;
    private boolean isSundayDoseDate;
    private Therapy therapy;

    private AdherenceSection ipAndEipAdherenceSection = new AdherenceSection();
    private AdherenceSection cpAdherenceSection = new AdherenceSection();

    public TreatmentCard(Patient patient) {
        this.patient = patient;
        this.therapy = patient.getCurrentTherapy();
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();
        this.isSundayDoseDate = patientPillDays.contains(DayOfWeek.Sunday);
    }

    public TreatmentCard initIPSection(List<Adherence> adherenceData) {
        LocalDate ipStartDate = therapy.getStartDate();
        ipAndEipAdherenceSection.init(patient, adherenceData, therapy, ipStartDate, ipBoxLastDoseDate(), asList(Phase.IP, Phase.EIP));
        return this;
    }

    public TreatmentCard initCPSection(List<Adherence> adherenceData) {
        LocalDate cpStartDate = therapy.getPhases().getCPStartDate();
        cpAdherenceSection.init(patient, adherenceData, therapy, cpStartDate, cpBoxLastDoseDate(), asList(Phase.CP));
        return this;
    }

    public List<MonthlyAdherence> getMonthlyAdherences() {
        return ipAndEipAdherenceSection.getMonthlyAdherences();
    }

    public LocalDate ipBoxAdherenceEndDate() {
        Phases phases = therapy.getPhases();
        LocalDate endDate = phases.getNextPhaseStartDate(Phase.IP);
        if (phases.ipPhaseWasExtended()) {
            endDate = phases.getNextPhaseStartDate(Phase.EIP);
        }
        //minusDays(1) so that the end of current phase does not overlap with the start of the next phase
        return (endDate == null) ? today() : endDate.minusDays(1);
    }

    public LocalDate cpBoxAdherenceEndDate() {
        Phases phases = therapy.getPhases();
        if (phases.isOrHasBeenOnCp() && phases.getEndDate(Phase.CP) != null) {
            return phases.getEndDate(Phase.CP);
        }
        return today();
    }

    public LocalDate cpBoxAdherenceStartDate() {
        Phases phases = therapy.getPhases();
        return (phases.getCPStartDate() == null) ? today() : phases.getCPStartDate();
    }

    public LocalDate ipBoxLastDoseDate() {
        return therapy.getStartDate().plusMonths(MONTHS_IN_IP_BOX).minusDays(1);
    }

    public LocalDate cpBoxLastDoseDate() {
        Phases phases = therapy.getPhases();
        if (phases.isOrHasBeenOnCp()) {
            return phases.getCPStartDate().plusMonths(MONTHS_IN_CP_BOX).minusDays(1);
        }
        return null;
    }

    public List<String> getProviderIds() {
        Set<String> all = new HashSet<String>();
        all.addAll(ipAndEipAdherenceSection.getProviderIds());
        all.addAll(cpAdherenceSection.getProviderIds());
        return new ArrayList<>(all);
    }

    public boolean isCPAdherenceSectionValid() {
        return null != patient.getCurrentTherapy().getPhases().getCPStartDate();
    }

    public boolean isIPAdherenceSectionValid() {
        return patient.getCurrentTherapy().isOrHasBeenOnIP();
    }
}
