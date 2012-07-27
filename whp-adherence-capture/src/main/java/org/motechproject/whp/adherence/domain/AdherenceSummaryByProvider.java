package org.motechproject.whp.adherence.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class AdherenceSummaryByProvider implements Serializable {

    private String providerId;
    private List<String> allPatients = new ArrayList<>();
    private List<String> allPatientsWithAdherence = new ArrayList<>();
    @Getter
    private List<String> allPatientsWithoutAdherence = new ArrayList<>();

    public AdherenceSummaryByProvider() {

    }

    public AdherenceSummaryByProvider(String providerId, List<String> allPatients, List<String> allPatientsWithAdherence) {
        this.providerId = providerId;
        this.allPatients = allPatients;
        this.allPatientsWithAdherence.addAll(allPatientsWithAdherence);
        setAllPatientsWithoutAdherence(allPatients, allPatientsWithAdherence);
    }

    private void setAllPatientsWithoutAdherence(List<String> allPatients, List<String> allPatientsWithAdherence) {
        allPatientsWithoutAdherence.addAll(allPatients);
        allPatientsWithoutAdherence.removeAll(allPatientsWithAdherence);
    }

    public int countOfAllPatients() {
        return allPatients.size();
    }

    public int countOfPatientsWithAdherence() {
        return allPatientsWithAdherence.size();
    }

    public int countOfPatientsWithoutAdherence() {
        return allPatientsWithoutAdherence.size();
    }

}
