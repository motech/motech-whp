package org.motechproject.whp.adherenceapi.domain;

import lombok.Data;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

@Data
public class AdherenceSummary {
    private List<String> patientsWithAdherence;
    private List<Patient> patientsForProvider;

    public AdherenceSummary(List<String> patientsWithAdherence, List<Patient> patientsForProvider) {
        this.patientsWithAdherence = patientsWithAdherence;
        this.patientsForProvider = patientsForProvider;
    }
}
