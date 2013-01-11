package org.motechproject.whp.adherence.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.patient.domain.Patient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

@EqualsAndHashCode
public class AdherenceSummaryByProvider implements Serializable {

    @Getter
    private String providerId;
    private List<Patient> patients;
    @Getter
    private List<Patient> allPatientsWithAdherence = new ArrayList<>();
    @Getter
    private List<Patient> allPatientsWithoutAdherence = new ArrayList<>();

    public AdherenceSummaryByProvider(String providerId, List<Patient> patients) {
        this.patients = patients;
        this.providerId = providerId;
        setAllPatientsWithAdherence();
        setAllPatientsWithoutAdherence();
    }

    public int countOfAllPatients() {
        return patients.size();
    }

    public int countOfPatientsWithAdherence() {
        return allPatientsWithAdherence.size();
    }

    public int countOfPatientsWithoutAdherence() {
        return allPatientsWithoutAdherence.size();
    }

    public boolean hasPatientsWithoutAdherence() {
        return allPatientsWithoutAdherence.size() > 0;
    }

    private void setAllPatientsWithAdherence() {
        allPatientsWithAdherence = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.hasAdherenceForLastReportingWeekForCurrentTherapy())
                allPatientsWithAdherence.add(patient);
        }
    }

    private void setAllPatientsWithoutAdherence() {
        allPatientsWithoutAdherence = new ArrayList<>(patients);
        allPatientsWithoutAdherence.removeAll(allPatientsWithAdherence);
    }

    public List<String> getAllPatientIdsWithoutAdherence() {
        return extract(getAllPatientsWithoutAdherence(), on(Patient.class).getPatientId());
    }
}
