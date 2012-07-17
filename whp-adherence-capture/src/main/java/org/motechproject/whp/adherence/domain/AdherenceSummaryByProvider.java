package org.motechproject.whp.adherence.domain;

import lombok.Data;

import java.util.List;

@Data
public class AdherenceSummaryByProvider {

    private String providerId;
    private List<String> allPatients;
    private List<String> allPatientsWithAdherence;

    public AdherenceSummaryByProvider(){

    }

    public AdherenceSummaryByProvider(String providerId, List<String> allPatients, List<String> allPatientsWithAdherence) {
        this.providerId = providerId;
        this.allPatients = allPatients;
        this.allPatientsWithAdherence = allPatientsWithAdherence;
    }

    public int countOfAllPatients() {
        return (allPatients == null) ? 0 : allPatients.size();
    }

    public int countOfPatientsWithAdherence() {
        return (allPatientsWithAdherence == null) ? 0 : allPatientsWithAdherence.size();
    }

    public int countOfPatientsWithoutAdherence() {
        return countOfAllPatients() - countOfPatientsWithAdherence();
    }

}
