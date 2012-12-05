package org.motechproject.whp.adherenceapi.response;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@EqualsAndHashCode
class AdherenceStatus {

    @XmlElement(name = "patient_remaining_count")
    private String patientRemainingCount;
    @XmlElement(name = "patient_given_count")
    private String patientGivenCount;
    @XmlElement(name = "patients_remaining")
    private PatientsRemaining patientsRemaining;

    AdherenceStatus() {
    }

    AdherenceStatus(String patientRemainingCount, String patientGivenCount, List<String> patientsRemaining) {
        this.patientRemainingCount = patientRemainingCount;
        this.patientGivenCount = patientGivenCount;
        if (isNotEmpty(patientsRemaining)) {
            this.patientsRemaining = new PatientsRemaining(patientsRemaining);
        }
    }
}
