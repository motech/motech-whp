package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@EqualsAndHashCode
class AdherenceStatus {

    @XmlElement(name = "patient_remaining_count")
    private Integer patientRemainingCount;
    @XmlElement(name = "patient_given_count")
    private Integer patientGivenCount;
    @XmlElement(name = "patients_remaining")
    private PatientsRemaining patientsRemaining;

    AdherenceStatus() {
    }

    AdherenceStatus(Integer patientRemainingCount, Integer patientGivenCount, List<String> patientsRemaining) {
        this.patientRemainingCount = patientRemainingCount;
        this.patientGivenCount = patientGivenCount;
        if (isNotEmpty(patientsRemaining)) {
            this.patientsRemaining = new PatientsRemaining(patientsRemaining);
        }
    }
}
