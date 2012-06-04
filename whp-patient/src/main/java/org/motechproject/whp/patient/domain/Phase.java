package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class Phase {

    public Phase(){
    }

    public Phase(PhaseName phaseName){
        this.name = phaseName;
    }

    private LocalDate startDate;
    private LocalDate endDate;
    private PhaseName name;
    /*Has to be updated under multiple cases. Identified so far:
    1) CMF Admin adherence update
    2) Provider adherence update
    3) CMF Admin startDate/endDate update
    4) Phase transition
    */
    private Integer numberOfDosesTaken;
}

