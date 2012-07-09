package org.motechproject.whp.domain.v1;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;

@Data
public class PhasesV1 {

    private List<PhaseV1> all = new ArrayList<>();

    private PhaseNameV1 nextPhaseName = null;

    //ektorp
    public PhasesV1() {
    }

    public PhasesV1(List<PhaseV1> all) {
        this.all = all;
    }

    @JsonIgnore
    public boolean ipPhaseWasExtended() {
        PhaseV1 eipPhase = eipPhase();
        return eipPhase != null && eipPhase.getStartDate() != null;
    }

    @JsonIgnore
    public boolean isOrHasBeenOnCp() {
        PhaseV1 cpPhase = cpPhase();
        return cpPhase != null && cpPhase.hasStarted();
    }

    @JsonIgnore
    public boolean isOrHasBeenOnIp() {
        PhaseV1 ipPhase = ipPhase();
        return ipPhase != null && ipPhase.hasStarted();
    }

    @JsonIgnore
    public PhaseV1 ipPhase() {
        return getByPhaseName(PhaseNameV1.IP);
    }

    @JsonIgnore
    public PhaseV1 eipPhase() {
        return getByPhaseName(PhaseNameV1.EIP);
    }

    @JsonIgnore
    public PhaseV1 cpPhase() {
        return getByPhaseName(PhaseNameV1.CP);
    }

    @JsonIgnore
    public PhaseV1 getByPhaseName(PhaseNameV1 phaseName) {
        for (PhaseV1 phase : this.all) {
            if (phase.getName().equals(phaseName)) return phase;
        }
        return null;
    }

    @JsonIgnore
    public PhaseV1 getLastCompletedPhase() {
        PhaseV1 lastCompletedPhase = null;
        for (PhaseV1 phase : this.all) {
            if (phase.getStartDate() != null && phase.getEndDate() != null) {
                lastCompletedPhase = phase;
            }
        }
        return lastCompletedPhase;
    }

    @JsonIgnore
    public PhaseV1 getCurrentPhase() {
        for (PhaseV1 phase : all) {
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
        PhaseV1 eipPhase = eipPhase();
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
        PhaseV1 EIP = eipPhase();
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
    public int indexOf(PhaseV1 currentPhase) {
        return all.indexOf(currentPhase);
    }

    public List<PhaseV1> subList(int fromIndex, int toIndex) {
        return all.subList(fromIndex, toIndex);
    }
}
