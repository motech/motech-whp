package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.*;

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

    AdherenceStatus(String providerId, List<String> patientsForProvider, List<String> patientsWithAdherence) {
        patientsForProvider = ifNullThenEmpty(patientsForProvider);
        patientsWithAdherence = ifNullThenEmpty(patientsWithAdherence);
        List<String> patientsRemaining = patientsRemaining(patientsForProvider, patientsWithAdherence);

        this.providerId = providerId;
        this.patientRemainingCount = patientsRemaining.size();
        this.patientGivenCount = patientGivenCount(patientsForProvider, patientsWithAdherence);
        if (isNotEmpty(patientsRemaining)) {
            this.patientsRemaining = new PatientsRemaining(patientsRemaining);
        }
    }

    private Integer patientGivenCount(List<String> patientsForProvider, List<String> patientsWithAdherence) {
        return intersection(patientsForProvider, patientsWithAdherence).size();
    }

    private List<String> ifNullThenEmpty(List<String> patients) {
        return (null == patients) ? Collections.<String>emptyList() : patients;
    }

    private List<String> patientsRemaining(List<String> patientsForProvider, List<String> patientsWithAdherence) {
        return new ArrayList<String>(subtract(patientsForProvider, patientsWithAdherence));
    }
}
