package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.PhaseName;

import java.util.ArrayList;
import java.util.Collection;

import static org.motechproject.util.DateUtil.today;

public class Phases extends ArrayList<Phase> {

    //ektorp
    public Phases() {
    }

    public Phases(Collection<? extends Phase> phases) {
        super(phases);
    }

    @JsonIgnore
    public boolean ipPhaseWasExtended() {
        Phase eipPhase = getByPhaseName(PhaseName.EIP);
        return eipPhase != null && eipPhase.getStartDate() != null;
    }

    @JsonIgnore
    public Phase getByPhaseName(PhaseName phaseName) {
        for (Phase phase : this) {
            if (phase.getName().equals(phaseName)) return phase;
        }
        return null;
    }

    @JsonIgnore
    public LocalDate getIPLastDate() {
        LocalDate ipEndDate = getByPhaseName(PhaseName.IP).getEndDate();
        return ipEndDate != null ? ipEndDate : today();
    }

    @JsonIgnore
    public LocalDate getEIPLastDate() {
        if (ipPhaseWasExtended()) {
            LocalDate epEndDate = getByPhaseName(PhaseName.EIP).getEndDate();
            return epEndDate != null ? epEndDate : today();
        }
        return null;
    }

    @JsonIgnore
    void setIPStartDate(LocalDate IPStartDate) {
        getByPhaseName(PhaseName.IP).setStartDate(IPStartDate);
    }

    @JsonIgnore
    public void setIPEndDate(LocalDate IPEndDate) {
        getByPhaseName(PhaseName.IP).setEndDate(IPEndDate);
    }

    @JsonIgnore
    public Phase getCurrentPhase() {
        for (Phase phase : this) {
            if (phase.getStartDate() != null && phase.getEndDate() == null) {
                return phase;
            }
        }
        return null;
    }

    @JsonIgnore
    public Phase getLastCompletedPhase() {
        Phase lastCompletedPhase = null;
        for (Phase phase : this) {
            if (phase.getStartDate() != null && phase.getEndDate() != null) {
                lastCompletedPhase = phase;
            }
        }
        return lastCompletedPhase;
    }

    @JsonIgnore
    public void setEIPStartDate(LocalDate EIPStartDate) {
        getByPhaseName(PhaseName.EIP).setStartDate(EIPStartDate);
        if (EIPStartDate != null) {
            setIPEndDate(EIPStartDate.minusDays(1));
        } else {
            setIPEndDate(null);
        }
    }

    @JsonIgnore
    public void setEIPEndDate(LocalDate EIPEndDate) {
        getByPhaseName(PhaseName.EIP).setEndDate(EIPEndDate);
    }

    @JsonIgnore
    public void setCPStartDate(LocalDate CPStartDate) {
        getByPhaseName(PhaseName.CP).setStartDate(CPStartDate);
        Phase EIP = getByPhaseName(PhaseName.EIP);
        if (EIP.getStartDate() != null) {
            if (CPStartDate != null) {
                setEIPEndDate(CPStartDate.minusDays(1));
            } else {
                setEIPEndDate(null);
            }
        } else {
            if (CPStartDate != null) {
                setIPEndDate(CPStartDate.minusDays(1));
            } else {
                setIPEndDate(null);
            }
        }
    }

    @JsonIgnore
    public void setCPEndDate(LocalDate CPEndDate) {
        getByPhaseName(PhaseName.CP).setEndDate(CPEndDate);
    }

}
