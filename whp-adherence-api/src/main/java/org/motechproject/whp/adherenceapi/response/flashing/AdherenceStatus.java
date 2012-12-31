package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@EqualsAndHashCode
@Getter
class AdherenceStatus {

    @XmlElement(name = "provider_id")
    private String providerId;
    @XmlElement(name = "patient_remaining_count")
    private Integer patientRemainingCount;
    @XmlElement(name = "patient_given_count")
    private Integer patientGivenCount;
    @XmlElement(name = "patients_remaining")
    private PatientsRemaining patientsRemaining;

    AdherenceStatus() {
    }

    AdherenceStatus(String providerId, Integer patientRemainingCount, Integer patientGivenCount, List<String> patientsRemaining) {
        this.providerId = providerId;
        this.patientRemainingCount = patientRemainingCount;
        this.patientGivenCount = patientGivenCount;
        if (isNotEmpty(patientsRemaining)) {
            this.patientsRemaining = new PatientsRemaining(patientsRemaining);
        }
    }
}
