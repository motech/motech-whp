package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.domain.Therapy;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreatmentCard {

    public static final int MONTHS_IN_IP_BOX = 5;

    private Patient patient;

    private List<String> providerIds = new ArrayList<>();

    private boolean isSundayDoseDate;

    private String therapyDocId;

    private Therapy therapy;

    private AdherenceSection ipAdherenceSection = new AdherenceSection();

    public TreatmentCard(Patient patient) {
        this.patient = patient;
        this.therapy = patient.currentTherapy();
        this.therapyDocId = therapy.getId();
        List<DayOfWeek> patientPillDays = therapy.getTreatmentCategory().getPillDays();
        this.isSundayDoseDate = patientPillDays.contains(DayOfWeek.Sunday);
    }

    public TreatmentCard initIPSection(List<Adherence> adherenceData) {
        Therapy currentTherapy = patient.currentTherapy();
        LocalDate ipStartDate = currentTherapy.getStartDate();
        ipAdherenceSection.init(patient, adherenceData, currentTherapy, ipStartDate, ipBoxLastDoseDate());
        return this;
    }

    public LocalDate ipBoxLastDoseDate() {
        return patient.currentTherapy().getStartDate().plusMonths(MONTHS_IN_IP_BOX).minusDays(1);
    }

    public List<MonthlyAdherence> getMonthlyAdherences() {
        return ipAdherenceSection.getMonthlyAdherences();
    }

    public LocalDate ipBoxAdherenceEndDate() {
        Phases phases = therapy.getPhases();
        LocalDate endDate = phases.getIPLastDate();
        if(phases.ipPhaseWasExtended()) {
            endDate = phases.getEIPLastDate();
        }
        return endDate;
    }

}
