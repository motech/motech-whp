package org.motechproject.whp.adherenceapi.domain;

import lombok.Data;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

@Data
public class AdherenceSummary {

    private String providerId;
    private List<String> patientsWithAdherence;
    private List<Patient> patientsUnderProvider;

    public AdherenceSummary(String providerId, List<String> patientsWithAdherence, List<Patient> patientsUnderProvider) {
        this.providerId = providerId;
        this.patientsWithAdherence = patientsWithAdherence;
        this.patientsUnderProvider = patientsUnderProvider;
    }

}
