package org.motechproject.whp.adherence.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.domain.Patient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@EqualsAndHashCode
public class AdherenceSummaryByProvider implements Serializable {

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
        LocalDate currentWeekStartDate = TreatmentWeekInstance.currentWeekInstance().startDate();
        allPatientsWithAdherence = filter(having(on(Patient.class).getLastAdherenceWeekStartDate(), is(currentWeekStartDate)), patients);
    }

    private void setAllPatientsWithoutAdherence() {
        LocalDate currentWeekStartDate = TreatmentWeekInstance.currentWeekInstance().startDate();
        allPatientsWithoutAdherence = filter(having(on(Patient.class).getLastAdherenceWeekStartDate(), not(currentWeekStartDate)), patients);
    }
}
