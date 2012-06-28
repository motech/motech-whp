package org.motechproject.whp.treatmentcard.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdherenceSection {

    private List<MonthlyAdherence> monthlyAdherences = new ArrayList<>();

    private List<String> providerIds = new ArrayList<String>();

    private boolean isSundayDoseDate;

    public AdherenceSection() {
    }

}
