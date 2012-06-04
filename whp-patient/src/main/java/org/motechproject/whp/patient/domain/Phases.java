package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;

public class Phases extends ArrayList<Phase> {

    //ektorp
    public Phases() {
    }

    public Phases(Collection<? extends Phase> phases) {
        super(phases);
    }

    @JsonIgnore
    public Phase getByPhaseName(PhaseName phaseName) {
        for (Phase phase : this) {
            if (phase.getName().equals(phaseName)) return phase;
        }
        return null;
    }

    @JsonIgnore
    public void setIPStartDate(LocalDate IPStartDate) {
        getByPhaseName(PhaseName.IP).setStartDate(IPStartDate);
    }

    @JsonIgnore
    public void setIPEndDate(LocalDate IPEndDate) {
        getByPhaseName(PhaseName.IP).setEndDate(IPEndDate);
    }

    @JsonIgnore
    public void setEIPStartDate(LocalDate EIPStartDate) {
        getByPhaseName(PhaseName.EIP).setStartDate(EIPStartDate);
        getByPhaseName(PhaseName.IP).setEndDate(EIPStartDate.minusDays(1));
    }

    @JsonIgnore
    public void setEIPEndDate(LocalDate EIPEndDate) {
        getByPhaseName(PhaseName.EIP).setEndDate(EIPEndDate);
    }

    @JsonIgnore
    public void setCPStartDate(LocalDate CPStartDate) {
        getByPhaseName(PhaseName.CP).setStartDate(CPStartDate);
        Phase EIP = getByPhaseName(PhaseName.EIP);
        if(EIP.getStartDate() != null) {
            EIP.setEndDate(CPStartDate.minusDays(1));
        } else {
            getByPhaseName(PhaseName.IP).setEndDate(CPStartDate.minusDays(1));
        }
    }

    @JsonIgnore
    public void setCPEndDate(LocalDate CPEndDate) {
        getByPhaseName(PhaseName.CP).setEndDate(CPEndDate);
    }

}
