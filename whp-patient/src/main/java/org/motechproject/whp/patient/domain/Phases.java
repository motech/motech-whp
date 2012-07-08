package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;

@Data
public class Phases {

    private List<PhaseRecord> all = new ArrayList<>();

    private Phase nextPhaseName = null;

    //ektorp
    public Phases() {
    }

    public Phases(List<PhaseRecord> all) {
        this.all = all;
    }

    @JsonIgnore
    public boolean ipPhaseWasExtended() {
        PhaseRecord eipPhase = eipPhase();
        return eipPhase != null && eipPhase.getStartDate() != null;
    }

    @JsonIgnore
    public boolean isOrHasBeenOnCp() {
        PhaseRecord cpPhase = cpPhase();
        return cpPhase != null && cpPhase.hasStarted();
    }

    @JsonIgnore
    public boolean isOrHasBeenOnIp() {
        PhaseRecord ipPhase = ipPhase();
        return ipPhase != null && ipPhase.hasStarted();
    }

    @JsonIgnore
    public PhaseRecord ipPhase() {
        return getByPhaseName(Phase.IP);
    }

    @JsonIgnore
    public PhaseRecord eipPhase() {
        return getByPhaseName(Phase.EIP);
    }

    @JsonIgnore
    public PhaseRecord cpPhase() {
        return getByPhaseName(Phase.CP);
    }

    @JsonIgnore
    public PhaseRecord getByPhaseName(Phase phaseName) {
        for (PhaseRecord phase : this.all) {
            if (phase.getName().equals(phaseName)) return phase;
        }
        return null;
    }

    @JsonIgnore
    public PhaseRecord getLastCompletedPhase() {
        PhaseRecord lastCompletedPhase = null;
        for (PhaseRecord phase : this.all) {
            if (phase.getStartDate() != null && phase.getEndDate() != null) {
                lastCompletedPhase = phase;
            }
        }
        return lastCompletedPhase;
    }

    @JsonIgnore
    public PhaseRecord getCurrentPhase() {
        for (PhaseRecord phase : all) {
            if (phase.getStartDate() != null && phase.getEndDate() == null) {
                return phase;
            }
        }
        return null;
    }

    @JsonIgnore
    public LocalDate getIPStartDate() {
        return ipPhase().getStartDate();
    }

    @JsonIgnore
    void setIPStartDate(LocalDate IPStartDate) {
        ipPhase().setStartDate(IPStartDate);
    }

    @JsonIgnore
    public LocalDate getIPLastDate() {
        LocalDate ipEndDate = ipPhase().getEndDate();
        return ipEndDate != null ? ipEndDate : today();
    }

    @JsonIgnore
    public void setIPEndDate(LocalDate IPEndDate) {
        ipPhase().setEndDate(IPEndDate);
    }

    @JsonIgnore
    public LocalDate getEIPStartDate() {
        if (ipPhaseWasExtended()) {
            return eipPhase().getStartDate();
        }
        return null;
    }

    @JsonIgnore
    public void setEIPStartDate(LocalDate eipStartDate) {
        PhaseRecord eipPhase = eipPhase();
        eipPhase.setStartDate(eipStartDate);
        if (eipStartDate != null) {
            setIPEndDate(eipStartDate.minusDays(1));
        } else {
            if (cpPhase().hasStarted()) {
                setIPEndDate(cpPhase().getStartDate().minusDays(1));
            } else {
                setIPEndDate(null);
            }
            setEIPEndDate(null);
        }
    }

    @JsonIgnore
    public LocalDate getEIPLastDate() {
        if (ipPhaseWasExtended()) {
            LocalDate epEndDate = eipPhase().getEndDate();
            return epEndDate != null ? epEndDate : today();
        }
        return null;
    }

    @JsonIgnore
    public void setEIPEndDate(LocalDate eipEndDate) {
        eipPhase().setEndDate(eipEndDate);
    }

    @JsonIgnore
    public LocalDate getCPStartDate() {
        if (isOrHasBeenOnCp()) {
            return cpPhase().getStartDate();
        }
        return null;
    }

    @JsonIgnore
    public void setCPStartDate(LocalDate cpStartDate) {
        cpPhase().setStartDate(cpStartDate);
        PhaseRecord EIP = eipPhase();
        if (EIP.getStartDate() != null) {
            if (cpStartDate != null) {
                setEIPEndDate(cpStartDate.minusDays(1));
            } else {
                setEIPEndDate(null);
            }
        } else {
            if (cpStartDate != null) {
                setIPEndDate(cpStartDate.minusDays(1));
            } else {
                setIPEndDate(null);
            }
        }
    }

    @JsonIgnore
    public LocalDate getCPLastDate() {
        if (isOrHasBeenOnCp()) {
            LocalDate cpEndDate = cpPhase().getEndDate();
            return cpEndDate != null ? cpEndDate : today();
        }
        return null;
    }

    @JsonIgnore
    public void setCPEndDate(LocalDate cpEndDate) {
        cpPhase().setEndDate(cpEndDate);
    }

    @JsonIgnore
    public int indexOf(PhaseRecord currentPhase) {
        return all.indexOf(currentPhase);
    }

    public List<PhaseRecord> subList(int fromIndex, int toIndex) {
        return all.subList(fromIndex, toIndex);
    }
}
