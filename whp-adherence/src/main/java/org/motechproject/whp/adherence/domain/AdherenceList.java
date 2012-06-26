package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;

import java.util.ArrayList;

@Data
public class AdherenceList extends ArrayList<Adherence> {

    public AdherenceList() {
    }

    public LocalDate firstDoseTakenOn() {
        /*Assume that the set is ordered by date*/
        for (Adherence adherence : this) {
            if (PillStatus.Taken == adherence.getPillStatus())
                return adherence.getPillDate();
        }
        return null;
    }


}
