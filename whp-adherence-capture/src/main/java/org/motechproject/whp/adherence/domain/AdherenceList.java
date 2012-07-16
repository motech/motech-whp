package org.motechproject.whp.adherence.domain;

import lombok.Data;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class AdherenceList extends ArrayList<Adherence> {

    public AdherenceList() {
    }

    public AdherenceList(Collection<? extends Adherence> c) {
        super(c);
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
